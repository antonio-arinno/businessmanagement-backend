package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.BuyOrder;
import com.arinno.businessmanagement.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IBuyOrderDAO extends CrudRepository<BuyOrder, Long> {

    Page<BuyOrder> findByCompany(Pageable pageable, Company company);

    Page<BuyOrder> findByInputDateIsNullAndCompany(Pageable pageable, Company company);

    @Query("select max(buyOrd.number) + 1 from BuyOrder buyOrd where buyOrd.company = ?1")
    Long nextOrderNumber(Company company);

    BuyOrder findByIdAndCompany(Long id, Company company);



}
