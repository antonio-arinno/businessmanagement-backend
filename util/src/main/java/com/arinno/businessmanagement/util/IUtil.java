package com.arinno.businessmanagement.util;

import com.arinno.businessmanagement.model.Company;
import org.springframework.security.core.Authentication;

public interface IUtil {

    public Company getCompany(Authentication authentication);
}
