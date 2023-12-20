package com.tejko.yamb.api.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping(value = { "/", "/{x:[\\w\\-]+}", "/{x:^(?!api$).*$}/**/{y:[\\w\\-]+}" })
    public String forward() { 
        return "/index.html";
    }

    @RequestMapping("/api/swagger")
	public void home(HttpServletResponse response) throws IOException {
		response.sendRedirect("/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config");
	}

} 