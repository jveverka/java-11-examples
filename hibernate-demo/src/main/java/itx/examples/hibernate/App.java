package itx.examples.hibernate;

import itx.examples.hibernate.entities.UserData;
import itx.examples.hibernate.services.DataAccessService;
import itx.examples.hibernate.services.DataAccessServiceImpl;
import itx.examples.hibernate.utils.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        DataAccessService dataAccessService = new DataAccessServiceImpl(HibernateUtil.getPostgresql10Properties());

        //1. create and save new user
        UserData userData = new UserData("Juraj", "Veverka", "juraj.veverka@inter-net.sk");
        dataAccessService.saveUserData(userData);
        LOG.info("UserData saved");

        //2. get all users
        Collection<UserData> userDataCollection = dataAccessService.getUserData();
        LOG.info("UserData [{}]", userDataCollection.size());

        //3. delete all users
        userDataCollection.forEach(u->{
            LOG.info("UserData: {}", u);
            dataAccessService.deleteUserData(u.getId());
        });

        //4. close the service
        dataAccessService.close();
    }

}
