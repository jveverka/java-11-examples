package itx.examples.java.eleven.application.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.Callable;

public final class Utils {

    final private static Logger LOG = LoggerFactory.getLogger(Utils.class);

    private Utils() {
        throw new UnsupportedOperationException("do not instantiate utility class");
    }

    public static Callable<String> getSimpleTask() {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                String result = "";
                LOG.info("task start");
                result = (new Date()).toString();
                LOG.info("task done");
                return result;
            }
        };
    }

    public static <T> T getServiceOrFail(Class<T> type) {
        ServiceLoader<T> loader = ServiceLoader.load(type);
        Optional<T> serviceOptional = loader.findFirst();
        if (serviceOptional.isPresent()) {
            return serviceOptional.get();
        } else {
            throw new UnsupportedOperationException("service not found");
        }
    }

}
