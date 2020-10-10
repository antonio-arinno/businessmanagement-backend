package com.arinno.businessmanagement.controllers;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Customer;
import com.arinno.businessmanagement.services.ICustomerService;
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
import java.util.stream.Collectors;

/**
 * Created by aarinopu on 10/12/2019.
 */

@CrossOrigin(origins = {"https://businessmanagement-7b334.web.app", "http://localhost:4200", "*"})
@RestController
@RequestMapping("/api")
public class CustomerRestController {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IUtil util;

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/customers")
    public List<Customer> getCustomers(Authentication authentication){
        return customerService.findByCompany(util.getCompany(authentication));
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/customers/page/{page}")
    public Page<Customer> getCustomers(@PathVariable Integer page, Authentication authentication){
        Pageable pageable = PageRequest.of(page, 5);
        return customerService.findByCompany(pageable, util.getCompany(authentication));
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/customers/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable Long id, Authentication authentication){
        return getCustomerOrErr(id, authentication);
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication authentication){

        ResponseEntity<?> responseEntity = null;
        responseEntity = getCustomerOrErr(id, authentication);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        Map<String, Object> response = new HashMap<>();

        try {
            customerService.delete(id);
        } catch (DataAccessException e) {
            response.put("error", "Error al eliminar el cliente de la base de datos");
            response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("title", "Cliente eliminado");
        response.put("message", "Cliente eliminado con éxito!");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PostMapping("/customers")
    public ResponseEntity<?> create(@Valid @RequestBody Customer customer, BindingResult result, Authentication authentication){

        ResponseEntity<?> responseEntity = null;
        responseEntity = util.getErrRequestBody(result); //getErrRequestBody(result);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        Map<String, Object> response = new HashMap<>();
        Customer  newCustomer = null;
        customer.setCompany(util.getCompany(authentication));
        try {
            newCustomer = customerService.save(customer);
        } catch(DataAccessException e) {
            response.put("error", "Error al realizar el insert en la base de datos");
            response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "El cliente ha sido creado con éxito!");
        response.put("cliente", newCustomer);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PutMapping("/customers/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Customer customer, BindingResult result, @PathVariable Long id, Authentication authentication){

        ResponseEntity<?> responseEntity = null;
        responseEntity = util.getErrRequestBody(result);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        responseEntity = this.getCustomerOrErr(id, authentication);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        Map<String, Object> response = new HashMap<>();

        Customer currentCustomer = (Customer) responseEntity.getBody();
        currentCustomer.setName(customer.getName());
        currentCustomer.setCode(customer.getCode());
        currentCustomer.setCreateAt(customer.getCreateAt());
        Customer updateCustomer = null;
        try {
            updateCustomer = customerService.save(currentCustomer);
        } catch (DataAccessException e) {
            response.put("error", "Error al actualizar el cliente en la base de datos");
            response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "El cliente ha sido actualizado con éxito!");
        response.put("cliente", updateCustomer);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    private ResponseEntity<?> getCustomerOrErr (Long id, Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        Customer customer = null;
        Company company = util.getCompany(authentication);

        try {
            customer = customerService.findByIdAndCompany(id, company);
        } catch(DataAccessException e) {
            response.put("error", "Error al realizar la consulta en la base de datos");
            response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(customer == null) {
            response.put("error", HttpStatus.NOT_FOUND);
            response.put("message", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Customer>(customer, HttpStatus.OK);
    }


}
