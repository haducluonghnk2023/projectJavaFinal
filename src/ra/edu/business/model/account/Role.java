package ra.edu.business.model.account;

public enum Role {
    ADMIN,STUDENT;

    public static Role fromString(String value) {
        return Role.valueOf(value.toUpperCase());
    }
}
