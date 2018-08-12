package itx.examples.sshd.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import itx.examples.sshd.commands.dto.ErrorData;
import itx.examples.sshd.commands.dto.GetRequest;
import itx.examples.sshd.commands.dto.GetResponse;
import itx.examples.sshd.commands.dto.SetRequest;
import itx.examples.sshd.commands.dto.SetResponse;
import itx.examples.sshd.commands.dto.StartStreamRequest;
import itx.examples.sshd.commands.dto.StreamResponse;
import itx.examples.sshd.sessions.SshClientSessionListenerImpl;
import itx.ssh.server.commands.CommandProcessor;
import itx.ssh.server.commands.CommandResult;
import itx.ssh.server.commands.OutputWriter;
import itx.ssh.server.commands.subsystem.SshClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SshClientCommandProcessor implements CommandProcessor, AutoCloseable {

    final private static Logger LOG = LoggerFactory.getLogger(SshClientCommandProcessor.class);

    private final ObjectMapper mapper;

    private String state;
    private long sessionId;
    private OutputWriter outputWriter;
    private SshClientSessionListenerImpl sshClientSessionListener;
    private ExecutorService executorService;

    public SshClientCommandProcessor(SshClientSessionListenerImpl sshClientSessionListener) {
        this.mapper = new ObjectMapper();
        this.state = "";
        this.sshClientSessionListener = sshClientSessionListener;
        this.executorService = Executors.newFixedThreadPool(4);
    }

    @Override
    public void onSessionStart(long sessionId, OutputWriter outputWriter) {
        this.sessionId = sessionId;
        this.outputWriter = outputWriter;
    }

    @Override
    public CommandResult processCommand(byte[] command) throws IOException {
        LOG.info("processing command: {} ", new String(command, Charset.forName("UTF-8")));
        JsonNode jsonNode = mapper.readTree(command);
        String type = jsonNode.get("type").asText();
        byte[] response = new byte[0];
        if (type.equals(GetRequest.class.getName())) {
            GetRequest getRequest = mapper.readValue(command, GetRequest.class);
            GetResponse getResponse = new GetResponse(getRequest.getId(), state);
            response = mapper.writeValueAsBytes(getResponse);
        } else if (type.equals(SetRequest.class.getName())) {
            SetRequest setRequest = mapper.readValue(command, SetRequest.class);
            state = setRequest.getData();
            SetResponse setResponse = new SetResponse(setRequest.getId(), state);
            response = mapper.writeValueAsBytes(setResponse);
        } else if (type.equals(StartStreamRequest.class.getName())) {
            StartStreamRequest startStreamRequest = mapper.readValue(command, StartStreamRequest.class);
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        LOG.info("started push-to-client job");
                        SshClientSession session = sshClientSessionListener.getSession(sessionId);
                        for (int i = 0; i < startStreamRequest.getStreamCount(); i++) {
                            StreamResponse streamResponse = new StreamResponse(startStreamRequest.getId(), i,
                                    startStreamRequest.getMessage());
                            byte[] response = mapper.writeValueAsBytes(streamResponse);
                            session.sendMessage(response);
                        }
                    } catch (Exception e) {
                        LOG.error("Error: ", e);
                    }
                }
            });
            return CommandResult.ok();
        } else {
            ErrorData errorData = new ErrorData("unsupported command");
            response = mapper.writeValueAsBytes(errorData);
        }
        outputWriter.writeMessage(response);
        return CommandResult.ok();
    }

    @Override
    public void close() throws Exception {
        if (executorService != null) {
            executorService.shutdown();
        }
    }

}
