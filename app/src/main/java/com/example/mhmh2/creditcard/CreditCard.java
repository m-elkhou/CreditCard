package com.example.mhmh2.creditcard;

public class CreditCard {
    
    private long id;
    private String number;
    private String expiryDate;
    private Integer controlNumber;
    private String type;
    
    public CreditCard() {
    
    }

    public CreditCard(Long id, String number, String expiryDate, Integer controlNumber, String type) {
        this.id = id;
        this.number = number;
        this.expiryDate = expiryDate;
        this.controlNumber = controlNumber;
        this.type = type;
    }
    // type :
    // new CreditCard("5555666677778884").getBrand();    //return MASTERCARD
    // new CreditCard("4111111111111111").getBrand();    //return VISA
    // new CreditCard("341111111111111").getBrand();     //return AMEX
    // new CreditCard("30111122223331").getBrand();      //return DINERS
    // new CreditCard("3841001111222233334").getBrand(); //return HIPERCARD
    // new CreditCard("4514160123456789").getBrand();    //return ELO
    // new CreditCard("6370950000000005").getBrand();    //return HIPER

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public Integer getControlNumber() {
        return controlNumber;
    }

    public String getType() {
        return type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setControlNumber(Integer controlNumber) {
        this.controlNumber = controlNumber;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CreditCard{" + "id=" + id + ", number=" + number + ", expiryDate=" + expiryDate + ", controlNumber=" + controlNumber + ", type=" + type + '}';
    }

    public String toXML() {
        return "<creditCard><id>" + id + "</id><number>" + number + "</number><expiryDate>" + expiryDate
                + "</expiryDate><controlNumber>" + controlNumber + "</controlNumber><type>" + type + "</type></creditCard>";
    }

    public String toJSON() {
        return "{" + "\"id\":" + id + ",\"number\":\"" + number + "\",\"expiryDate\":\"" + expiryDate + "\",\"controlNumber\""
                + controlNumber + ",\"type\":\"" + type + "\"}";
    }
}
