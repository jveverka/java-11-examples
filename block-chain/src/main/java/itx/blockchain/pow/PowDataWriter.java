package itx.blockchain.pow;

import itx.blockchain.api.DataWriter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PowDataWriter implements DataWriter<PowData> {

    @Override
    public Class<PowData> forClass() {
        return PowData.class;
    }

    @Override
    public byte[] writeData(PowData data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(baos);
        byte[] dataPayload = data.getData();
        dataOutputStream.writeInt(dataPayload.length);
        dataOutputStream.write(dataPayload);
        byte[] nonce = data.getNonce();
        dataOutputStream.writeInt(nonce.length);
        dataOutputStream.write(nonce);
        dataOutputStream.flush();
        baos.flush();
        byte[] result = baos.toByteArray();
        dataOutputStream.close();
        baos.close();
        return result;
    }

}
