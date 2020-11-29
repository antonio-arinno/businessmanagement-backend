package com.arinno.businessmanagement.services;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Provider;

import java.util.List;

public interface IProviderService {

    public Provider findByIdAndCompany(Long id, Company company);

    public Provider save(Provider provider);

    public void delete(Long id);

    List<Provider> findByNameStartingWithIgnoreCaseAndCompany(String term, Company company);
}
