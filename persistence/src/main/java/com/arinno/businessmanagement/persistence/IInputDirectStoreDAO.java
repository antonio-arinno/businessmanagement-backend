package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.InputDirectStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInputDirectStoreDAO extends JpaRepository<InputDirectStore, Long> {


    public Page<InputDirectStore> findByCompany(Pageable pageable, Company company);

}
