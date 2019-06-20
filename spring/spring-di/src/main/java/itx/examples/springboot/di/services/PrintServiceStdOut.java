package itx.examples.springboot.di.services;

public class PrintServiceStdOut implements PrintService {

    @Override
    public void print(String message) {
        System.out.print("OUT: " + message);
    }

}
