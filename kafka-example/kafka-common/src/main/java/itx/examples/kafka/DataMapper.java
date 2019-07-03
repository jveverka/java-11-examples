package itx.examples.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.utils.Bytes;

import java.io.IOException;

public class DataMapper {

    private final ObjectMapper objectMapper;

    public DataMapper() {
        this.objectMapper = new ObjectMapper();
    }

    public Bytes serialize(Object data) throws JsonProcessingException {
        return new Bytes(objectMapper.writeValueAsBytes(data));
    }

    public <T> T deserialize(Bytes data, Class<T> type) throws IOException {
        return objectMapper.readValue(data.get(), type);
    }

}
