package pk2;

import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import pk3.Client;
import pk3.CreditCard;

public class DataBase {
    private static Connection con;
    private static Statement stmt;
    private static ResultSet res;
    private static String sql = "";
    
    public DataBase(){
    }  
    
    private static void Connect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            String Bd = "jdbc:mysql://localhost/bank?autoReconnect=true&useSSL=false";
            String user = "root";
            String password = "";
            con = DriverManager.getConnection(Bd, user, password);
            stmt = con.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("\n\n##### ! Error !! :   " + e);
	}
    }
    
    public static CreditCard getCreditCard(long id){
        Connect();
        sql="SELECT * FROM `creditcard` WHERE id="+id;
        try {
            res = stmt.executeQuery(sql);
            res.next();
            return new CreditCard(id,res.getString(2),res.getString(3),Integer.parseInt(res.getString(4)),res.getString(5));
        } catch (SQLException e) {
            System.out.println("\n\n##### ! Error !! :   " + e);
	}
        return new CreditCard(id, "0000000000000000", "00/00", 0, "!Erreur : 404 ! : NOT FOND !!! ");
    }
    
    public static Vector<CreditCard> getAllCard(){
        Vector<CreditCard> vect = new Vector<CreditCard>();
        Connect();
        sql="SELECT * FROM `creditcard` ";
        try {
            res = stmt.executeQuery(sql);
            while(res.next())
                vect.add( new CreditCard(Long.parseLong(res.getString(1)),res.getString(2),res.getString(3),Integer.parseInt(res.getString(4)),res.getString(5)) );
            return vect;
        } catch (SQLException e) {
            System.out.println("\n\n##### ! Error !! :   " + e);
	}
        return null;
    }
    
    public static boolean insertCreditCard(CreditCard creditCard){
        Connect();
       
        sql="INSERT INTO `creditcard` (`number`, `expiryDate`, `controlNumber`, `type`) VALUES ('"
                +creditCard.getNumber()+"', '"+creditCard.getExpiryDate()
                +"', '"+creditCard.getControlNumber()+"', '"+creditCard.getType()+"');";
        try {
            if(!stmt.execute(sql))
                return true;
            else return false;
        } catch (SQLException e) {
            System.out.println("\n\n##### ! Error !! :   " + e);
            return false;
	}
    }
    
    public static boolean isExistedCard(CreditCard creditCard) {
        Connect();
        
        sql="SELECT * FROM `creditcard` WHERE id="+creditCard.getId()+" and number LIKE '"+creditCard.getNumber()+
                "' and expiryDate LIKE '"+creditCard.getExpiryDate()+"' and controlNumber = "+creditCard.getControlNumber()+
                " and type LIKE '"+creditCard.getType()+"' ";
        try {
            res = stmt.executeQuery(sql);
            int b = 0;
            while(res.next())
                b=1;
            if(b==1) return true;
        } catch (SQLException e) {
            System.out.println("\n\n##### ! Error !! :   " + e);
	}
        return false;
    }
    
    public static boolean validatType(int number, String Type){
        Connect();
        try {
            sql="SELECT * FROM `creditcard` WHERE `number` LIKE '"+number+"%' AND `type` LIKE '"+Type+"' ";
            res = stmt.executeQuery(sql);
            int b = 0;
            while(res.next())
                b=1;
            if(b==1) return true;
        } catch (SQLException e) {
            System.out.println("\n\n##### ! Error !! :   " + e);
	}
        return false;
    }
    
    public static boolean isExistedClient(Client c) {
        Connect();
        
        sql="SELECT * FROM `client` WHERE login LIKE '"+c.getUser()+"' and pswd LIKE '"+c.getPswd()+"' ";
        try {
            res = stmt.executeQuery(sql);
            int b = 0;
            while(res.next())
                b=1;
            if(b==1) return true;
        } catch (SQLException e) {
            System.out.println("\n\n##### ! Error !! :   " + e);
	}
        return false;
    }
    
    public static Client getClient(long id){
        Connect();
        sql="SELECT * FROM `client` WHERE id="+id;
        try {
            res = stmt.executeQuery(sql);
            res.next();
            return new Client(id,res.getString(2),res.getString(3),res.getString(4),res.getString(5));
        } catch (SQLException e) {
            System.out.println("\n\n##### ! Error !! :   " + e);
	}
        return new Client();
    }
    
    public static Vector<Client> getAllClient(){
        Vector<Client> vect = new Vector<Client>();
        Connect();
        sql="SELECT * FROM `client` ";
        try {
            res = stmt.executeQuery(sql);
            while(res.next())
                vect.add( new Client(Long.parseLong(res.getString(1)),res.getString(2),res.getString(3),res.getString(4),res.getString(5)) );
            return vect;
        } catch (SQLException e) {
            System.out.println("\n\n##### ! Error !! :   " + e);
	}
        return null;
    }
    
    public static boolean insertClient(Client c){
        Connect();
       
        sql="INSERT INTO `client` (`login`, `pswd`, `mail`, `phone`) VALUES ('"
                +c.getUser()+"', '"+c.getPswd()+"', '"+c.getMail()+"', '"+c.getPhone()+"');";
        try {
            if(!stmt.execute(sql))
                return true;
            else return false;
        } catch (SQLException e) {
            System.out.println("\n\n##### ! Error !! :   " + e);
            return false;
	}
    }
    
    public static void main(String[] args) {
        CreditCard cr = getCreditCard(1);
        System.out.println(cr.toJSON());
        
        
        String s = "01/01/2012";
        Date d = new Date(s);
        
        System.out.println(d.toGMTString());
    }
}
