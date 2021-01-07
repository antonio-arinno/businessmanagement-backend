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

    @Embedded
    private Address address;


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



    public com.arinno.businessmanagement.model.Address getAddress() {
        return address;
    }

    public void setAddress(com.arinno.businessmanagement.model.Address address) {
        this.address = address;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", taxId='" + taxId + '\'' +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", createAt=" + createAt +
                ", company=" + company +
                ", invoices=" + invoices +
                '}';
    }

    public boolean hasIncompleteAddress() {
        Boolean hasIncompleteAddress = false;
        if(address.getTypeStreet()==null ||
                address.getStreet()==null ||
                address.getNumberKm()==null ||
                address.getTown()==null ||
                address.getStateProvince()==null ||
                address.getCountry()==null ||
                address.getPostalCode()==null){
            hasIncompleteAddress = true;
        }
        return hasIncompleteAddress;
    }
}
