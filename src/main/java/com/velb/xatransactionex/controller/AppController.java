package com.velb.xatransactionex.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.velb.xatransactionex.service.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class AppController {

    private final MyService myService;


    @GetMapping(value = "/orders", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    public String getOrdersByConsumer() {
        myService.saveEntity1();
        myService.saveEntity2();
        return "Hello";
    }


}
