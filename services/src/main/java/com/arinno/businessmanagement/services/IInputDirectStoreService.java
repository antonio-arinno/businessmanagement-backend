package com.arinno.businessmanagement.services;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.InputDirectStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IInputDirectStoreService {

    public Page<InputDirectStore> findByCompany(Pageable pageable, Company company);

    public InputDirectStore save(InputDirectStore inputDirectStore);
}
