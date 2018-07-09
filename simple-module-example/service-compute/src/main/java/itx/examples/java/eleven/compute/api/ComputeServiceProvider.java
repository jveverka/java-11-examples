package itx.examples.java.eleven.compute.api;

import itx.examples.java.eleven.compute.impl.ComputeServiceImpl;

public class ComputeServiceProvider {

    public static ComputeService getComputeService() {
        return new ComputeServiceImpl();
    }

}
