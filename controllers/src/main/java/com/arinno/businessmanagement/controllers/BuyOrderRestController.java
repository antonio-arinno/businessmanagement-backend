package com.arinno.businessmanagement.controllers;


import com.arinno.businessmanagement.model.*;
import com.arinno.businessmanagement.services.IBuyOrderService;
import com.arinno.businessmanagement.services.IProductLotService;
import com.arinno.businessmanagement.services.IProductService;
import com.arinno.businessmanagement.services.IProviderService;
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
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"https://businessmanagement-7b334.web.app", "http://localhost:4200", "*"})
@RestController
@RequestMapping("/api")
public class BuyOrderRestController {

    @Autowired
    private IBuyOrderService buyOrderService;

    @Autowired
    private IProviderService providerService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IProductLotService productLotService;

    @Autowired
    private IUtil util;

    @Autowired
    private IGeneratePdfReport generatePdfReport;

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/buy-orders/{id}")
    public ResponseEntity<?> getBuyOrder(@PathVariable Long id, Authentication authentication){
        return getBuyOrderOrErr(id, authentication);
    }


    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/buy-orders/page/{page}")
    public Page<BuyOrder> getBuyOrders(@PathVariable Integer page, Authentication authentication){
        Pageable pageable = PageRequest.of(page, 5);
        return buyOrderService.findByCompany(pageable, util.getCompany(authentication));
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/buy-orders/pending/page/{page}")
    public Page<BuyOrder> getPendingBuyOrders(@PathVariable Integer page, Authentication authentication){
        Pageable pageable = PageRequest.of(page, 5);
        return buyOrderService.findByInputDateIsNullAndCompany(pageable, util.getCompany(authentication));
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/buy-orders/load-provider-name/{term}")
    public List<Provider> loadProviderCode(@PathVariable String term, Authentication authentication){
        return providerService.findByNameStartingWithIgnoreCaseAndCompany(term, util.getCompany(authentication));
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/buy-orders/load-product/{idProvider}/{term}")
    public List<Product> loadProduct(@PathVariable Long idProvider, @PathVariable String term, Authentication authentication){
        Provider provider = providerService.findByIdAndCompany(idProvider, util.getCompany(authentication));
        Company company = util.getCompany(authentication);
        return productService.findByDescriptionContainingIgnoreCaseAndProviderAndCompany(term, provider, company);
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PostMapping("/buy-orders")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Valid @RequestBody BuyOrder buyOrder, BindingResult result, Authentication authentication){

        ResponseEntity<?> responseEntity = null;
        responseEntity = getErrRequestBody(result);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        if (buyOrder.getProvider().getId()==null){
            Map<String, Object> response = new HashMap<>();
            response.put("error", "El Proveedor no puede ser nulo");
            response.put("message", "El proveedor no puede ser nulo");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (buyOrder.getItems().size()==0){
            Map<String, Object> response = new HashMap<>();
            response.put("error", "El albaran no tiene lineas");
            response.put("message", "El albaran no tiene lineas");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> response = new HashMap<>();
        BuyOrder  newBuyOrder = null;
        Company company = util.getCompany(authentication);
        buyOrder.setCompany(company);

        try {
            newBuyOrder = buyOrderService.save(buyOrder);
        } catch(DataAccessException e) {
            response.put("error", "Error al realizar el insert en la base de datos");
            response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("title", "Nueva Orden");
        response.put("message", "La orden ha sido creada con éxito!");
        response.put("buyOrder", newBuyOrder);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PutMapping("/buy-orders/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody BuyOrder buyOrder, BindingResult result, @PathVariable Long id, Authentication authentication){

        ResponseEntity<?> responseEntity = null;
        responseEntity = getErrRequestBody(result);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        responseEntity = this.getBuyOrderOrErr(id, authentication);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        BuyOrder currentBuyOrder = (BuyOrder) responseEntity.getBody();

        if(currentBuyOrder.getInputDate()!=null){
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Productos ya recibidos");
            response.put("message", "Productos ya recibidos "+ currentBuyOrder.getInputDate());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (buyOrder.getProvider().getId()==null){
            Map<String, Object> response = new HashMap<>();
            response.put("error", "El Proveedor no puede ser nulo");
            response.put("message", "El Proveedor no puede ser nulo");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (buyOrder.getItems().size()==0){
            Map<String, Object> response = new HashMap<>();
            response.put("error", "El Albaran no tiene lineas");
            response.put("message", "El Albaran no tiene lineas");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> response = new HashMap<>();

        currentBuyOrder.setProvider(buyOrder.getProvider());
        currentBuyOrder.setCreateAt(buyOrder.getCreateAt());
        currentBuyOrder.setObservation(buyOrder.getObservation());
        currentBuyOrder.setItems(buyOrder.getItems());

        BuyOrder updateBuyOrder = null;
        try {
            updateBuyOrder = buyOrderService.update(currentBuyOrder);
        } catch (DataAccessException e) {
            response.put("error", "Error al actualizar la order en la base de datos");
            response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "la orden ha sido actualizado con éxito!");
        response.put("orden", updateBuyOrder);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @DeleteMapping("/buy-orders/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication authentication){

        ResponseEntity<?> responseEntity = null;
        responseEntity = getBuyOrderOrErr(id, authentication);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        BuyOrder currentBuyOrder = (BuyOrder) responseEntity.getBody();

        Map<String, Object> response = new HashMap<>();

        try {
            buyOrderService.deleteById(id);
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
    @PutMapping("/buy-orders/update-store/{id}")
    public ResponseEntity<?> updateStore(@Valid @RequestBody BuyOrder buyOrder, BindingResult result, @PathVariable Long id, Authentication authentication){

        if(this.getErrRequestBody(result).getStatusCode()!=HttpStatus.OK){
            return getErrRequestBody(result);
        }

        if(!buyOrder.hasAllLots()){
            Map<String, Object> response = new HashMap<>();
            response.put("error", "No se admiten entregas parciales");
            response.put("message", "Al menos hay un producto sin Lote");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if(this.getBuyOrderOrErr(id, authentication).getStatusCode()!=HttpStatus.OK){
            return this.getBuyOrderOrErr(id, authentication);
        }
        BuyOrder currentBuyOrder = (BuyOrder) this.getBuyOrderOrErr(id, authentication).getBody();

        if(currentBuyOrder.getInputDate()!=null){
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Productos ya recibidos");
            response.put("message", "Productos ya recibidos "+ currentBuyOrder.getInputDate());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> response = new HashMap<>();

        currentBuyOrder.setInputDate(new Date());
        currentBuyOrder.setItems(buyOrder.getItems());

        BuyOrder updateBuyOrder = null;
        try {
            updateBuyOrder = buyOrderService.update(currentBuyOrder);
        } catch (DataAccessException e) {
            response.put("error", "Error al actualizar la order en la base de datos");
            response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ProductLot productLot = null;
        for (BuyOrderItem item : updateBuyOrder.getItems()) {
            productLot = new ProductLot();
            productLot.setProduct(item.getProduct());
            productLot.setLot(item.getLot());
            productLot.setStock(item.getQuantity());
            productLot.setCompany(updateBuyOrder.getCompany());
            productLotService.save(productLot);
        }

        response.put("message", "El Almacen ha sido actualizado correctamente");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/buy-orders/pdf/{id}")
    public ResponseEntity<InputStreamResource> PDF (@PathVariable Long id, Authentication authentication) throws DocumentException {

        ResponseEntity<?> responseEntity = null;
        responseEntity = getBuyOrderOrErr(id, authentication);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return (ResponseEntity<InputStreamResource>) ResponseEntity.badRequest();
        }

        BuyOrder buyOrder = (BuyOrder) responseEntity.getBody();
        ByteArrayInputStream bais = null;
        bais = generatePdfReport.generateBuyOrder(buyOrder);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=order.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bais));
    }

    private ResponseEntity<?> getBuyOrderOrErr(Long id, Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        BuyOrder buyOrder = null;
        Company company = util.getCompany(authentication);

        try {
            buyOrder = buyOrderService.findByIdAndCompany(id, company);
        } catch(DataAccessException e) {
            response.put("error", "Error al realizar la consulta en la base de datos");
            response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(buyOrder == null) {
            response.put("error", HttpStatus.NOT_FOUND);
            response.put("message", "La orden de compra ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<BuyOrder>(buyOrder, HttpStatus.OK);

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
