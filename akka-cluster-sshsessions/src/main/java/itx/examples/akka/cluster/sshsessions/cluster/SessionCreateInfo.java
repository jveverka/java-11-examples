package itx.examples.akka.cluster.sshsessions.cluster;

/**
 * Created by juraj on 3/19/17.
 */
public class SessionCreateInfo {

    private String clientId;
    private String memberAddress;
    private String clientActorAddress;

    public SessionCreateInfo(String clientId, String memberAddress, String clientActorAddress) {
        this.clientId = clientId;
        this.memberAddress = memberAddress;
        this.clientActorAddress = clientActorAddress;
    }

    public String getClientId() {
        return clientId;
    }

    public String getMemberAddress() {
        return memberAddress;
    }

    public String getClientActorAddress() {
        return clientActorAddress;
    }

}
