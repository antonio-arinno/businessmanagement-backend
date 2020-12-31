package com.arinno.businessmanagement.controllers;


import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.InputDirectStore;
import com.arinno.businessmanagement.model.Product;
import com.arinno.businessmanagement.services.IInputDirectStoreService;
import com.arinno.businessmanagement.services.IProductService;
import com.arinno.businessmanagement.util.IUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"https://businessmanagement-7b334.web.app", "http://localhost:4200", "*"})
@RestController
@RequestMapping("/api")
public class InputDirectStoreController {

    @Autowired
    private IInputDirectStoreService inputDirectStoreService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IUtil util;

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/inputs-directs-store/page/{page}")
    public Page<InputDirectStore> getInputsDirectsStore(@PathVariable Integer page, Authentication authentication){
        Pageable pageable = PageRequest.of(page, 5);
        return inputDirectStoreService.findByCompany(pageable, util.getCompany(authentication));
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PostMapping("/inputs-directs-store")
    public ResponseEntity<?> create(@Valid @RequestBody InputDirectStore inputDirectStore, BindingResult result, Authentication authentication) {

        ResponseEntity<?> responseEntity = null;
        responseEntity = util.getErrRequestBody(result);

        if(responseEntity.getStatusCode()!= HttpStatus.OK){
            return responseEntity;
        }

        Map<String, Object> response = new HashMap<>();
        InputDirectStore  newInputDirectStore = null;
        inputDirectStore.setCompany(util.getCompany(authentication));

        try {
            newInputDirectStore = inputDirectStoreService.save(inputDirectStore);
        } catch(DataAccessException e) {
            response.put("error", "Error al realizar el insert en la base de datos");
            response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("title", "Nueva entrada en almacen");
        response.put("message", "Nueva entrada en almacen");
        response.put("inputDirectStore", newInputDirectStore);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/inputs-directs-store/load-product/{term}")
    public List<Product> loadProduct(@PathVariable String term, Authentication authentication){
        return productService.findByDescriptionContainingIgnoreCaseAndCompany(term, util.getCompany(authentication));
    }
}
