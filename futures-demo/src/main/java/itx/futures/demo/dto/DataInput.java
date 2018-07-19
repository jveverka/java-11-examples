package itx.futures.demo.dto;

public class DataInput {

    private String input;
    private boolean expectedToSucceed;

    public DataInput(String input, boolean expectedToSucceed) {
        this.input = input;
        this.expectedToSucceed = expectedToSucceed;
    }

    public String getInput() {
        return input;
    }

    public boolean isExpectedToSucceed() {
        return expectedToSucceed;
    }

}
