package itx.examples.akka.cluster.sshsessions;

/**
 * Created by juraj on 3/18/17.
 */
public final class Utils {

    private Utils() {
    }

    public static final String CLUSTER_MANAGER_NAME = "ssh-cluster-manager-actor";
    public static final String LOCAL_MANAGER_NAME = "ssh-local-manager-actor";
    public static final String CLUSTER_NAME = "test-cluster";

    public static String getSshClusterManagerAddress(String clusterAddress) {
        return clusterAddress + "/user/" + CLUSTER_MANAGER_NAME;
    }

    public static String getSshClientAddress(String clusterAddress, String clientId) {
        return clusterAddress + "/user/ssh-client-" + clientId;
    }

    public static String getSshSessionAddress(String clusterAddress, String sessionId) {
        return clusterAddress + "/user/ssh-session-" + sessionId;
    }

    public static String getSshSessionsSelectionAddress(String clusterAddress) {
        return clusterAddress + "/user/ssh-session-*";
    }

    public static String getSshLocalManagerActorAddress(String clusterAddress) {
        return clusterAddress + "/user/" + LOCAL_MANAGER_NAME;
    }

    public static String generateClientActorName(String clientId) {
        return "ssh-client-" + clientId;
    }

    public static String generateSessionActorName(String sessionId) {
        return "ssh-session-" + sessionId;
    }

}
