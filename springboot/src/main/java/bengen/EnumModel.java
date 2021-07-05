package bengen;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class EnumModel {
    private String packageName;
    private String enumName;
    private List<String> values;
}
