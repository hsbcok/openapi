package bengen.controller;

import org.springframework.web.bind.annotation.*;
import bengen.bean.*;
@RestController
@RequestMapping("/")
public class CoursesController {


    @PostMapping("/courses")
    @ResponseBody
    public String createCourse (@RequestBody CreateCourseRequest pCreateCourseRequest) {


        return "true";
    }

    @GetMapping("/courses/{courseId}")
    @ResponseBody
    public String getCourseDetail (@RequestParam("courseId") int courseId) {


        return String.valueOf(courseId);
    }
}