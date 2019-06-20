package itx.examples.springboot.di.services;

public class SuperService implements PrintService, DataService {

    @Override
    public String getData() {
        return "SUPER";
    }

    @Override
    public void print(String message) {
        System.out.print("SUPER: " + message);
    }

}
