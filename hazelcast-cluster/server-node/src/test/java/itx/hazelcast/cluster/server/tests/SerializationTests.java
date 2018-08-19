package itx.hazelcast.cluster.server.tests;

import com.google.protobuf.InvalidProtocolBufferException;
import itx.hazelcast.cluster.dto.Address;
import itx.hazelcast.cluster.dto.InstanceInfo;
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

}
