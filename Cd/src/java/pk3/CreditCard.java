package pk3;

import pk2.DataBase;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement     //only needed if we also want to generate XML
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
    
    public Boolean isValidate(){
        
        if(number.length()!=16){
            System.out.println("######################################   number.length()!=16");
            return false;
        }
        long number2;
        try{
            number2=Long.parseLong(number);
        }catch(Exception e){
            System.out.println("###############"+number+"###########");
            System.out.println("################################# pk1.CreditCard.isValidate()   number2=Integer.parseInt(number);");
            number2=0;
            return false;
        }
        int nmberType = (int)(number2 / 1000000000);
        if(!DataBase.validatType(nmberType, type)){
            System.out.println("################################### pk1.CreditCard.isValidate()    !DataBase.validatType(nmberType, type)");
            return false;
        }
//        String nmberType = "";
//        for(int i=0;i<5;i++)
//            nmberType+=card.getNumber().getChars(0, 6, nmberType, 0);
        
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        return true;
    }
    
    
    
}
