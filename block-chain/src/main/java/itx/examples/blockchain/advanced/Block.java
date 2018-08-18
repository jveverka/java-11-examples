package itx.examples.blockchain.advanced;

public class Block {

    private String id;
    private String nonce;
    private String data;
    private String previousHash;
    private String hash;

    public Block(String id, String nonce, String data, String previousHash, String hash) {
        this.id = id;
        this.nonce = nonce;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = hash;
    }

    public String getId() {
        return id;
    }

    public String getNonce() {
        return nonce;
    }

    public String getData() {
        return data;
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

}
