package com.induction.sales.controller;

import com.induction.sales.service.SalesforceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SalesForceController {

    @Autowired
    private SalesforceService salesforceService;

    @GetMapping("/getSalesforceData")
    public String getSalesforceData(@RequestParam("userName") String userName, @RequestParam("password") String password) {
        return salesforceService.getSalesforceData(userName, password);
    }
}
