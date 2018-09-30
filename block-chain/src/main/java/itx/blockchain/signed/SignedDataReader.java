package itx.blockchain.signed;

import itx.blockchain.api.DataReader;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class SignedDataReader implements DataReader<SignedData> {

    @Override
    public Class<SignedData> forClass() {
        return SignedData.class;
    }

    @Override
    public SignedData readData(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dataInputStream = new DataInputStream(bais);
        String algorithm = dataInputStream.readUTF();
        int payloadSize = dataInputStream.readInt();
        byte[] payload = new byte[payloadSize];
        dataInputStream.read(payload);
        int signatureSize = dataInputStream.readInt();
        byte[] signature = new byte[signatureSize];
        dataInputStream.read(signature);
        dataInputStream.close();
        bais.close();
        return new SignedDataImpl(algorithm, payload, signature);
    }

}
