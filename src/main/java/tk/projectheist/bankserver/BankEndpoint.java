/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tk.projectheist.bankserver;

import java.sql.SQLException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author roy
 */
@Path("/")
public class BankEndpoint {
    @GET
    @Path("/balance/{rekeningnummer}")
    public long getSaldo(@PathParam("rekeningnummer") String rekeningnummer){
        return (long)889749437;
    }
    @POST
    @Path("/withdraw")
    @Consumes(MediaType.APPLICATION_JSON)
    public WithdrawResponse withdraw(WithdrawRequest request) throws SQLException{
        WithdrawResponse response = new WithdrawResponse();
        if(request.getAmount() < Database.getDatabase().getBalance(Integer.parseInt(request.getIBAN())))
        {
            response.setResponse("Vooruit dan maar...");
        }
        else
        {
            response.setResponse("Mag niet!");
            throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity(response).build());
        }
        return response;
    }
    
}
