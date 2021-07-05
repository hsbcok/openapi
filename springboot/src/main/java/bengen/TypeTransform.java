package bengen;

/**
 * refer:
 *
 */
public class TypeTransform {
    public static String openAPIv3TypeToJava(String v3Type ){

        switch (v3Type){
            case "string": return "String";
            case "number": return "float";
            case "integer": return "int";
            default: return "String";
        }

    }
}
