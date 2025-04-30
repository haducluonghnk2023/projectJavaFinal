package ra.edu.business.model.account;

public enum Status {
    ACTIVE, INACTIVE, BLOCK;

    public static Status fromString(String value) {
        return Status.valueOf(value.toUpperCase());
    }

}
