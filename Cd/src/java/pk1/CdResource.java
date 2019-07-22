package pk1;

import pk3.CreditCard;
import pk3.Client;
import pk2.DataBase;
import java.util.Vector;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;

@Path("cd")
public class CdResource {

    @Context
    private UriInfo context;

    public CdResource() {
    }

    // Pour verifie le host
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response get() {
        return  Response.accepted().entity("true").build();
    }
    
    // pour valider le user & paswd (Login)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Boolean post(JAXBElement<Client> clientJaxb){
        Client client= clientJaxb.getValue();
        if( DataBase.isExistedClient(client))
           return true;
       return  false;
    }
    
    // pour insert un nouvel Client sur la BD (SignUp)
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Boolean put(JAXBElement<Client> clientJaxb){
        Client client= clientJaxb.getValue();
        if(DataBase.insertClient(client))
            return true;
        return false;
    }
    
    
    @GET
    @Path("Client/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Client getClient(@PathParam("id") long id){
        return DataBase.getClient(id);
    }
    
    
    @GET
    @Path("Client/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Vector<Client> getAllClient(){
        return DataBase.getAllClient();
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CreditCard getCreditCard(@PathParam("id") long id){
        return DataBase.getCreditCard(id);
    }
    
    
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Vector<CreditCard> getAll(){
        return DataBase.getAllCard();
    }
    
    @POST
    @Path("valid")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Boolean isExested1(JAXBElement<CreditCard> creditCardJaxb){
        CreditCard creditCard = creditCardJaxb.getValue();
       if( DataBase.isExistedCard(creditCard))
           return true;
       return  false;
    }
    
    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Boolean Create(JAXBElement<CreditCard> creditCardJaxb){
        CreditCard creditCard = creditCardJaxb.getValue();
        if(creditCard.isValidate()){
            if(DataBase.insertCreditCard(creditCard))
                return true;
        }
        return false;
    }
}
