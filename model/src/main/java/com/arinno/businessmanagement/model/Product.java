package com.arinno.businessmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by aarinopu on 08/01/2020.
 */

@Entity
@Table(name="products", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"company_id" , "code"})})
 //       ,@UniqueConstraint(columnNames = {"company_id" , "description"})})

public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String code;

    private String description;

    @Column(name="buy_price")
    private Double buyPrice;

    @Column(name="sale_price")
    private Double salePrice;

    @Enumerated(value = EnumType.ORDINAL)
    private IvaType ivaType;

    @Column(name="create_at")
    @Temporal(TemporalType.DATE)
    private Date createAt;


    @NotNull
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    private Provider provider;

    @NotNull
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }


    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }


    public IvaType getIvaType() {
        return ivaType;
    }

    public void setIvaType(IvaType ivaType) {
        this.ivaType = ivaType;
    }

    public void setCompany(Company company) {
        this.company = company;
    }


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", code=" + code +
                ", description='" + description + '\'' +
                ", price=" + salePrice +
                ", createAt=" + createAt +
                ", company=" + company +
                '}';
    }
}
