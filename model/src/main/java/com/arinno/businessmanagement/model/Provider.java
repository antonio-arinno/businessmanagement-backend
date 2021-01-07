package com.arinno.businessmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name="providers", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"company_id" , "code"})})
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String name;

    @Embedded
    private Address address;

    @Column(name="create_at")
    @Temporal(TemporalType.DATE)
    private Date createAt;

    @NotNull
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "Provider{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", createAt=" + createAt +
                ", company=" + company +
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
