package itx.examples.springboot.di.services;

public class PrintServiceStdErr implements PrintService {

    @Override
    public void print(String message) {
        System.err.print("ERR: " + message);
    }

}
