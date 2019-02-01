package itx.examples.grpc.service;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import io.grpc.stub.StreamObserver;
import itx.examples.grpc.service.commands.GetBulkDataAsyncScenario;
import itx.examples.grpc.service.commands.GetBulkDataSyncScenario;
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
    final private static String GET_BULK_DATA_SYNC_SCENARIO = "getBulkData";
    final private static String GET_BULK_DATA_ASYNC_SCENARIO = "getBulkDataAsync";

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
            GetBulkDataSyncScenario getBulkDataSyncScenario = new GetBulkDataSyncScenario();
            GetBulkDataAsyncScenario getBulkDataAsyncScenario = new GetBulkDataAsyncScenario();
            JCommander jCommander = JCommander.newBuilder()
                .addObject(main)
                .addCommand(SAY_HELLO_SCENARIO, sayHelloScenario)
                .addCommand(GET_DATA_SYNC_SCENARIO, getDataSyncScenario)
                .addCommand(GET_DATA_ASYNC_SCENARIO, getDataAsyncScenario)
                .addCommand(GET_BULK_DATA_SYNC_SCENARIO, getBulkDataSyncScenario)
                .addCommand(GET_BULK_DATA_ASYNC_SCENARIO, getBulkDataAsyncScenario)
                .build();
            jCommander.parse(argv);
            main.run(jCommander,
                    sayHelloScenario,
                    getDataSyncScenario,
                    getDataAsyncScenario,
                    getBulkDataSyncScenario,
                    getBulkDataAsyncScenario);
        } catch (Exception e) {
            LOG.error("Exception: ", e);
        }
    }

    public void run(JCommander jCommander,
                    SayHelloScenario sayHelloScenario,
                    GetDataSyncScenario getDataSyncScenario,
                    GetDataAsyncScenario getDataAsyncScenario,
                    GetBulkDataSyncScenario getBulkDataSyncScenario,
                    GetBulkDataAsyncScenario getBulkDataAsyncScenario) throws Exception {
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
                            for (int r=0; r<4; r++) {
                                LOG.info("warming up, sending {} \"{}\" messages ", getDataSyncScenario.getWarmupCount(), getDataSyncScenario.getMessage());
                                boolean resultWarmup = sendDataMessagesSync(getDataSyncScenario.getWarmupCount(), getDataSyncScenario.getMessage(), client);
                                LOG.info("warm-up result: {}", resultWarmup);
                                Thread.sleep(500);
                            }

                            float averageMsgInSec = 0;
                            boolean testResult = true;
                            for (int r = 0; r < getDataSyncScenario.getRepeat(); r++) {
                                LOG.info("sending {} \"{}\" messages", getDataSyncScenario.getMessageCount(), getDataSyncScenario.getMessage());
                                long delay = System.currentTimeMillis();
                                boolean result = sendDataMessagesSync(getDataSyncScenario.getMessageCount(), getDataSyncScenario.getMessage(), client);
                                delay = System.currentTimeMillis() - delay;
                                float msgInSec = (getDataSyncScenario.getMessageCount() / (delay / 1000f));
                                averageMsgInSec = averageMsgInSec + msgInSec;
                                testResult = testResult & result;
                                LOG.info("send {} messages in {}ms = {} msg/s, result: {} {}/{}", getDataSyncScenario.getMessageCount(), delay,
                                        msgInSec, result, (r + 1), getDataSyncScenario.getRepeat());
                                Thread.sleep(200);
                            }
                            LOG.info("average result: {} msg/s, testOK: {}", (averageMsgInSec / getDataSyncScenario.getRepeat()), testResult);
                            break;
                        }
                        case GET_DATA_ASYNC_SCENARIO: {
                            LOG.info("repeat cycles: {}", getDataAsyncScenario.getRepeat());

                            for (int r=0; r<4; r++) {
                                LOG.info("warming up, sending {} \"{}\" messages ", getDataAsyncScenario.getWarmupCount(), getDataAsyncScenario.getMessage());
                                boolean resultWarmup = sendDataMessagesAsync(getDataAsyncScenario.getWarmupCount(), getDataAsyncScenario.getMessage(), client);
                                LOG.info("warm-up result: {}", resultWarmup);
                                Thread.sleep(500);
                            }

                            float averageMsgInSec = 0;
                            boolean testResult = true;
                            for (int r = 0; r < getDataAsyncScenario.getRepeat(); r++) {
                                LOG.info("sending {} \"{}\" messages", getDataAsyncScenario.getMessageCount(), getDataAsyncScenario.getMessage());
                                long delay = System.currentTimeMillis();
                                boolean result = sendDataMessagesAsync(getDataAsyncScenario.getMessageCount(), getDataAsyncScenario.getMessage(), client);
                                delay = System.currentTimeMillis() - delay;
                                float msgInSec = (getDataAsyncScenario.getMessageCount() / (delay / 1000f));
                                averageMsgInSec = averageMsgInSec + msgInSec;
                                testResult = testResult & result;
                                LOG.info("send {} messages in {}ms = {} msg/s, result: {} {}/{}", getDataAsyncScenario.getMessageCount(), delay,
                                        msgInSec, result, (r + 1), getDataAsyncScenario.getRepeat());
                                Thread.sleep(200);
                            }
                            LOG.info("average result: {} msg/s, testOK: {}", (averageMsgInSec / getDataAsyncScenario.getRepeat()), testResult);
                            break;
                        }
                        case GET_BULK_DATA_SYNC_SCENARIO: {
                            LOG.info("repeat cycles: {}", getBulkDataSyncScenario.getRepeat());

                            for (int r=0; r<4; r++) {
                                LOG.info("warming up, sending {} \"{}\" messages ", getBulkDataSyncScenario.getWarmupCount(), getBulkDataSyncScenario.getMessage());
                                boolean resultWarmup = sendBulkDataMessagesSync(getBulkDataSyncScenario.getWarmupCount(),
                                        getBulkDataSyncScenario.getBulkCount(), getBulkDataSyncScenario.getMessage(), client);
                                LOG.info("warm-up result: {}", resultWarmup);
                                Thread.sleep(500);
                            }

                            float averageMsgInSec = 0;
                            boolean testResult = true;
                            for (int r = 0; r < getBulkDataSyncScenario.getRepeat(); r++) {
                                LOG.info("sending {}x{} \"{}\" messages", getBulkDataSyncScenario.getMessageCount(), getBulkDataSyncScenario.getBulkCount(), getBulkDataSyncScenario.getMessage());
                                long delay = System.currentTimeMillis();
                                boolean result = sendBulkDataMessagesSync(getBulkDataSyncScenario.getMessageCount(),
                                        getBulkDataSyncScenario.getBulkCount(), getBulkDataSyncScenario.getMessage(), client);
                                delay = System.currentTimeMillis() - delay;
                                float msgInSec = ((getBulkDataSyncScenario.getMessageCount()*getBulkDataSyncScenario.getBulkCount())/ (delay / 1000f));
                                averageMsgInSec = averageMsgInSec + msgInSec;
                                testResult = testResult & result;
                                LOG.info("send {}x{} messages in {}ms = {} msg/s, result: {} {}/{}", getBulkDataSyncScenario.getMessageCount(), getBulkDataSyncScenario.getBulkCount(), delay,
                                        msgInSec, result, (r + 1), getBulkDataSyncScenario.getRepeat());
                                Thread.sleep(200);
                            }
                            LOG.info("average result: {} msg/s, testOK: {}", (averageMsgInSec / getBulkDataSyncScenario.getRepeat()), testResult);
                            break;
                        }
                        case GET_BULK_DATA_ASYNC_SCENARIO: {
                            LOG.info("repeat cycles: {}", getBulkDataAsyncScenario.getRepeat());

                            for (int r=0; r<4; r++) {
                                LOG.info("warming up, sending {} \"{}\" messages ", getBulkDataAsyncScenario.getWarmupCount(), getBulkDataAsyncScenario.getMessage());
                                boolean resultWarmup = sendBulkDataMessagesAsync(getBulkDataAsyncScenario.getWarmupCount(),
                                        getBulkDataAsyncScenario.getBulkCount(), getBulkDataAsyncScenario.getMessage(), client);
                                LOG.info("warm-up result: {}", resultWarmup);
                                Thread.sleep(500);
                            }

                            float averageMsgInSec = 0;
                            boolean testResult = true;
                            for (int r = 0; r < getBulkDataAsyncScenario.getRepeat(); r++) {
                                LOG.info("sending {}x{} \"{}\" messages", getBulkDataAsyncScenario.getMessageCount(), getBulkDataAsyncScenario.getBulkCount(), getBulkDataAsyncScenario.getMessage());
                                long delay = System.currentTimeMillis();
                                boolean result = sendBulkDataMessagesAsync(getBulkDataAsyncScenario.getMessageCount(),
                                        getBulkDataAsyncScenario.getBulkCount(),getBulkDataAsyncScenario.getMessage(), client);
                                delay = System.currentTimeMillis() - delay;
                                float msgInSec = ((getBulkDataAsyncScenario.getMessageCount()*getBulkDataAsyncScenario.getBulkCount())/ (delay / 1000f));
                                averageMsgInSec = averageMsgInSec + msgInSec;
                                testResult = testResult & result;
                                LOG.info("send {}x{} messages in {}ms = {} msg/s, result: {} {}/{}", getBulkDataAsyncScenario.getMessageCount(), getBulkDataAsyncScenario.getBulkCount(), delay,
                                        msgInSec, result, (r + 1), getBulkDataAsyncScenario.getRepeat());
                                Thread.sleep(200);
                            }
                            LOG.info("average result: {} msg/s, testOK: {}", (averageMsgInSec / getBulkDataAsyncScenario.getRepeat()), testResult);
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
        for (int i = 0; i < messageCount; i++) {
            DataMessage dataMessage = createMessage(i, message);
            DataMessage replyMessage = client.getData(dataMessage);
            if (!dataMessage.equals(replyMessage)) {
                return false;
            }
        }
        return true;
    }

    private boolean sendBulkDataMessagesSync(int messageCount, int bulkCount, String message, SimpleClient client) {
        int index = 0;
        for (int i = 0; i < messageCount; i++) {
            //1. generate bulk message
            DataMessages.Builder builder = DataMessages.newBuilder();
            for (int j = 0; j<bulkCount; j++) {
                DataMessage dataMessage = createMessage(index, message);
                builder.addMessages(dataMessage);
                index++;
            }
            //2. send bulk message and wait for reply
            DataMessages replyMessage = client.getBulkData(builder.build());
            //3. verify received message
            if (replyMessage.getMessagesCount() != bulkCount) {
                return false;
            }
            for (int j=0; j<replyMessage.getMessagesCount(); j++) {
                if (!replyMessage.getMessages(j).getMessage().equals(message)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean sendDataMessagesAsync(int messageCount, String message, SimpleClient client) throws InterruptedException {
        ClientDataChannelStreamObserver incomingStreamObserver = new ClientDataChannelStreamObserver(messageCount);
        StreamObserver<DataMessage> outgoingStreamObserver = client.getDataChannel(incomingStreamObserver);
        for (int i = 0; i < messageCount; i++) {
            DataMessage dataMessage = createMessage(i, message);
            outgoingStreamObserver.onNext(dataMessage);
        }
        outgoingStreamObserver.onCompleted();
        incomingStreamObserver.awaitCompletion(5, TimeUnit.MINUTES);
        return true;
    }

    private boolean sendBulkDataMessagesAsync(int messageCount, int bulkCount, String message, SimpleClient client) throws InterruptedException {
        int index = 0;
        ClientBulkDataChannelStreamObserver incomingStreamObserver = new ClientBulkDataChannelStreamObserver(messageCount*bulkCount);
        StreamObserver<DataMessages> outgoingStreamObserver = client.getBulkDataChannel(incomingStreamObserver);
        for (int i = 0; i < messageCount; i++) {
            DataMessages.Builder builder = DataMessages.newBuilder();
            for (int j = 0; j<bulkCount; j++) {
                DataMessage dataMessage = createMessage(index, message);
                builder.addMessages(dataMessage);
                index++;
            }
            outgoingStreamObserver.onNext(builder.build());
        }
        outgoingStreamObserver.onCompleted();
        incomingStreamObserver.awaitCompletion(5, TimeUnit.MINUTES);
        return true;
    }

    private static DataMessage createMessage(int index, String message) {
        return DataMessage.newBuilder()
                .setIndex(index)
                .setMessage(message)
                .build();
    }

}