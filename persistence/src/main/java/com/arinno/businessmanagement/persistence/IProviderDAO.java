package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProviderDAO extends JpaRepository<Provider, Long> {

    public Provider findByIdAndCompany(Long id, Company company);

    List<Provider> findByNameStartingWithIgnoreCaseAndCompany(String term, Company company);
}
