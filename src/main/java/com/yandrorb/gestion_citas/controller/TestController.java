package com.yandrorb.gestion_citas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/mensaje")
    public String mensaje(){
        return "Mensaje de test";
    }
}
