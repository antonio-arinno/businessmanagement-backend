package com.arinno.businessmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by aarinopu on 08/01/2020.
 */

@Entity
@Table(name="invoices",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"company_id" , "number"})})
public class Invoice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long number;

    private String observation;

    @Column(name="create_at")
    @Temporal(TemporalType.DATE)
    private Date createAt;

    @NotNull
    @JsonIgnoreProperties(value={"invoices", "hibernateLazyInitializer", "handler"}, allowSetters=true)
    @ManyToOne(fetch=FetchType.LAZY)
    private Customer customer;

    @NotNull
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @NotNull
    @JsonIgnoreProperties(value={"customer", "invoice", "hibernateLazyInitializer", "handler"},allowSetters = true)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "invoice_id")
    private List<Order> items;


    public Invoice() {
        this.items = new ArrayList<>();
    }

    @PrePersist
    public void prePersist(){
        this.createAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Order> getItems() {
        return items;
    }

    public void setItems(List<Order> items) {
        this.items = items;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Double getTotal(){
        Double total = 0.00;
        for(Order item: items){
 //           total += item.getAmount();
            total += 1;
        }
        return total;
    }

    public Double getTotal(IvaType ivaType){
        Double total = 0.00;
        for(Order item: items){
            total += item.getTotal(ivaType);
        }
        return total;
    }

    public Double getTotalIva(IvaType ivaType){
        Double total = 0.00;
        for(Order item: items){
            total += item.getTotalIva(ivaType);
        }
        return total;
    }

    public Double getTotalWithIva(){
        Double total = 0.00;
        for(Order item: items){
            total += item.getTotalWithIva();
        }
        return total;
    }

    public boolean hasIvaType(IvaType ivaType) {
        Boolean hasIvaType = false;
        for(Order item: items){
            if(item.hasIvaType(ivaType)){
                hasIvaType = true;
                break;
            }

        }
        return hasIvaType;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", number=" + number +
                ", observation='" + observation + '\'' +
                ", createAt=" + createAt +
                ", customer=" + customer +
                ", company=" + company +
                ", items=" + items +
                '}';
    }


}
