package ir.ac.kntu.patogh.ApiDataTypes;

public class TypeEditUserDetails {
    String phoneNumber;
    String firstName;
    String lastName;
    String email;

    public TypeEditUserDetails(String phoneNumber, String firstName, String lastName, String email) {
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
