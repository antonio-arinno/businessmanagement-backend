package com.arinno.businessmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by aarinopu on 10/12/2019.
 */

@Entity
@Table(name="customers", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"company_id" , "code"})})
 //       ,@UniqueConstraint(columnNames = {"company_id" , "name"})})

public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String taxId;

    @NotEmpty
    private String name;

//    @NotNull
    private String fullAddress;

    @Embedded
    private Address address;

    private String telephone;

    private String email;

    @NotNull
    @Column(name="create_at")
    @Temporal(TemporalType.DATE)
    private Date createAt;

    @NotNull
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @JsonIgnoreProperties(value={"customer", "hibernateLazyInitializer", "handler"},allowSetters = true)
    @OneToMany(fetch = FetchType.LAZY, mappedBy ="customer", cascade = CascadeType.ALL)
    private List<Invoice> invoices;

    public Customer() {
        this.invoices = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public com.arinno.businessmanagement.model.Address getAddress() {
        return address;
    }

    public void setAddress(com.arinno.businessmanagement.model.Address address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCompany(Company company) {
        this.company = company;
    }


}
