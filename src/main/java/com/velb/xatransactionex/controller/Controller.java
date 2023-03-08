package com.velb.xatransactionex.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.velb.xatransactionex.service.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class Controller {

    private final Service service;


    @PostMapping(value = "/entities/db1", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    public void saveEntity1() {
        service.saveEntity1();
    }


    @PostMapping(value = "/entities/db2", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    public void saveEntity2() {
        service.saveEntity2();
    }


    @PostMapping(value = "/entities", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    public void saveDoubleEntities() {
        service.saveDoubleEntity();
    }

    @PatchMapping(value = "/entities", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    public void transferFunds() {
        service.transferFunds();
    }





}
