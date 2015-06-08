/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tk.projectheist.bankserver;

import java.sql.SQLException;
import javax.security.auth.login.AccountLockedException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
    public long getSaldo(@PathParam("rekeningnummer") String rekeningnummer) throws SQLException {
        return (long)Database.getDatabase().getBalance(Integer.parseInt(rekeningnummer));
    }
    @POST
    @Path("/withdraw")
    @Consumes(MediaType.APPLICATION_JSON)
    public WithdrawResponse withdraw(WithdrawRequest request) throws SQLException{
        WithdrawResponse response = new WithdrawResponse();
        long max = Database.getDatabase().maximumWithdraw(Integer.parseInt(request.getTIBAN().substring(4)));
        if(request.getAmount() < max)
        {
            response.setTransactionNumber(Database.getDatabase().performWithdraw(Integer.parseInt(request.getTIBAN().substring(4)), request.getAmount()));
        }
        else
        {
            response.setResponse("Mag niet!");
            throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity(response).build());
        }

        return response;
    }
    @GET
    @Path("/auth/{rekeningnummer}/{kaartnummer}/{pincode}")
    public int authenticate(@PathParam("rekeningnummer") String rekeningnummer, @PathParam("kaartnummer") String kaartnummer, @PathParam("pincode") String pincode) throws Exception {
        int attempts_left = Database.getDatabase().authenticate(Integer.parseInt(rekeningnummer), Integer.parseInt(pincode), kaartnummer);
        if(attempts_left == -1){
            return -1;
        } else if (attempts_left > 0) {
            throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED).build());
        } else {
            throw new NotAuthorizedException(Response.status(403).entity(attempts_left).build());
        }
    }
    
    @GET
    @Path("/maximum_withdraw/{rekeningnummer}")
    public long maximumWithdraw(@PathParam("rekeningnummer") String rekeningnummer) throws SQLException{
        return Database.getDatabase().maximumWithdraw(Integer.parseInt(rekeningnummer));
    }
    
    
}
