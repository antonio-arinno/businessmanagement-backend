package com.arinno.businessmanagement.controllers;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.services.ICompanyService;
import com.arinno.businessmanagement.services.IUserModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
import java.util.stream.Collectors;

/**
 * Created by aarinopu on 22/01/2020.
 */

@CrossOrigin(origins = {"https://businessmanagement-7b334.web.app", "http://localhost:4200", "*"})
@RestController
@RequestMapping("/api")
public class CompanyRestController {

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private IUserModelService userModelService;
/*
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/companies2/{id}")
    public Company getCompany2(@PathVariable Long id, Authentication authentication){
        return companyService.findById(id);
    }
*/

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/companies")
    public ResponseEntity<?> getCompany2(Authentication authentication){
        System.out.print("getCompany2");
        return getCompanyOrErr2(authentication);
    }


    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/companies/{id}")
    public ResponseEntity<?> getCompany(@PathVariable Long id, Authentication authentication){
        return getCompanyOrErr(id, authentication);
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PostMapping("/companies")
    public ResponseEntity<?> create(@Valid @RequestBody Company company, BindingResult result, Authentication authentication) {

        Map<String, Object> response = new HashMap<>();
        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        Company newCompany = null;
        try{
            newCompany = companyService.save(company);
        } catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El cliente ha sido creado con éxito!");
        response.put("cliente", newCompany);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PutMapping("/companies/{id}")
    public ResponseEntity<?> update2(@Valid @RequestBody Company company, BindingResult result, @PathVariable Long id, Authentication authentication){

/*
        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
*/



        ResponseEntity<?> responseEntity = null;
        responseEntity = this.getCompanyOrErr(id, authentication);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        Map<String, Object> response = new HashMap<>();

        Company currentCompany = (Company) responseEntity.getBody();

        currentCompany.setName(company.getName());



        Company updateCompany = null;
        try{
            updateCompany = companyService.save(currentCompany);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar el producto en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La compañia ha sido actualizado con éxito!");
        response.put("company", updateCompany);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PutMapping("/companies")
    public ResponseEntity<?> update(@Valid @RequestBody Company company, BindingResult result, Authentication authentication){

        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }




        ResponseEntity<?> responseEntity = null;
        responseEntity = this.getCompanyOrErr2(authentication);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

 //       Map<String, Object> response = new HashMap<>();

        Company currentCompany = (Company) responseEntity.getBody();

        currentCompany.setName(company.getName());
        currentCompany.setAddress(company.getAddress());

        Company updateCompany = null;
        try{
            updateCompany = companyService.save(currentCompany);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar el producto en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La compañia ha sido actualizado con éxito!");
        response.put("company", updateCompany);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

    }


    private ResponseEntity<?> getCompanyOrErr(Long id, Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        Long userId = userModelService.findByUsername(authentication.getName()).getCompany().getId();
        if( userId != id){
            response.put("mensaje", "La compañia ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        Company company = null;

        try{
            company = companyService.findById(id);
        } catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(company == null){
            response.put("mensaje", "La compañia ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Company>(company, HttpStatus.OK);

    }

    private ResponseEntity<?> getCompanyOrErr2(Authentication authentication){

        Map<String, Object> response = new HashMap<>();

        Long userId = userModelService.findByUsername(authentication.getName()).getCompany().getId();

        Company company = null;

        try{
            company = companyService.findById(userId);
        } catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(company == null){
            response.put("mensaje", "La compañia ID: ".concat(userId.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Company>(company, HttpStatus.OK);

    }



}
