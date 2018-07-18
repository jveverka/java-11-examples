package itx.examples.grpc.service;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import io.grpc.stub.StreamObserver;
import itx.examples.grpc.service.commands.GetDataAsyncScenario;
import itx.examples.grpc.service.commands.GetDataSyncScenario;
import itx.examples.grpc.service.commands.SayHelloScenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Main {

    final private static Logger LOG = LoggerFactory.getLogger(Main.class);
    final private static String SAY_HELLO_SCENARIO = "sayHello";
    final private static String GET_DATA_SYNC_SCENARIO = "getDataSync";
    final private static String GET_DATA_ASYNC_SCENARIO = "getDataAsync";

    @Parameter(names = {"--host"}, description = "Host name of the server, if in client mode, or interface ip address if in server mode.")
    private String host;

    @Parameter(names = {"--port"}, required = true, description = "Port of the server or client.")
    private int port;

    @Parameter(names = {"--help", "-h", "--info", "-i"}, help = true)
    private boolean help;

    public static void main(String[] argv) {
        try {
            Main main = new Main();
            SayHelloScenario sayHelloScenario = new SayHelloScenario();
            GetDataSyncScenario getDataSyncScenario = new GetDataSyncScenario();
            GetDataAsyncScenario getDataAsyncScenario = new GetDataAsyncScenario();
            JCommander jCommander = JCommander.newBuilder()
                .addObject(main)
                .addCommand(SAY_HELLO_SCENARIO, sayHelloScenario)
                .addCommand(GET_DATA_SYNC_SCENARIO, getDataSyncScenario)
                .addCommand(GET_DATA_ASYNC_SCENARIO, getDataAsyncScenario)
                .build();
            jCommander.parse(argv);
            main.run(jCommander,
                    sayHelloScenario,
                    getDataSyncScenario,
                    getDataAsyncScenario);
        } catch (Exception e) {
            LOG.error("Exception: ", e);
        }
    }

    public void run(JCommander jCommander,
                    SayHelloScenario sayHelloScenario,
                    GetDataSyncScenario getDataSyncScenario,
                    GetDataAsyncScenario getDataAsyncScenario) throws Exception {
        if (help) {
            LOG.info("processing help command.");
            jCommander.usage();
            System.exit(1);
        } else {
            String scenarioName = jCommander.getParsedCommand();
            if (scenarioName == null) {
                LOG.info("Starting gRPC server at port {}:{}", host, port);
                final SimpleServer server = new SimpleServer(host, port);
                server.start();
                server.blockUntilShutdown();
                LOG.info("gRPC server stopped.");
                return;
            } else {
                LOG.info("Starting gRPC client {}:{} with scenario {}", host, port, scenarioName);
                SimpleClient client = new SimpleClient(host, port);
                try {
                    switch (scenarioName) {
                        case SAY_HELLO_SCENARIO: {
                            HelloReply helloReply = client.greet(sayHelloScenario.getMessage());
                            LOG.info("response from server: {}", helloReply.getMessage());
                            break;
                        }
                        case GET_DATA_SYNC_SCENARIO: {
                            LOG.info("repeat cycles: {}", getDataSyncScenario.getRepeat());
                            float averageMsgInSec = 0;
                            boolean testResult = true;
                            for (int r = 0; r < getDataSyncScenario.getRepeat(); r++) {
                                LOG.info("warming up, sending {} \"{}\" messages ", getDataSyncScenario.getWarmupCount(), getDataSyncScenario.getMessage());
                                boolean result = sendDataMessagesSync(getDataSyncScenario.getWarmupCount(), getDataSyncScenario.getMessage(), client);
                                LOG.info("warm-up result: {}", result);
                                Thread.sleep(500);
                                LOG.info("sending {} \"{}\" messages", getDataSyncScenario.getMessageCount(), getDataSyncScenario.getMessage());
                                long delay = System.currentTimeMillis();
                                result = sendDataMessagesSync(getDataSyncScenario.getMessageCount(), getDataSyncScenario.getMessage(), client);
                                delay = System.currentTimeMillis() - delay;
                                float msgInSec = (getDataSyncScenario.getMessageCount() / (delay / 1000f));
                                averageMsgInSec = averageMsgInSec + msgInSec;
                                testResult = testResult & result;
                                LOG.info("send {} messages in {}ms = {} msg/s, result: {} {}/{}", getDataSyncScenario.getMessageCount(), delay,
                                        msgInSec, result, (r + 1), getDataSyncScenario.getRepeat());
                            }
                            LOG.info("average result: {} msg/s, testOK: {}", (averageMsgInSec / getDataSyncScenario.getRepeat()), testResult);
                            break;
                        }
                        case GET_DATA_ASYNC_SCENARIO: {
                            LOG.info("repeat cycles: {}", getDataAsyncScenario.getRepeat());
                            float averageMsgInSec = 0;
                            boolean testResult = true;
                            for (int r = 0; r < getDataAsyncScenario.getRepeat(); r++) {
                                LOG.info("warming up, sending {} \"{}\" messages ", getDataAsyncScenario.getWarmupCount(), getDataAsyncScenario.getMessage());
                                boolean result = sendDataMessagesAsync(getDataAsyncScenario.getWarmupCount(), getDataAsyncScenario.getMessage(), client);
                                LOG.info("warm-up result: {}", result);
                                Thread.sleep(500);
                                LOG.info("sending {} \"{}\" messages", getDataAsyncScenario.getMessageCount(), getDataAsyncScenario.getMessage());
                                long delay = System.currentTimeMillis();
                                result = sendDataMessagesAsync(getDataAsyncScenario.getMessageCount(), getDataAsyncScenario.getMessage(), client);
                                delay = System.currentTimeMillis() - delay;
                                float msgInSec = (getDataAsyncScenario.getMessageCount() / (delay / 1000f));
                                averageMsgInSec = averageMsgInSec + msgInSec;
                                testResult = testResult & result;
                                LOG.info("send {} messages in {}ms = {} msg/s, result: {} {}/{}", getDataAsyncScenario.getMessageCount(), delay,
                                        msgInSec, result, (r + 1), getDataAsyncScenario.getRepeat());
                            }
                            LOG.info("average result: {} msg/s, testOK: {}", (averageMsgInSec / getDataAsyncScenario.getRepeat()), testResult);
                            break;
                        }
                        default: {
                            LOG.error("Unsupported scenario name: {}", scenarioName);
                            break;
                        }
                    }
                } finally {
                    LOG.info("gRPC client shutting down.");
                    client.shutdown();
                    LOG.info("gRPC client stopped.");
                }
            }
        }
    }

    private boolean sendDataMessagesSync(int messageCount, String message, SimpleClient client) {
        boolean dataOk = true;
        for (int i = 0; i < messageCount; i++) {
            DataMessage dataMessage = DataMessage.newBuilder().setIndex(i).setMessage(message).build();
            DataMessage replyMessage = client.getData(dataMessage);
            if (!dataMessage.equals(replyMessage)) {
                dataOk = false;
            }
        }
        return dataOk;
    }

    private boolean sendDataMessagesAsync(int messageCount, String message, SimpleClient client) throws InterruptedException {
        boolean[] msgConfirmation = new boolean[messageCount];
        DataMessage closeChannelMessage = DataMessage.newBuilder().setIndex(0).setMessage("CLOSE").build();
        ClientDataChannelStreamObserver incomingStreamObserver = new ClientDataChannelStreamObserver(msgConfirmation);
        StreamObserver outgoingStreamObserver = client.getDataChannel(incomingStreamObserver);
        for (int i = 0; i < messageCount; i++) {
            msgConfirmation[i] = false;
            DataMessage dataMessage = DataMessage.newBuilder().setIndex(i).setMessage(message).build();
            outgoingStreamObserver.onNext(dataMessage);
        }

        outgoingStreamObserver.onNext(closeChannelMessage);
        incomingStreamObserver.awaitCompletion(5, TimeUnit.MINUTES);

        for (int i = 0; i < messageCount; i++) {
            if (!msgConfirmation[i]) {
                return false;
            }
        }
        return true;
    }

}