package my.edu.um.umpoint.modules.sys.controller;

import my.edu.um.umpoint.common.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/")
    public Result<String> index(){
            String tips = "Please startup umpoint-ui to access";
        return new Result<String>().ok(tips);
    }
}
