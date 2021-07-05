package bengen.bean;



public enum Enumstatus {

    underReview("underReview") , 
    published("published") , 
    inactive("inactive") , 
    editing("editing") , 
    rejected("rejected") , 
    removed("removed") ;

    private String value;


    Enumstatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static Enumstatus fromValue(String text) {
        for (Enumstatus b : Enumstatus.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + text + "'");
    }
}
