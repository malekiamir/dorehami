package ir.ac.kntu.patogh.ApiDataTypes;

import androidx.annotation.NonNull;

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

    @NonNull
    @Override
    public String toString() {
        return phoneNumber + " " + firstName + " " + lastName + " " + email;
    }
}
