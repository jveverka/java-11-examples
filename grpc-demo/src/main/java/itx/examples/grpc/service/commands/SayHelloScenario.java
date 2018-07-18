package itx.examples.grpc.service.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "sayHello scenario, runs in client mode if specified")
public class SayHelloScenario {

    @Parameter(names = "-m", description = "message payload")
    private String message = "world";

    public String getMessage() {
        return message;
    }

}
