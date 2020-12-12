package com.arinno.businessmanagement.services;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProviderService {

    public Provider findByIdAndCompany(Long id, Company company);

    public Provider findByCodeAndCompany(String code, Company company);

    public Page<Provider> findByCompany(Pageable pageable, Company company);

    public Provider save(Provider provider);

    public void delete(Long id);

    List<Provider> findByNameStartingWithIgnoreCaseAndCompany(String term, Company company);
}
