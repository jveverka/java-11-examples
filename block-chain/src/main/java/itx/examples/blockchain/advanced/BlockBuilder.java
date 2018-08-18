package itx.examples.blockchain.advanced;

public class BlockBuilder {

    private String id;
    private String nonce;
    private String data;
    private String previousHash;

    public BlockBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public BlockBuilder setNonce(String nonce) {
        this.nonce = nonce;
        return this;
    }

    public BlockBuilder setData(String data) {
        this.data = data;
        return this;
    }

    public BlockBuilder setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
        return this;
    }

    public BlockBuilder from (Block block) {
        this.id = block.getId();
        this.nonce = block.getNonce();
        this.data = block.getData();
        this.previousHash = block.getPreviousHash();
        return this;
    }

    public Block build() {
        return new Block(id, nonce, data, previousHash, BlockChainUtils.createSHA256Hash(id, nonce, data, previousHash));
    }

}
