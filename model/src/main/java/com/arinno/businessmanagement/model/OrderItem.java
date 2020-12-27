package com.arinno.businessmanagement.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="orders_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    private Double price;

    private Double discount;

    private Double iva;

    private IvaType ivaType;

    private String lot;

    @NotNull
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public IvaType getIvaType() {
        return ivaType;
    }

    public void setIvaType(IvaType ivaType) {
        this.ivaType = ivaType;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getAmount(){
        return (price.doubleValue() -
                (price.doubleValue() * (discount.doubleValue() /100))) * quantity.doubleValue();
    }

    public Double getAmountWithIva(){
        return getAmount() + getAmount() * (iva.doubleValue() / 100);
    }

    public Double getAmountIva(){
        return getAmount() * (iva.doubleValue() / 100);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", price=" + price +
                ", discount=" + discount +
                ", iva=" + iva +
                ", ivaType=" + ivaType +
                ", lot='" + lot + '\'' +
                ", product=" + product +
                '}';
    }
}
