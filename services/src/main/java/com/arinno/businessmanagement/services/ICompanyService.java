package com.arinno.businessmanagement.services;

import com.arinno.businessmanagement.model.Company;

import javax.validation.Valid;

/**
 * Created by aarinopu on 22/01/2020.
 */
public interface ICompanyService {

    Company findById(Long id);

    Company save(@Valid Company company);
}
