package itx.examples.springboot.security.springsecurity.jwt.services.dto;

public class Password {

    private final String password;

    public Password(String password) {
        this.password = password;
    }

    public boolean verify(String password) {
        return password.equals(password);
    }

    public static Password from(String password) {
        return new Password(password);
    }

}
