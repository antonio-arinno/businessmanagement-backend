package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.Address;
import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.services.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

/**
 * Created by aarinopu on 27/01/2020.
 */

@Service
public class CompanyServiceImpl implements ICompanyService {

    @Autowired
    private ICompanyDAO companyDAO;

    @Override
    @Transactional(readOnly = true)
    public Company findById(Long id) {
        Company company = companyDAO.findById(id).orElse(null);
        if (company.getAddress() == null){
            company.setAddress(new Address());
        }
        return company;
    }

    @Override
    public Company save(Company company) {
        return companyDAO.save(company);
    }
}
