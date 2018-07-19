package itx.futures.demo.dto;

public class DataResult {

    private String input;
    private String result;

    public DataResult(String input, String result) {
        this.input = input;
        this.result = result;
    }

    public String getInput() {
        return input;
    }

    public String getResult() {
        return result;
    }

}
