package itx.examples.hibernate.tests;

import itx.examples.hibernate.entities.UserData;
import itx.examples.hibernate.services.DataAccessService;
import itx.examples.hibernate.services.DataAccessServiceImpl;
import org.hibernate.cfg.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Properties;

public class HibernateTest {

    private static final Logger LOG = LoggerFactory.getLogger(HibernateTest.class);

    public static Properties getDerbyProperties() {
        Properties properties = new Properties();
        properties.put(Environment.DRIVER, "org.apache.derby.jdbc.EmbeddedDriver");
        properties.put(Environment.URL, "jdbc:derby:memory:userdata;create=true");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.DerbyDialect");
        properties.put(Environment.SHOW_SQL, "true");
        properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.put(Environment.HBM2DDL_AUTO, "update");
        return properties;
    }

    private DataAccessService dataAccessService;

    @BeforeClass
    public void init() {
        LOG.info("init ...");
        dataAccessService = new DataAccessServiceImpl(getDerbyProperties());
    }

    @Test
    public void testDataAccessService() {
        UserData userData = new UserData("Juraj", "Veverka", "juraj.veverka@inter-net.sk");
        dataAccessService.saveUserData(userData);

        Collection<UserData> userDataCollection = dataAccessService.getUserData();
        Assert.assertNotNull(userDataCollection);
        Assert.assertFalse(userDataCollection.isEmpty());
        UserData userDataFromDb = userDataCollection.iterator().next();
        Assert.assertNotNull(userDataFromDb);
        Assert.assertNotNull(userDataFromDb.getId());
        Assert.assertEquals(userDataFromDb.getFirstName(), "Juraj");
        Assert.assertEquals(userDataFromDb.getLastName(), "Veverka");
        Assert.assertEquals(userDataFromDb.getEmail(), "juraj.veverka@inter-net.sk");

        dataAccessService.deleteUserData(userDataFromDb.getId());
        userDataCollection = dataAccessService.getUserData();
        Assert.assertNotNull(userDataCollection);
        Assert.assertTrue(userDataCollection.isEmpty());
    }

    @AfterClass
    public void shutdown() throws Exception {
        LOG.info("shutdown.");
        dataAccessService.close();
    }

}
