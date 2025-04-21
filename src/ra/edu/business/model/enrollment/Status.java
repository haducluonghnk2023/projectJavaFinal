package ra.edu.business.model.enrollment;

public enum Status {
    WAITING,DENIED,CANCEL,CONFIRM;
    public static Status fromString(String value) {
        return Status.valueOf(value.toUpperCase());
    }
}
