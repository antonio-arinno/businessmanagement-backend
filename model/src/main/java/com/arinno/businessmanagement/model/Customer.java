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

    @NotNull
    private String address;

    private String typeStreet;

    private String Street;

    private String numberKm;

    private String country;

    private String stateProvince;

    private String town;

    @NotNull
    private String postalCode;

    private String building;

    private String staircase;

    private String floor;

    private String door;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTypeStreet() {
        return typeStreet;
    }

    public void setTypeStreet(String typeStreet) {
        this.typeStreet = typeStreet;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getNumberKm() {
        return numberKm;
    }

    public void setNumberKm(String numberKm) {
        this.numberKm = numberKm;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getStaircase() {
        return staircase;
    }

    public void setStaircase(String staircase) {
        this.staircase = staircase;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDoor() {
        return door;
    }

    public void setDoor(String door) {
        this.door = door;
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

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", code=" + code +
                ", name='" + name + '\'' +
                ", createAt=" + createAt +
                ", company=" + company +
                ", invoices=" + invoices +
                '}';
    }
}
