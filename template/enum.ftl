package ${packageName};



public enum ${enumName} {

<#list values as enumValue>
    ${enumValue}("${enumValue}") <#if enumValue_has_next>, <#else >;</#if>
</#list>

    private String value;


    ${enumName}(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static ${enumName} fromValue(String text) {
        for (${enumName} b : ${enumName}.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + text + "'");
    }
}
