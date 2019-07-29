package itx.examples.hibernate.utils;

import itx.examples.hibernate.entities.UserData;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public final class HibernateUtil {

    private HibernateUtil() {
    }

    public static Properties getPostgresql10Properties() {
        Properties properties = new Properties();
        properties.put(Environment.DRIVER, "org.postgresql.Driver");
        // jdbc:postgresql://host:port/database
        properties.put(Environment.URL, "jdbc:postgresql://127.0.0.1:5432/userdata");
        properties.put(Environment.USER, "juraj");
        properties.put(Environment.PASS, "secret");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQL10Dialect");
        properties.put(Environment.SHOW_SQL, "true");
        properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.put(Environment.HBM2DDL_AUTO, "update");
        return properties;
    }

    public static SessionFactory getSessionFactory(Properties properties) {
        Configuration configuration = new Configuration();

        configuration.setProperties(properties);
        configuration.addAnnotatedClass(UserData.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

}
