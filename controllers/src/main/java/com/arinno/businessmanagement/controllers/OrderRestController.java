package com.arinno.businessmanagement.controllers;

import com.arinno.businessmanagement.model.*;
import com.arinno.businessmanagement.services.ICustomerService;
import com.arinno.businessmanagement.services.IOrderService;
import com.arinno.businessmanagement.services.IProductService;
import com.arinno.businessmanagement.util.IGeneratePdfReport;
import com.arinno.businessmanagement.util.IUtil;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@CrossOrigin(origins = {"https://businessmanagement-7b334.web.app", "http://localhost:4200", "*"})
@RestController
@RequestMapping("/api")
public class OrderRestController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IUtil util;

    @Autowired
    private IProductService productService;

    @Autowired
    private IGeneratePdfReport generatePdfReport;

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/orders/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id, Authentication authentication){
        return getOrderOrErr(id, authentication);
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/orders")
    public List<Order> getOrders(Authentication authentication){
        return orderService.findByCompany(util.getCompany(authentication));
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/orders/page/{page}")
    public Page<Order> getOrders(@PathVariable Integer page, Authentication authentication){
        Pageable pageable = PageRequest.of(page, 5);
        return orderService.findByCompany(pageable, util.getCompany(authentication));
    }

/*
   @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/customers/page/{page}")
    public Page<Customer> getCustomers(@PathVariable Integer page, Authentication authentication){
        Pageable pageable = PageRequest.of(page, 5);
        return customerService.findByCompany(pageable, util.getCompany(authentication));
    }

 */


    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/orders/load-product/{term}")
    public List<Product> loadProduct(@PathVariable String term, Authentication authentication){
        return productService.findByDescriptionContainingIgnoreCaseAndCompany(term, util.getCompany(authentication));
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/orders/load-customer/{term}")
    public List<Customer> loadCustomer(@PathVariable String term, Authentication authentication){
        return customerService.findByNameContainingIgnoreCaseAndCompany(term, util.getCompany(authentication));
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/orders/load-customer-code/{term}")
    public List<Customer> loadCustomerCode(@PathVariable String term, Authentication authentication){

        return customerService.findByCodeStartingWithIgnoreCaseAndCompany(term, util.getCompany(authentication));
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Valid @RequestBody Order order, BindingResult result, Authentication authentication){

        ResponseEntity<?> responseEntity = null;
        responseEntity = getErrRequestBody(result);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        if (order.getCustomer().getId()==null){
            Map<String, Object> response = new HashMap<>();
            response.put("error", "El Cliente no puede ser nulo");
            response.put("message", "El cliente no puede ser nulo");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (order.getItems().size()==0){
            Map<String, Object> response = new HashMap<>();
            response.put("error", "La factura no tiene lineas");
            response.put("message", "La factura no tiene lineas");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> response = new HashMap<>();
        Order  newOrder = null;
        Company company = util.getCompany(authentication);
        order.setCompany(company);

        try {
            newOrder = orderService.save(order);
        } catch(DataAccessException e) {
            response.put("error", "Error al realizar el insert en la base de datos");
            response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("title", "Nueva Orden");
        response.put("message", "La orden ha sido creada con éxito!");
        response.put("order", newOrder);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PutMapping("/orders/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Order order, BindingResult result, @PathVariable Long id, Authentication authentication){

        ResponseEntity<?> responseEntity = null;
        responseEntity = getErrRequestBody(result);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        responseEntity = this.getOrderOrErr(id, authentication);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        if (order.getCustomer().getId()==null){
            Map<String, Object> response = new HashMap<>();
            response.put("error", "El Cliente no puede ser nulo");
            response.put("message", "El cliente no puede ser nulo");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (order.getItems().size()==0){
            Map<String, Object> response = new HashMap<>();
            response.put("error", "La factura no tiene lineas");
            response.put("message", "La factura no tiene lineas");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> response = new HashMap<>();

        Order currentOrder = (Order) responseEntity.getBody();
        currentOrder.setCustomer(order.getCustomer());
        currentOrder.setCreateAt(order.getCreateAt());
        currentOrder.setObservation(order.getObservation());
        currentOrder.setItems(order.getItems());

        Order updateOrder = null;
        try {
            updateOrder = orderService.update(currentOrder);
        } catch (DataAccessException e) {
            response.put("error", "Error al actualizar la order en la base de datos");
            response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "la orden ha sido actualizado con éxito!");
        response.put("orden", updateOrder);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication authentication){

        ResponseEntity<?> responseEntity = null;
        responseEntity = getOrderOrErr(id, authentication);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        Map<String, Object> response = new HashMap<>();

        try {
            orderService.deleteById(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al eliminar la orden de la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("title", "Orden eliminado");
        response.put("message", "Orden eliminada con éxito!");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/orders/pdf/{id}")
    public ResponseEntity<InputStreamResource> PDF (@PathVariable Long id,  Authentication authentication) throws DocumentException {

        ResponseEntity<?> responseEntity = null;
        responseEntity = getOrderOrErr(id, authentication);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return (ResponseEntity<InputStreamResource>) ResponseEntity.badRequest();
        }

        Order order = (Order) responseEntity.getBody();
        ByteArrayInputStream bais = null;
        bais = generatePdfReport.generateOrder(order);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=order.pdf");

        return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bais));

    }

    private ResponseEntity<?> getOrderOrErr(Long id, Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        Order order = null;
        Company company = util.getCompany(authentication);

        try {
            order = orderService.findByIdAndCompany(id, company);
        } catch(DataAccessException e) {
            response.put("error", "Error al realizar la consulta en la base de datos");
            response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(order == null) {
            response.put("error", HttpStatus.NOT_FOUND);
            response.put("message", "La Orden ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Order>(order, HttpStatus.OK);

    }

    private ResponseEntity<?> getErrRequestBody(BindingResult result) {
        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .filter(err -> !"company".equals(err.getField()) && !"number".equals(err.getField()) )
                    .map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
                    .collect(Collectors.toList());
            if(errors.size() != 0) {
                response.put("errors", errors);
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        }
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

    }


}
