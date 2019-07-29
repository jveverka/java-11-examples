package itx.examples.hibernate.services;

import itx.examples.hibernate.entities.UserData;
import itx.examples.hibernate.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Properties;

public class DataAccessServiceImpl implements DataAccessService {

    private static final Logger LOG = LoggerFactory.getLogger(DataAccessServiceImpl.class);

    private final SessionFactory sessionFactory;

    public DataAccessServiceImpl(Properties properties) {
        this.sessionFactory = HibernateUtil.getSessionFactory(properties);
    }

    @Override
    public void saveUserData(UserData userData) {
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            LOG.info("save user data, TX started");
            session.save(userData);
            transaction.commit();
            LOG.info("save user data, TX committed");
        } catch (Exception e) {
            LOG.error("Error: ", e);
            if (transaction != null) {
                LOG.info("user data rollback, TX rollback");
                transaction.rollback();
            }
        }
    }

    @Override
    public Collection<UserData> getUserData() {
        try (Session session = this.sessionFactory.openSession()) {
            return session.createQuery("from UserData", UserData.class).list();
        }
    }

    @Override
    public void deleteUserData(long id) {
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            LOG.info("delete user data id={}, TX started", id);
            //Delete a transient  object
            UserData userData = new UserData(id);
            session.delete(userData);
            transaction.commit();
            LOG.info("delete user data id={}, TX committed", id);
        } catch (Exception e) {
            LOG.error("Error: ", e);
            if (transaction != null) {
                LOG.info("user data rollback, TX rollback");
                transaction.rollback();
            }
        }
    }

    @Override
    public void close() throws Exception {
        this.sessionFactory.close();
    }

}
