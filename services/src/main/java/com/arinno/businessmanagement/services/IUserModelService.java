package com.arinno.businessmanagement.services;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.UserModel;

import javax.validation.Valid;

/**
 * Created by aarinopu on 30/12/2019.
 */
public interface IUserModelService {

    public UserModel findByUsername(String username);

    UserModel findByIdAndCompany(Long id, Company company);

    UserModel save(@Valid UserModel userModel);
}
