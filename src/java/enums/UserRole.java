package enums;

public enum UserRole {
    MANAGER("MANAGER"),
    MEMBER("MEMBER"),
    NONE("NONE");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserRole fromString(String role) {
        for (UserRole r : UserRole.values()) {
            if (r.value.equalsIgnoreCase(role)) {
                return r;
            }
        }
        return NONE; // Default to NONE if invalid value
    }
}
