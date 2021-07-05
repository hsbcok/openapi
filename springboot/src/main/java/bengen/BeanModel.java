package bengen;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BeanModel {

    private String packageName;
    private String packagePath;
    private String className;
    private List<Attribute> attributeList;

}
