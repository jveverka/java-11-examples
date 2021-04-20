package itx.hazelcast.cluster.server.tests;

import com.google.protobuf.InvalidProtocolBufferException;
import itx.hazelcast.cluster.dto.Address;
import itx.hazelcast.cluster.dto.DataMessage;
import itx.hazelcast.cluster.dto.InstanceInfo;
import itx.hazelcast.cluster.dto.MessageWrapper;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SerializationTests {

    @Test
    public void testInstanceInfoSerializationAndDeserialization() throws InvalidProtocolBufferException {
        Address address = Address.newBuilder()
                .setHostName("127.0.0.1")
                .setPort(22222)
                .build();
        InstanceInfo instanceInfo = InstanceInfo.newBuilder()
                .setAddress(address)
                .setWebServerPort(8080)
                .build();

        byte[] bytes = instanceInfo.toByteArray();
        InstanceInfo deserializeInstanceInfo = InstanceInfo.parseFrom(bytes);
        Assert.assertNotNull(deserializeInstanceInfo);
        Assert.assertEquals(deserializeInstanceInfo.getAddress().getHostName(), instanceInfo.getAddress().getHostName());
        Assert.assertTrue(deserializeInstanceInfo.getAddress().getPort() == instanceInfo.getAddress().getPort());
        Assert.assertTrue(deserializeInstanceInfo.getWebServerPort() == instanceInfo.getWebServerPort());
    }

    @Test
    public void messageWrapperSerializationAndDeserializationTest() throws InvalidProtocolBufferException {
        DataMessage dataMessage = DataMessage.newBuilder()
                .setTopicId("topic1")
                .setMessage("hello")
                .build();
        MessageWrapper messageWrapper = MessageWrapper.newBuilder()
                .setDataMessage(dataMessage)
                .build();

        byte[] bytes = messageWrapper.toByteArray();

        MessageWrapper deserializedMessageWrapper = MessageWrapper.parseFrom(bytes);
        Assert.assertNotNull(deserializedMessageWrapper);
        DataMessage deserializedDataMessage = deserializedMessageWrapper.getDataMessage();
        Assert.assertNotNull(deserializedDataMessage);
        Assert.assertEquals(dataMessage.getTopicId(), deserializedDataMessage.getTopicId());
        Assert.assertEquals(dataMessage.getMessage(), deserializedDataMessage.getMessage());
    }

}
