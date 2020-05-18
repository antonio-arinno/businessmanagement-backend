package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.UserModel;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by aarinopu on 15/12/2019.
 */
public interface IUserModelDAO extends CrudRepository<UserModel, Long> {

    public UserModel findByUsername(String username);

    UserModel findByIdAndCompany(Long id, Company company);
}
