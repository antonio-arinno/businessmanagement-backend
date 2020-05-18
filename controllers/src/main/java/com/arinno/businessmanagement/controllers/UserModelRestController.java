package com.arinno.businessmanagement.controllers;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.UserModel;
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
import java.util.Map;

/**
 * Created by aarinopu on 27/01/2020.
 */

@RestController
@RequestMapping("/api")
public class UserModelRestController {

    @Autowired
    private IUserModelService userModelService;

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserModel(@PathVariable Long id, Authentication authentication){
        return getUserModelOrErr(id, authentication);
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PostMapping("/users")
    public ResponseEntity<?> create(@Valid @RequestBody UserModel userModel, BindingResult result, Authentication authentication) {

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
        Map<String, Object> response = new HashMap<>();
        UserModel newUserModel = null;
        userModel.setCompany(userModelService.findByUsername(authentication.getName()).getCompany());
        try {
            newUserModel = userModelService.save(userModel);
        } catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El cliente ha sido creado con éxito!");
        response.put("cliente", newUserModel);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PutMapping("/users/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody UserModel userModel, BindingResult result, @PathVariable Long id, Authentication authentication) {

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
        responseEntity = this.getUserModelOrErr(id, authentication);

        if(responseEntity.getStatusCode()!=HttpStatus.OK){
            return responseEntity;
        }

        Map<String, Object> response = new HashMap<>();

        UserModel currentUserModel = (UserModel) responseEntity.getBody();
        currentUserModel.setEmail(userModel.getEmail());
        currentUserModel.setName(userModel.getName());
        currentUserModel.setLastName(userModel.getLastName());
        currentUserModel.setPassword(userModel.getPassword());
        currentUserModel.setRoles(userModel.getRoles());
        UserModel updataUserModel = null;

        try{
            updataUserModel = userModelService.save(currentUserModel);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar el producto en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El producto ha sido actualizado con éxito!");
        response.put("usuario", updataUserModel);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

    }


     private ResponseEntity<?> getUserModelOrErr(Long id, Authentication authentication) {

        Map<String, Object> response = new HashMap<>();
        UserModel userModel = null;
        Company company = userModelService.findByUsername(authentication.getName()).getCompany();

        try{
            userModel = userModelService.findByIdAndCompany(id, company);
        } catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(userModel == null){
            response.put("mensaje", "El product ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<UserModel>(userModel, HttpStatus.OK);

    }

}
