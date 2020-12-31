package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.InputDirectStore;
import com.arinno.businessmanagement.services.IInputDirectStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InputDirectStoreServiceImpl implements IInputDirectStoreService {

    @Autowired
    private IInputDirectStoreDAO inputDirectStoreDAO;

    @Override
    @Transactional(readOnly = true)
    public Page<InputDirectStore> findByCompany(Pageable pageable, Company company) {
        return inputDirectStoreDAO.findByCompany(pageable, company);
    }

    @Override
    @Transactional
    public InputDirectStore save(InputDirectStore inputDirectStore) {
        return inputDirectStoreDAO.save(inputDirectStore);
    }
}
