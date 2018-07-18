package itx.examples.jetty.common;

import java.io.InputStream;
import java.security.KeyStore;

public final class SystemUtils {

    private SystemUtils() {
    }

    public static KeyStore loadJKSKeyStore(String classPath, String password) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        InputStream is = SystemUtils.class.getClassLoader().getResourceAsStream(classPath);
        keyStore.load(is, password.toCharArray());
        return keyStore;
    }

    public static String[] getURLParameters(String baseURI, String fullURI) {
        if (baseURI.equals(fullURI)) {
            return new String[] {};
        }
        String parameters = fullURI.substring(baseURI.length() + 1);
        if ("".equals(parameters)) {
            return new String[] {};
        }
        String[] params = parameters.split("/");
        return params;
    }

}
