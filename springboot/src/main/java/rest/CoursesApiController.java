package rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-07-02T01:05:49.469+08:00[Asia/Shanghai]")

@RestController
@RequestMapping("/")
public class CoursesApiController implements CoursesApi {

    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public CoursesApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

}
