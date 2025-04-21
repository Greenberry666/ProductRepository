package com.example.multi.app.controller.error;

import com.example.multi.module.utils.Response;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalErrorController implements ErrorController {
    @RequestMapping("/error")
    public Response error() {
        return new Response(4004);
    }

}
