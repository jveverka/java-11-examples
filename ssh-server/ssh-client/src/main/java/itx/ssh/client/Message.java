package itx.ssh.client;

public interface Message {

    byte[] getData();

    static Message from(byte[] data) {
        return new Message() {
            @Override
            public byte[] getData() {
                return data;
            }
        };
    }

}
