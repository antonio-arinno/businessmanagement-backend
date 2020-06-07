package com.arinno.businessmanagement.controllers;

import com.arinno.businessmanagement.model.*;
import com.arinno.businessmanagement.services.ICustomerService;
import com.arinno.businessmanagement.services.IInvoiceService;
import com.arinno.businessmanagement.services.IProductService;
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
 * Created by aarinopu on 08/01/2020.
 */

@CrossOrigin(origins = {"https://businessmanagement-7b334.web.app", "http://localhost:4200", "*"})
@RestController
@RequestMapping("/api")
public class InvoiceRestController {

    @Autowired
    private IInvoiceService invoiceService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IUserModelService userModelService;

    @Autowired
    private IProductService productService;

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/invoices/{id}")
    public ResponseEntity<?> getInvoice(@PathVariable Long id, Authentication authentication){
        return getInvoiceOrErr(id, authentication);
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/invoices")
    public List<Invoice> getInvoices(Authentication authentication){
        return invoiceService.findByCompany(userModelService.findByUsername(authentication.getName()).getCompany());
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @DeleteMapping("/invoices/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication authentication){

        ResponseEntity<?> responseEntity = null;
        responseEntity = getInvoiceOrErr(id, authentication);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        Map<String, Object> response = new HashMap<>();

        try {
            invoiceService.deleteById(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al eliminar el cliente de la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "Factura eliminado con éxito!");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PostMapping("/invoices")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Valid @RequestBody Invoice invoice, BindingResult result, Authentication authentication){

        ResponseEntity<?> responseEntity = null;
        responseEntity = getErrRequestBody(result);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        if (invoice.getCustomer().getId()==null){
            Map<String, Object> response = new HashMap<>();
            response.put("error", "El Cliente no puede ser nulo");
            response.put("message", "El cliente no puede ser nulo");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (invoice.getItems().size()==0){
            Map<String, Object> response = new HashMap<>();
            response.put("error", "La factura no tiene lineas");
            response.put("message", "La factura no tiene lineas");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);

        }

        Map<String, Object> response = new HashMap<>();
        Invoice  newInvoice = null;
        Company company = userModelService.findByUsername(authentication.getName()).getCompany();
        invoice.setCompany(company);

        try {
            newInvoice = invoiceService.save(invoice);
        } catch(DataAccessException e) {
            response.put("error", "Error al realizar el insert en la base de datos");
            response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("title", "Nueva factura");
        response.put("message", "La factura ha sido creada con éxito!");
        response.put("product", newInvoice);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PutMapping("/invoices/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Invoice invoice, BindingResult result, @PathVariable Long id, Authentication authentication){

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
        responseEntity = this.getInvoiceOrErr(id, authentication);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        Map<String, Object> response = new HashMap<>();

        Invoice currentInvoice = (Invoice) responseEntity.getBody();

        currentInvoice.setCustomer(invoice.getCustomer());
        currentInvoice.setObservation(invoice.getObservation());
        currentInvoice.setItems(invoice.getItems());

        Invoice updateInvoice = null;
        try{
            updateInvoice = invoiceService.save(currentInvoice);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar el producto en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El producto ha sido actualizado con éxito!");
        response.put("producto", updateInvoice);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/invoices/load-product/{term}")
    public List<Product> loadProduct(@PathVariable String term, Authentication authentication){
        return productService.findByDescriptionContainingIgnoreCaseAndCompany(term, userModelService.findByUsername(authentication.getName()).getCompany() );
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/invoices/customers")
    public List<Customer> getCustomers(Authentication authentication){
        return customerService.findByCompany(userModelService.findByUsername(authentication.getName()).getCompany());
    }



    private ResponseEntity<?> getInvoiceOrErr(Long id, Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        Invoice invoice = null;
        Company company = userModelService.findByUsername(authentication.getName()).getCompany();

        try {
            invoice = invoiceService.findByIdAndCompany(id, company);
        } catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(invoice == null) {
            response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Invoice>(invoice, HttpStatus.OK);

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
