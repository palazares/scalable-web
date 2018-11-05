package com.waes.palazares.scalableweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home endpoint redirects to the Swagger API documentation
 */
@Controller
public class HomeController {

    /**
     * Endpoint redirects to the Swagger API documentation
     *
     * @return Redirect command to the Swagger
     */
    @RequestMapping("/")
    public String home() {
        return "redirect:swagger-ui.html";
    }
}
