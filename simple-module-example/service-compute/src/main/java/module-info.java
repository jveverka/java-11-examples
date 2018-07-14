module itx.examples.java.eleven.compute {
    exports itx.examples.java.eleven.compute.api;
    provides itx.examples.java.eleven.compute.api.ComputeService with itx.examples.java.eleven.compute.impl.ComputeServiceImpl;
    requires org.slf4j;
}