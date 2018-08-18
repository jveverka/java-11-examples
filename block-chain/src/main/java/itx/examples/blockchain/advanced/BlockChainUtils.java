package itx.examples.blockchain.advanced;

import itx.examples.blockchain.CommonUtils;

public final class BlockChainUtils {

    private BlockChainUtils() {
    }

    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvxywz";

    public static String getNextNonce(String lastNonce) {
        if (lastNonce == null) return String.valueOf(ALPHABET.charAt(0));
        int maxIndex = ALPHABET.length() - 1;
        StringBuilder nextNonce = new StringBuilder(lastNonce);
        int remainder = 0;
        for (int i=(lastNonce.length()-1); i>=0; i--) {
            int nextIndex = ALPHABET.indexOf(lastNonce.charAt(i)) + 1;
            if (nextIndex <= maxIndex) {
                nextNonce.setCharAt(i, ALPHABET.charAt(nextIndex));
                remainder = 0;
                break;
            } else {
                nextNonce.setCharAt(i, ALPHABET.charAt(0));
                remainder = 1;
            }
        }
        if (remainder != 0) {
            return ALPHABET.charAt(0) + nextNonce.toString();
        } else {
            return nextNonce.toString();
        }
    }

    public static String createSHA256Hash(Block block) {
        String blockData = block.getId() + block.getNonce() + block.getData() + block.getPreviousHash();
        return CommonUtils.createSHA256Hash(blockData);
    }

    public static String createSHA256Hash(String id, String nonce, String data, String previousHash) {
        String blockData = id + nonce + data + previousHash;
        return CommonUtils.createSHA256Hash(blockData);
    }

    public static boolean isValid(Block block, String hashPrefix) {
        return block.getHash().startsWith(hashPrefix);
    }

    public static Block mineGenesisBlock(String data, String hashPrefix) {
        String nextId = CommonUtils.GENESIS_BLOCK_ID;
        return mineForValidBlock(nextId, data, CommonUtils.GENESIS_BLOCK_PREVIOUS_HASH, hashPrefix);
    }

    public static Block mineNextBlock(Block lastBlock, String data, String hashPrefix) {
        String nextId = CommonUtils.getNextBlockId(lastBlock.getId());
        return mineForValidBlock(nextId, data, lastBlock.getHash(), hashPrefix);
    }

    private static Block mineForValidBlock(String id, String data, String previousHash, String hashPrefix) {
        String nonce = getNextNonce(null);
        Block nextBlock = new BlockBuilder()
                .setId(id)
                .setNonce(nonce)
                .setData(data)
                .setPreviousHash(previousHash)
                .build();
        while (true) {
            if (isValid(nextBlock, hashPrefix)) break;
            nonce = getNextNonce(nonce);
            nextBlock = new BlockBuilder()
                    .from(nextBlock)
                    .setNonce(nonce)
                    .build();
        }
        return nextBlock;
    }

    public static boolean verifyBlock(Block block, Block previousBlock, String hashPrefix) {
        return (verifyBlock(block, hashPrefix) && verifyBlock(previousBlock, hashPrefix) && block.getPreviousHash().equals(previousBlock.getHash()));
    }

    public static boolean verifyBlock(Block block, String hashPrefix) {
        String hash = createSHA256Hash(block);
        return (hash.equals(block.getHash()) && isValid(block, hashPrefix));
    }

    public static boolean verifyLedger(Ledger ledger) {
        for (int i=(ledger.size()-1); i>0; i--) {
            Block previousBlock = ledger.getBlockAt(i - 1);
            Block block = ledger.getBlockAt(i);
            if (!verifyBlock(block, previousBlock, ledger.getHashPrefix())) {
                return false;
            }
        }
        return true;
    }

}
