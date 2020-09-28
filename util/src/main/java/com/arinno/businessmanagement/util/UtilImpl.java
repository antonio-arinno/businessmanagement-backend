package com.arinno.businessmanagement.util;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.services.IUserModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UtilImpl implements IUtil {

    @Autowired
    private IUserModelService userModelService;

    @Override
    public Company getCompany(Authentication authentication) {
        return userModelService.findByUsername(authentication.getName()).getCompany();
    }
}
