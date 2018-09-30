package itx.blockchain.pow;

import itx.blockchain.api.DataReader;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class PowDataReader implements DataReader<PowData> {

    @Override
    public Class<PowData> forClass() {
        return PowData.class;
    }

    @Override
    public PowData readData(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dataInputStream = new DataInputStream(bais);
        int dataPayloadLength = dataInputStream.readInt();
        byte[] dataPayload = new byte[dataPayloadLength];
        dataInputStream.read(dataPayload);
        int nonceLength = dataInputStream.readInt();
        byte[] nonce = new byte[nonceLength];
        dataInputStream.read(nonce);
        dataInputStream.close();
        bais.close();
        return new PowDataImpl(dataPayload, nonce);
    }

}
