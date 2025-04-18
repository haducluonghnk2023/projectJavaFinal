package ra.edu.business.model.account;

public enum Status {
    ACTIVE, INACTIVE, BLOCKED;

    public static Status fromString(String value) {
        return Status.valueOf(value.toUpperCase());
    }

}
