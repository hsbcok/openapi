package ${packageName};

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import ${packageName}.*;


@Getter
@Setter
public class ${className} {
<#list attributeList as attr>
    private ${attr.type} ${attr.name};
</#list>

}