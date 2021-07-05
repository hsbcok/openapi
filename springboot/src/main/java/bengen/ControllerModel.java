package bengen;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ControllerModel {
    private String controllerPackage;
    private String beanPackage;
    private String controllerName;
    private String requestMapping;
    private List<ControllerMethod> methodList;

}

