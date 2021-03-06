package com.arinno.businessmanagement.controllers;

import com.arinno.businessmanagement.model.*;
import com.arinno.businessmanagement.services.*;
import com.arinno.businessmanagement.util.EmailBody;
import com.arinno.businessmanagement.util.IEmailPort;
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

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private IUtil util;

    @Autowired
    private IProductService productService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IEmailPort emailPort;

    @Autowired
    private IGeneratePdfReport generatePdfReport;

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/invoices/{id}")
    public ResponseEntity<?> getInvoice(@PathVariable Long id, Authentication authentication){
        return getInvoiceOrErr(id, authentication);
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/invoices")
    public List<Invoice> getInvoices(Authentication authentication){
        return invoiceService.findByCompany(util.getCompany(authentication));
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/invoices/page/{page}")
    public Page<Invoice> getInvoices(@PathVariable Integer page, Authentication authentication){
        Pageable pageable = PageRequest.of(page, 5);
        return invoiceService.findByCompany(pageable, util.getCompany(authentication));
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
        Company company = util.getCompany(authentication);
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
        response.put("invoice", newInvoice);
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
        return productService.findByDescriptionContainingIgnoreCaseAndCompany(term, util.getCompany(authentication) );
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/invoices/customers")
    public List<Customer> getCustomers(Authentication authentication){
        return customerService.findByCompany(util.getCompany(authentication));
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/invoices/load-customer-name/{term}")
    public List<Customer> loadCustomerName(@PathVariable String term, Authentication authentication){
        return customerService.findByNameContainingIgnoreCaseAndCompany(term, util.getCompany(authentication));
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/invoices/email/{id}")
    public ResponseEntity<?> SendMail (@PathVariable Long id,  Authentication authentication) throws MessagingException, IOException, DocumentException {

        ResponseEntity<?> responseEntity = null;
        responseEntity = getInvoiceOrErr(id, authentication);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        Map<String, Object> response = new HashMap<>();

        Invoice invoice = (Invoice) responseEntity.getBody();

        EmailBody emailBody = new EmailBody();
        emailBody.setEmail("antonio.arinno@gmail.com");
        emailBody.setSubject("prueba");
        emailBody.setContent("Content");

        try{
            emailPort.sendEmail(emailBody, invoice);
        } catch (Exception e){
            response.put("error", "No se ha podido enviar el email");
            response.put("message", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);

        }

        response.put("message", "Email enviado con éxito!");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }


    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/invoices/generate")
    public ResponseEntity<?> generate (Authentication authentication){

        List<Order> orders = null;

        List<Customer> customers = orderService.findCustomerDistinctByInvoiceIsNullAndCompany(util.getCompany(authentication));

        if (customers.size()==0){
            Map<String, Object> response = new HashMap<>();
            response.put("error", "No hay facturacion pendiente");
            response.put("message", "No hay facturacion pendiente");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> response = new HashMap<>();

        for (Customer customer : customers) {
            orders = orderService.findByInvoiceIsNullAndCustomer(customer);
            Invoice invoice = new Invoice();
            invoice.setCustomer(customer);
            invoice.setCompany(util.getCompany(authentication));
            invoice.setItems(orders);
            try {
                invoiceService.save(invoice);
            } catch(DataAccessException e) {
                response.put("error", "Error al realizar el insert en la base de datos");
                response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        response.put("title", "Facturacion generada con exito");
        response.put("message", customers.size() +" Clientes facturados");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PostMapping("/invoices/generate/id-dates")
    public ResponseEntity<?> generate (@Valid @RequestBody IdDates idDates, BindingResult result, Authentication authentication){

        if (idDates.getId()==null){
            Map<String, Object> response = new HashMap<>();
            response.put("error", "El Cliente no puede ser nulo");
            response.put("message", "El cliente no puede ser nulo");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        Customer customer = customerService.findByIdAndCompany(idDates.getId(), util.getCompany(authentication));

        System.out.print(idDates.toString());

        if (idDates.getDateRange().getFromDate() ==null || idDates.getDateRange().getToDate()==null){
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Rango de fechas invalido");
            response.put("message", "Rango de fechas invalido");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setCompany(util.getCompany(authentication));
        invoice.setItems(orderService.findByInvoiceIsNullAndCustomerAndCreateAtAfterAndCreateAtBefore
                (customer, idDates.getDateRange().getFromDate(), idDates.getDateRange().getToDate()));

        if(invoice.getItems().size()==0){
            Map<String, Object> response = new HashMap<>();
            response.put("error", "No hay albaranes sin facturar");
            response.put("message", "No hay albaranes sin facturar");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> response = new HashMap<>();
        try {
            invoiceService.save(invoice);

        } catch(DataAccessException e) {
            response.put("error", "Error al realizar el insert en la base de datos");
            response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "Facturas generadas con éxito!");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PostMapping("/invoices/generate/dates")
    public ResponseEntity<?> generate (@Valid @RequestBody DateRange dateRange, BindingResult result, Authentication authentication){

        if (dateRange.getFromDate() ==null || dateRange.getToDate()==null){
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Rango de fechas invalido");
            response.put("message", "Rango de fechas invalido");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        List<Order> orders = null;

        List<Customer> customers =
                orderService.findCustomerDistinctByInvoiceIsNullAndCreateAtBetweenAndCompany
                        (dateRange.getFromDate(), dateRange.getToDate(), util.getCompany(authentication));

        if (customers.size()==0){
            Map<String, Object> response = new HashMap<>();
            response.put("error", "No hay facturacion pendiente");
            response.put("message", "No hay facturacion pendiente");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> response = new HashMap<>();

        for (Customer customer : customers) {
        //    orders = orderService.findByInvoiceIsNullAndCustomerAndCreateAtAfterAndCreateAtBefore
        //            (customer, dateRange.getFromDate(), dateRange.getToDate());

            orders = orderService.findByInvoiceIsNullAndCustomerAndCreateAtBetween
                    (customer, dateRange.getFromDate(), dateRange.getToDate());

            Invoice invoice = new Invoice();
            invoice.setCustomer(customer);
            invoice.setCompany(util.getCompany(authentication));
            invoice.setItems(orders);
            try {
                invoiceService.save(invoice);
            } catch(DataAccessException e) {
                response.put("error", "Error al realizar el insert en la base de datos");
                response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        response.put("title", "Facturacion generada con exito");
        response.put("message", customers.size() +" Clientes facturados");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/invoices/pdf/{id}")
    public ResponseEntity<InputStreamResource> PDF (@PathVariable Long id, Authentication authentication) throws DocumentException {
        ResponseEntity<?> responseEntity = null;
        responseEntity = getInvoiceOrErr(id, authentication);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return (ResponseEntity<InputStreamResource>) ResponseEntity.badRequest();
        }

        Invoice invoice = (Invoice) responseEntity.getBody();
        ByteArrayInputStream bais = null;
        bais = generatePdfReport.generateInvoice(invoice);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=invoice.pdf");

        System.out.print("PDF3");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bais));
    }

    private ResponseEntity<?> getInvoiceOrErr(Long id, Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        Invoice invoice = null;
        Company company = util.getCompany(authentication);

        try {
            invoice = invoiceService.findByIdAndCompany(id, company);
        } catch(DataAccessException e) {
            response.put("error", "Error al realizar la consulta en la base de datos");
            response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(invoice == null) {
            response.put("error", HttpStatus.NOT_FOUND);
            response.put("message", "La factura ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
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
