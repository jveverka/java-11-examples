package itx.examples.jdbc.dto;

public class UserData {

    private final String id;
    private final String email;
    private final String password;

    public UserData(String id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
