package itx.examples.jwt;

public class JWToken {

    private final String token;

    public JWToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
