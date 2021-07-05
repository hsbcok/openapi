package bengen.bean;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import bengen.bean.*;


@Getter
@Setter
public class CourseDto {
    private int id;
    private Enumstatus status;
    private float price;
    private String title;
    private String cover;
    private int presenterRef;
    private String shortDesc;
    private int albumRef;
    private int homepageRef;
    private String poster;
    private String createdTime;

}