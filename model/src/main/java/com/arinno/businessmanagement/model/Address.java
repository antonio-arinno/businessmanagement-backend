package com.arinno.businessmanagement.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;


@Embeddable
public class Address {

    private String typeStreet;

    private String street;

    private String numberKm;

    private String country;

    private String stateProvince;

    private String town;

    private String postalCode;

    private String building;

    private String staircase;

    private String floor;

    private String door;

    private String additionalInformation;

    private String telephone;

    private String email;

    private String web;

    public String getTypeStreet() {
        return typeStreet;
    }

    public void setTypeStreet(String typeStreet) {
        this.typeStreet = typeStreet;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumberKm() {
        return numberKm;
    }

    public void setNumberKm(String numberKm) {
        this.numberKm = numberKm;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getAddressLine_1() {
        return getValueOrString(this.typeStreet) + " " + getValueOrString(this.street) + ", " + getValueOrString(this.numberKm) + " " + getValueOrString(this.floor) + " " + getValueOrString(this.door);
    }

    public String getAddressLine_2() {
        return getValueOrString(this.stateProvince) + ", " + getValueOrString(this.town) +" - " +
                getValueOrString(this.postalCode);
    }


    public String getAddressLine_3(){
        return getValueOrString(this.building) + " " + getValueOrString(this.staircase) + " " +
                getValueOrString(this.additionalInformation);
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

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    @Override
    public String toString() {
        return "Address{" +
                "typeStreet='" + typeStreet + '\'' +
                ", street='" + street + '\'' +
                ", numberKm='" + numberKm + '\'' +
                ", country='" + country + '\'' +
                ", stateProvince='" + stateProvince + '\'' +
                ", town='" + town + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", building='" + building + '\'' +
                ", staircase='" + staircase + '\'' +
                ", floor='" + floor + '\'' +
                ", door='" + door + '\'' +
                ", additionalInformation='" + additionalInformation + '\'' +
                '}';
    }

    private String getValueOrString(String value) {
        return (value == null) ? "" : value;
    }




}
