package com.arinno.businessmanagement.controllers;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Customer;
import com.arinno.businessmanagement.model.Product;
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
 * Created by aarinopu on 22/01/2020.
 */

@CrossOrigin(origins = {"https://businessmanagement-7b334.web.app", "http://localhost:4200", "*"})
@RestController
@RequestMapping("/api")
public class ProductRestController {


    @Autowired
    private IProductService productService;

    @Autowired
    private IUserModelService userModelService;


    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/products")
    public List<Product> getProducts(Authentication authentication){
         return productService.findByCompany(userModelService.findByUsername(authentication.getName()).getCompany());
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id, Authentication authentication){
        return getProductOrErr(id, authentication);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication authentication){

        ResponseEntity<?> responseEntity = null;
        responseEntity = getProductOrErr(id, authentication);
        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }
        Map<String, Object> response = new HashMap<>();
        try {
            productService.delete(id);
        } catch (DataAccessException e) {
            response.put("error", "Error al eliminar el cliente de la base de datos");
            response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("title", "Producto eliminado");
        response.put("message", "Producto eliminado con éxito!");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PostMapping("/products")
    public ResponseEntity<?> create(@Valid @RequestBody Product product, BindingResult result, Authentication authentication) {

        ResponseEntity<?> responseEntity = null;
        responseEntity = getErrRequestBody(result);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        Map<String, Object> response = new HashMap<>();
        Product newProduct = null;
        product.setCompany(userModelService.findByUsername(authentication.getName()).getCompany());
        try {
            newProduct = productService.save(product);
        } catch(DataAccessException e) {
            response.put("error", "Error al realizar el insert en la base de datos");
            response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("title", "Nuevo producto");
        response.put("message", "El producto ha sido creado con éxito!");
        response.put("product", newProduct);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

    }


    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PutMapping("/products/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Product product, BindingResult result, @PathVariable Long id, Authentication authentication){

        ResponseEntity<?> responseEntity = null;
        responseEntity = getErrRequestBody(result);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        responseEntity = this.getProductOrErr(id, authentication);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        Map<String, Object> response = new HashMap<>();

        Product currentProduct = (Product) responseEntity.getBody();
        currentProduct.setDescription(product.getDescription());
        currentProduct.setPrice(product.getPrice());
        currentProduct.setCode(product.getCode());
        Product updateProduct = null;
        try{
            updateProduct = productService.save(currentProduct);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar el producto en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("title", "Producto actualizado");
        response.put("message", "El producto ha sido actualizado con éxito!");
        response.put("product", updateProduct);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

    }

    private ResponseEntity<?> getProductOrErr(Long id, Authentication authentication) {

        Map<String, Object> response = new HashMap<>();
        Product product = null;
        Company company = userModelService.findByUsername(authentication.getName()).getCompany();

        try{
            product = productService.findByIdAndCompany(id, company);
        } catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(product == null){
            response.put("mensaje", "El product ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Product>(product, HttpStatus.OK);

    }

    private ResponseEntity<?> getErrRequestBody(BindingResult result) {
        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .filter(err -> !"company".equals(err.getField()))
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













