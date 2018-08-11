package itx.examples.sshd.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import itx.examples.sshd.commands.dto.ErrorData;
import itx.examples.sshd.commands.dto.GetRequest;
import itx.examples.sshd.commands.dto.GetResponse;
import itx.examples.sshd.commands.dto.SetRequest;
import itx.examples.sshd.commands.dto.SetResponse;
import itx.ssh.server.commands.CommandProcessor;
import itx.ssh.server.commands.CommandResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

public class JsonCommandProcessor implements CommandProcessor {

    final private static Logger LOG = LoggerFactory.getLogger(JsonCommandProcessor.class);
    final private static int ENTER = 13;

    private final ObjectMapper mapper;
    private String data;

    public JsonCommandProcessor() {
        this.mapper = new ObjectMapper();
        this.data = "";
    }

    @Override
    public CommandResult processCommand(String command, OutputStream stdout, OutputStream stderr) throws IOException {
        LOG.info("processing command: {} ", command);
        JsonNode jsonNode = mapper.readTree(command);
        String type = jsonNode.get("type").asText();
        String response = "{}";
        if (type.equals(GetRequest.class.getName())) {
            GetRequest getRequest = mapper.readValue(command, GetRequest.class);
            GetResponse getResponse = new GetResponse(getRequest.getId(), data);
            response = mapper.writeValueAsString(getResponse);
        } else if (type.equals(SetRequest.class.getName())) {
            SetRequest setRequest = mapper.readValue(command, SetRequest.class);
            data = setRequest.getData();
            SetResponse setResponse = new SetResponse(setRequest.getId(), data);
            response = mapper.writeValueAsString(setResponse);
        } else {
            ErrorData errorData = new ErrorData("unsupported command");
            response = mapper.writeValueAsString(errorData);
        }
        stdout.write(response.getBytes());
        stdout.write(ENTER);
        return CommandResult.ok();
    }

}
