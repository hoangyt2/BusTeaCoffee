package com.java04.wibucian.controllers.staff;

import com.java04.wibucian.models.GroupTableNoMapPing;
import com.java04.wibucian.models.OrdercfNoMapPing;
import com.java04.wibucian.models.ProductNoMapPing;
import com.java04.wibucian.services.GroupTableService;
import com.java04.wibucian.services.OrdercfService;
import com.java04.wibucian.services.ProductService;
import com.java04.wibucian.vos.OrdercfUpdateVO;
import com.java04.wibucian.vos.OrdercfVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Validated
@Controller
@RequestMapping("staff/ordercf")
public class OrdercfController {
    @Autowired
    private ProductService productService;

    @Autowired
    private GroupTableService groupTableService;

    @Autowired
    private OrdercfService ordercfService;



    @RequestMapping( method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        modelMap.addAttribute("groupTables", groupTableService.findAll());
        modelMap.addAttribute("products", productService.listAll());
        return "staff/order/index";
    }
    @PostMapping( produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<Object> save(@RequestBody OrdercfVO ordercfVO) {
        return ResponseEntity.ok().body(ordercfService.save(ordercfVO));
    }

    @PutMapping(value = "/{id}", produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<Object> update(@PathVariable("id") String id, @RequestBody OrdercfUpdateVO vo) {
        return ResponseEntity.ok().body(ordercfService.update(id, vo));
    }

    @RequestMapping(value = {"/store","/store/"}, method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<Object> store(@RequestBody List<OrdercfVO> ordercfList) {
        try {
            for(OrdercfVO ordercfVO: ordercfList){
                if (ordercfVO.getStatus() != 2){
                    ordercfService.save(ordercfVO);
                }
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("check", true);
            map.put("value", "test");
            return ResponseEntity.ok().body(map);
        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String, Object> map = new HashMap<>();
            map.put("check", false);
            map.put("value", "test");
            return ResponseEntity.ok().body(map);
        }
    }
    @RequestMapping(value = {"/store-final/{id}"}, method = RequestMethod.PUT,
            produces = "application/json; charset=utf-8")
    public ResponseEntity<Object> storeFinal(@PathVariable("id") String id, @RequestBody OrdercfUpdateVO vo) {
        return ResponseEntity.ok().body(ordercfService.update_final(id, vo));
    }

    @RequestMapping(value = "/find-by-group-table/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<Object> findByGroupTableId(@PathVariable("id") String id) {
        try {
            List<OrdercfNoMapPing> ordercfList = ordercfService.findByGroupTableId(id);
            return ResponseEntity.ok().body(ordercfList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(null);
        }
    }
    //find product by findByProductId
    @RequestMapping(value = "/find-product/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<Object> findByProductId(@PathVariable("id") String id) {
        try {
            ProductNoMapPing product = ordercfService.findByProductId(id);
            return ResponseEntity.ok().body(product);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(null);
        }
    }
    @RequestMapping(value = {"/find-all/","/find-all"} , method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<Object> findAll() {
        try {
            List<OrdercfNoMapPing> ordercfList = ordercfService.findAllNoMapp();
            return ResponseEntity.ok().body(ordercfList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(null);
        }
    }
    //find Ordercf by findByGroupTableId
    @RequestMapping(value = "/find-group-table/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<Object> findGroupTableId(@PathVariable("id") String id) {
        try {
            GroupTableNoMapPing groupTableNoMapPing = ordercfService.findGroupTableId(id);
            return ResponseEntity.ok().body(groupTableNoMapPing);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(null);
        }
    }
    ///delete Ordercf by id
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
        try {
            ordercfService.delete(id);
            HashMap<String, Object> map = new HashMap<>();
            map.put("check", true);
            map.put("value", "test");
            return ResponseEntity.ok().body(map);
        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String, Object> map = new HashMap<>();
            map.put("check", false);
            map.put("value", "test");
            return ResponseEntity.ok().body(map);
        }
    }

}