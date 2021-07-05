package rest.dto;



public enum StatusEnum {
    UNDERREVIEW("underReview"),

    PUBLISHED("published"),

    INACTIVE("inactive"),

    EDITING("editing"),

    REJECTED("rejected"),

    REMOVED("removed");

    private String value;

    StatusEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static StatusEnum fromValue(String text) {
        for (StatusEnum b : StatusEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + text + "'");
    }
}
