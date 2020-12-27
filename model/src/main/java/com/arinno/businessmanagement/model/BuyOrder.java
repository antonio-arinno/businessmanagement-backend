package com.arinno.businessmanagement.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="buy_orders",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"company_id" , "number"})})
public class BuyOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long number;

    private String observation;

    @NotNull
    @Column(name="create_at")
    @Temporal(TemporalType.DATE)
    private Date createAt;

    @Column(name="input_date")
    @Temporal(TemporalType.DATE)
    private Date inputDate;



    @NotNull
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch=FetchType.LAZY)
    private Provider provider;

    @NotNull
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "buy_order_id")
    private List<BuyOrderItem> items;

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

    public Date getInputDate() {
        return inputDate;
    }

    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public List<BuyOrderItem> getItems() {
        return items;
    }

    public void setItems(List<BuyOrderItem> items) {
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
        for(BuyOrderItem item: items){
            total += item.getAmount();
        }
        return total;
    }

    public Double getTotalWithIva(){
        Double total = 0.00;
        for(BuyOrderItem item: items){
            total += item.getAmountWithIva();
        }
        return total;
    }

    public Double getTotal(IvaType ivaType){
        Double total = 0.00;
        for(BuyOrderItem item: items){
            if(item.getIvaType() == ivaType) {
                total += item.getAmount();
            }
        }
        return total;
    }

    public Double getTotalIva(IvaType ivaType){
        Double total = 0.00;
        for(BuyOrderItem item: items){
            if(item.getIvaType() == ivaType) {
                total += item.getAmountIva();
            }
        }
        return total;
    }

    public boolean hasIvaType(IvaType ivaType) {
        Boolean hasIvaType = false;
        for(BuyOrderItem item: items){
            if(item.getIvaType() == ivaType) {
                hasIvaType = true;
                break;
            }
        }
        return hasIvaType;
    }

    @Override
    public String toString() {
        return "BuyOrder{" +
                "id=" + id +
                ", number=" + number +
                ", observation='" + observation + '\'' +
                ", createAt=" + createAt +
                ", provider=" + provider +
                ", items=" + items +
                ", company=" + company +
                '}';
    }
}
