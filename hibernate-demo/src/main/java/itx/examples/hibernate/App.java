package itx.examples.hibernate;

import itx.examples.hibernate.entities.UserData;
import itx.examples.hibernate.services.DataAccessService;
import itx.examples.hibernate.services.DataAccessServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class App {

    final private static Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        DataAccessService dataAccessService = new DataAccessServiceImpl();
        UserData userData = new UserData("Juraj", "Veverka", "juraj.veverka@inter-net.sk");
        dataAccessService.saveUserData(userData);
        LOG.info("UserData saved");
        Collection<UserData> userDataCollection = dataAccessService.getUserData();
        LOG.info("UserData [{}]", userDataCollection.size());
        userDataCollection.forEach(u->{
            LOG.info("UserData: {}", u);
        });
    }

}
