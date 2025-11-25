package com.proyectoUltimo.Proyecto.model;

public class PaymentForm {
    private String cardNumber;
    private String cardName;
    private String expiryDate;
    private String cvv;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private boolean saveInfo;
    private String carritoData; // JSON
    //Getter and Setters

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public boolean isSaveInfo() {
        return saveInfo;
    }

    public void setSaveInfo(boolean saveInfo) {
        this.saveInfo = saveInfo;
    }

    public String getCarritoData() {
        return carritoData;
    }

    public void setCarritoData(String carritoData) {
        this.carritoData = carritoData;
    }
}

