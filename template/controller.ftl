package ${controllerPackage};

import org.springframework.web.bind.annotation.*;
import ${beanPackage}.*;
@RestController
@RequestMapping("${requestMapping}")
public class ${controllerName} {

<#list methodList as method>

    @${method.getOrPost}("${method.mappingPath}")
    public String ${method.methodName} (@RequestParam("${method.parameterName}") ${method.parameterType} ${method.parameterName}) {


        return "true";
    }
</#list>
}