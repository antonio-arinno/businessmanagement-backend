package com.arinno.businessmanagement.controllers;


import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Provider;
import com.arinno.businessmanagement.services.IProviderService;
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
import java.util.Map;

@CrossOrigin(origins = {"https://businessmanagement-7b334.web.app", "http://localhost:4200", "*"})
@RestController
@RequestMapping("/api")
public class ProviderRestController {

    @Autowired
    private IProviderService providerService;

    @Autowired
    private IUtil util;

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/providers/page/{page}")
    public Page<Provider> getProviders(@PathVariable Integer page, Authentication authentication){
        Pageable pageable = PageRequest.of(page, 5);
        return providerService.findByCompany(pageable, util.getCompany(authentication));
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/providers/{id}")
    public ResponseEntity<?> getProvider(@PathVariable Long id, Authentication authentication){
        return getProviderOrErr(id, authentication);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @DeleteMapping("/providers/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication authentication){

        ResponseEntity<?> responseEntity = null;
        responseEntity = getProviderOrErr(id, authentication);
        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }
        Map<String, Object> response = new HashMap<>();
        try {
            providerService.delete(id);
        } catch (DataAccessException e) {
            response.put("error", "Error al eliminar el cliente de la base de datos");
            response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("title", "Provider eliminado");
        response.put("message", "Provider eliminado con éxito!");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PostMapping("/providers")
    public ResponseEntity<?> create(@Valid @RequestBody Provider provider, BindingResult result, Authentication authentication) {

        ResponseEntity<?> responseEntity = null;
        responseEntity = util.getErrRequestBody(result);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        Map<String, Object> response = new HashMap<>();
        Provider newProvider = null;
        provider.setCompany(util.getCompany(authentication));
        try {
            newProvider = providerService.save(provider);
        } catch(DataAccessException e) {
            response.put("error", "Error al realizar el insert en la base de datos");
            response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("title", "Nuevo producto");
        response.put("message", "El producto ha sido creado con éxito!");
        response.put("product", newProvider);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PutMapping("/providers/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Provider provider, BindingResult result, @PathVariable Long id, Authentication authentication){

        ResponseEntity<?> responseEntity = null;
        responseEntity = util.getErrRequestBody(result);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        responseEntity = this.getProviderOrErr(id, authentication);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        Map<String, Object> response = new HashMap<>();

        Provider currentProvider = (Provider) responseEntity.getBody();
        currentProvider.setCode(provider.getCode());
        currentProvider.setName(provider.getName());
        currentProvider.setCreateAt(provider.getCreateAt());
        currentProvider.setAddress(provider.getAddress());

        Provider updateProvider = null;
        try{
            updateProvider = providerService.save(currentProvider);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar el producto en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("title", "Provider actualizado");
        response.put("message", "El provider ha sido actualizado con éxito!");
        response.put("product", updateProvider);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

    }

    private ResponseEntity<?> getProviderOrErr(Long id, Authentication authentication) {

        Map<String, Object> response = new HashMap<>();
        Provider provider = null;
        Company company = util.getCompany(authentication);

        try{
            provider = providerService.findByIdAndCompany(id, company);
        } catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(provider == null){
            response.put("mensaje", "El provider ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Provider>(provider, HttpStatus.OK);

    }


















}
