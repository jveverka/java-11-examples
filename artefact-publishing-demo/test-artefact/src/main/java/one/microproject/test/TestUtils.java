package one.microproject.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUtils {

    private static final Logger LOG = LoggerFactory.getLogger(TestUtils.class);

    private TestUtils() {
    }

    public static String getMessage() {
        LOG.info("hello world !");
        return "hello world !";
    }

}
