package itx.examples.kafka.client;

import com.beust.jcommander.Parameter;

public class Arguments {

    @Parameter(names = {"-i", "--identity" }, description = "Unique client Id")
    private String clientId = "default-id";

    public String getClientId() {
        return clientId;
    }

}
