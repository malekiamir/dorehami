package ir.ac.kntu.patogh.ApiDataTypes;

public class TypeAuthentication {
    String phoneNumber;
    String loginToken;

    public TypeAuthentication(String phoneNumber, String loginToken) {
        this.phoneNumber = phoneNumber;
        this.loginToken = loginToken;
    }
}
