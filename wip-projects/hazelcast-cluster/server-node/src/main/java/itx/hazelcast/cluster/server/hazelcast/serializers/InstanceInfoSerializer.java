package itx.hazelcast.cluster.server.hazelcast.serializers;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;
import itx.hazelcast.cluster.dto.InstanceInfo;

import java.io.IOException;

public class InstanceInfoSerializer implements StreamSerializer<InstanceInfo> {

    @Override
    public void write(ObjectDataOutput out, InstanceInfo object) throws IOException {
        out.writeByteArray(object.toByteArray());
    }

    @Override
    public InstanceInfo read(ObjectDataInput in) throws IOException {
        return InstanceInfo.parseFrom(in.readByteArray());
    }

    @Override
    public int getTypeId() {
        return 1;
    }

    @Override
    public void destroy() {
    }

}
