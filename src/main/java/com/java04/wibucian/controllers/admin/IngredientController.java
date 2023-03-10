package com.java04.wibucian.controllers.admin;

import com.java04.wibucian.dtos.IngredientDTO;
import com.java04.wibucian.models.ImportGoods;
import com.java04.wibucian.models.Ingredient;
import com.java04.wibucian.services.IngredientService;
import com.java04.wibucian.vos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;

@Controller

@RequestMapping("admin/ingredient")
public class IngredientController {

    public static final int limit = 5;

    @Autowired
    private IngredientService ingredientService;

    //phan trang
    @GetMapping("/")
    public String Home(ModelMap modelMap)throws Exception {
        int page = 1;
        Pageable pageable = PageRequest.of(page - 1, limit);
        int totalPage = ingredientService.getTotalPage(limit);
        modelMap.addAttribute("page", page);
        modelMap.addAttribute("totalPage", totalPage);
        modelMap.addAttribute("xemdanhsach", ingredientService.findAll(pageable));
        //modelMap.addAttribute("xemdanhsach", ingredientService.findAll());
        return "admin/ingredient/index";
    }



    @GetMapping("/page/{page}")
    public String HomePage(ModelMap modelMap, @Valid @NotNull @PathVariable("page") int page)throws Exception {
        int totalPage = ingredientService.getTotalPage(limit);
        page = (page < 1) ? 1 : page;
        page = (page > totalPage) ? totalPage : page;
        Pageable pageable = PageRequest.of(page - 1, limit);
        modelMap.addAttribute("page", page);
        modelMap.addAttribute("totalPage", totalPage);
        modelMap.addAttribute("xemdanhsach", ingredientService.findAll(pageable));
        return "admin/ingredient/index";
    }

    //phan trang

    @GetMapping("/add")
   public String add(ModelMap modelMap)throws Exception {
       System.out.println("Hello");
      modelMap.addAttribute("xemdanhsach", ingredientService.findAll());
      return "admin/ingredient/index";
    }

    @GetMapping("/index")
    public String createEmployeePage(ModelMap modelMap) throws Exception {
        return "admin/ingredient/index";
    }
    @GetMapping("/create")
    public String createIngredientPage(ModelMap modelMap) throws Exception {
        modelMap.addAttribute("ingredientVO", new IngredientVO());
        return "admin/ingredient/create";
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
   public String createIngredient(ModelMap modelMap, @Valid IngredientVO ingredientVO, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return "admin/ingredient/create";
        }
       String productId = this.ingredientService.save(ingredientVO);
       System.out.println(ingredientVO);
        return "redirect:/admin/ingredient/";
  }

    @GetMapping("/edit/{id}")
    public String HomeEdit(ModelMap modelMap, @Valid @NotNull @PathVariable("id") String id)throws Exception {
        Ingredient ingredient = ingredientService.findById(id);
        modelMap.addAttribute("xemdanhsach", ingredient);
        modelMap.addAttribute("ingredientUpdateVO", new IngredientUpdateVO());
        return "admin/ingredient/edit";
    }

    @RequestMapping(value = "/edit",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String editIngredient(ModelMap modelMap,
                                @Valid @ModelAttribute(name = "ingredientUpdateVO") IngredientUpdateVO ingredientUpdateVO,
                                BindingResult bindingResult,
                                @RequestBody MultiValueMap<String, String> formData) throws Exception {
        if (bindingResult.hasErrors()) {
            String idIngredient =  formData.get("ingredientId").get(0);
            Ingredient ingredient = ingredientService.findById(idIngredient);
            modelMap.addAttribute("xemdanhsach", ingredient);
            return "admin/ingredient/edit";
        }
        String idIngredient =  formData.get("ingredientId").get(0);
        this.ingredientService.update(idIngredient, ingredientUpdateVO);
        return "redirect:/admin/ingredient/";
    }
    @PostMapping
    public String save(@Valid @RequestBody IngredientVO vO) {
        return ingredientService.save(vO).toString();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
        try {
            ingredientService.delete(id);
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

    @PutMapping("/{id}")
    public void update(@Valid @NotNull @PathVariable("id") String id,
                       @Valid @RequestBody IngredientUpdateVO vO) {
        ingredientService.update(id, vO);
    }

    @GetMapping("/{id}")
    public IngredientDTO getById(@Valid @NotNull @PathVariable("id") String id) {
        return ingredientService.getById(id);
    }

    @GetMapping
    public Page<IngredientDTO> query(@Valid IngredientQueryVO vO) {
        return ingredientService.query(vO);
    }
}