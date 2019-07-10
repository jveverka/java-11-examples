package itx.examples.akka.cluster.sshsessions;

import com.beust.jcommander.Parameter;

public class Arguments {

    public final static String CLUSTER_TYPE_STATIC = "static";
    public final static String CLUSTER_TYPE_DYNAMIC = "dynamic";

    @Parameter(names = {"-t", "--cluster-type" }, description = "Type of akka cluster")
    private String clusterType = CLUSTER_TYPE_STATIC;

    @Parameter(names = {"-c", "--config-path" }, description = "Akka config path")
    private String configPath;

    public String getClusterType() {
        return clusterType;
    }

    public String getConfigPath() {
        return configPath;
    }

}
