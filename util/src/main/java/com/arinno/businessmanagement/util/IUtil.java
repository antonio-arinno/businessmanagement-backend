package com.arinno.businessmanagement.util;

import com.arinno.businessmanagement.model.Company;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;

public interface IUtil {

    public Company getCompany(Authentication authentication);

    public ResponseEntity<?> getErrRequestBody(BindingResult result);
}
