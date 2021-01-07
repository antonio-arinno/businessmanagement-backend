package com.arinno.businessmanagement.controllers;


import com.arinno.businessmanagement.model.*;
import com.arinno.businessmanagement.services.IInputDirectStoreService;
import com.arinno.businessmanagement.services.IProductLotService;
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

    @Autowired
    private IProductLotService productLotService;

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

        Company company = util.getCompany(authentication);
        Map<String, Object> response = new HashMap<>();
        InputDirectStore  newInputDirectStore = null;
        inputDirectStore.setCompany(company);

        try {
            newInputDirectStore = inputDirectStoreService.save(inputDirectStore);
        } catch(DataAccessException e) {
            response.put("error", "Error al realizar el insert en la base de datos");
            response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ProductLot productLot = null;
        productLot = productLotService.findByProductAndLotAndCompany
                (inputDirectStore.getProduct(), inputDirectStore.getLot(), company);
        response.put("title", "Nueva entrada en almacen");
        if (productLot==null){
            productLot = new ProductLot();
            productLot.setProduct(inputDirectStore.getProduct());
            productLot.setLot(inputDirectStore.getLot());
            productLot.setStock(inputDirectStore.getQuantity());
            productLot.setCompany(company);
            productLotService.save(productLot);
            response.put("message", getTestNegative(inputDirectStore.getQuantity()));
        }else {
            Integer quantity = productLot.getStock() + inputDirectStore.getQuantity();
            if(quantity==0){
                productLotService.deleteById(productLot.getId());
                response.put("message", "Almacen actualizado");
            }else{
                productLot.setStock(quantity);
                productLotService.save(productLot);
                response.put("message", getTestNegative(quantity));
            }
        }

        response.put("inputDirectStore", newInputDirectStore);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    private String getTestNegative(Integer quantity) {
        if(quantity<0) {
            return "Almacen actualizado, ADVERTENCIA tiene un valor negativo!!!";
        }
        return "Almacen actualizado";
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/inputs-directs-store/load-product/{term}")
    public List<Product> loadProduct(@PathVariable String term, Authentication authentication){
        return productService.findByDescriptionContainingIgnoreCaseAndCompany(term, util.getCompany(authentication));
    }
}
