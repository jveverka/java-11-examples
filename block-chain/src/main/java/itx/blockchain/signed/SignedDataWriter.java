package itx.blockchain.signed;

import itx.blockchain.api.DataWriter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SignedDataWriter implements DataWriter<SignedData> {

    @Override
    public Class<SignedData> forClass() {
        return SignedData.class;
    }

    @Override
    public byte[] writeData(SignedData data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(baos);
        dataOutputStream.writeUTF(data.getAlgorithm());
        byte[] payload = data.getData();
        dataOutputStream.writeInt(payload.length);
        dataOutputStream.write(payload);
        byte[] signature = data.getSignature();
        dataOutputStream.writeInt(signature.length);
        dataOutputStream.write(signature);
        dataOutputStream.flush();
        baos.flush();
        byte[] result = baos.toByteArray();
        dataOutputStream.close();
        baos.close();
        return result;
    }

}
