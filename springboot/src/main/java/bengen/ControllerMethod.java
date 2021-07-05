package bengen;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ControllerMethod {
    private String getOrPost;
    private String mappingPath;
    private String methodName;
    private String parameterType;
    private String parameterName;
}
