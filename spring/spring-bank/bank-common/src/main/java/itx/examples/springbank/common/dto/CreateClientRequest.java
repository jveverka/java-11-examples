package itx.examples.springbank.common.dto;

public class CreateClientRequest {

    private final String firstName;
    private final String lastName;

    public CreateClientRequest(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}
