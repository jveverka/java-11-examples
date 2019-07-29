package itx.examples.hibernate.services;

import itx.examples.hibernate.entities.UserData;
import itx.examples.hibernate.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Collection;

public class DataAccessServiceImpl implements DataAccessService {

    private final SessionFactory sessionFactory;

    public DataAccessServiceImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void saveUserData(UserData userData) {
        Transaction transaction = null;
        try(Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(userData);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public Collection<UserData> getUserData() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from UserData", UserData.class).list();
        }
    }

}
