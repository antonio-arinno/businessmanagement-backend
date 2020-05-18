package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.Company;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by aarinopu on 27/01/2020.
 */
public interface ICompanyDAO extends CrudRepository<Company, Long> {
}
