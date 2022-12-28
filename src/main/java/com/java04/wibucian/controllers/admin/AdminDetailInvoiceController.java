package com.java04.wibucian.controllers.admin;

import com.java04.wibucian.dtos.DetailInvoiceDTO;
import com.java04.wibucian.services.DetailInvoiceService;
import com.java04.wibucian.vos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

@Validated
@RestController
@RequestMapping("admin/detailInvoice")
public class AdminDetailInvoiceController {

    @Autowired
    private DetailInvoiceService detailInvoiceService;

    @PostMapping(value = "/statistical",consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Object> statistical (@Valid @RequestBody StatisticalVO vO){
        System.out.println(vO);
        Instant start = Instant.parse(vO.getTimeStart()+"T00:00:00Z");
        Instant end = Instant.parse(vO.getTimeEnd()+"T00:00:00Z");
        List<StatisticalResultVO> statisticalResultVOList = detailInvoiceService.getStatisticalResult(start, end);
        HashMap<String, Object> map = new HashMap<>();
        map.put("data", statisticalResultVOList);
        map.put("turnover", detailInvoiceService.getTurnover(statisticalResultVOList));
        map.put("profit", detailInvoiceService.getProfit(statisticalResultVOList));
        return ResponseEntity.ok().body(map);
    }
}