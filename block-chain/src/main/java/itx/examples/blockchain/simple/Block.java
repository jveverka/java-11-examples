package itx.examples.blockchain.simple;

public class Block {

    private String id;
    private String data;
    private String previousHash;
    private String hash;

    public Block(String id, String data, String previousHash, String hash) {
        this.id = id;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = hash;
    }

    public String getId() {
        return id;
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
