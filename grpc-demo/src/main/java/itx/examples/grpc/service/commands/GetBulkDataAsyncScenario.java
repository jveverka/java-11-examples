package itx.examples.grpc.service.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "getBulkDataAsync scenario, runs in client mode if specified")
public class GetBulkDataAsyncScenario {

    @Parameter(names = "-w", description = "number of warm-up data messages")
    private int warmupCount = 1000;

    @Parameter(names = "-c", description = "number of data messages")
    private int messageCount = 1000;

    @Parameter(names = "-m", description = "message payload")
    private String message = "data";

    @Parameter(names = "-r", description = "number repeat cycles")
    private int repeat = 1;

    @Parameter(names = "-b", description = "number of messages in one bulk")
    private int bulkCount = 1;

    public int getWarmupCount() {
        return warmupCount;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public String getMessage() {
        return message;
    }

    public int getRepeat() {
        return repeat;
    }

    public int getBulkCount() {
        return bulkCount;
    }

}
