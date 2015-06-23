/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tk.projectheist.bankserver;

import java.sql.SQLException;
import java.util.Random;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

/**
 *
 * @author roy
 */
@Path("/")
public class BankEndpoint {

    @GET
    @Path("/balance/{rekeningnummer}")
    public long getSaldo(@PathParam("rekeningnummer") String rekeningnummer) throws SQLException {
        return (long) Database.getDatabase().getBalance(Integer.parseInt(rekeningnummer));
    }

    @POST
    @Path("/withdraw")
    @Consumes(MediaType.APPLICATION_JSON)
    public WithdrawResponse withdraw(WithdrawRequest request) throws SQLException {
        WithdrawResponse response = new WithdrawResponse();
        long max = Database.getDatabase().maximumWithdraw(Integer.parseInt(request.getTIBAN().substring(4)));
        if (request.getAmount() < max) {
            response.setTransactionNumber(Database.getDatabase().performWithdraw(Integer.parseInt(request.getTIBAN().substring(4)), request.getAmount()));
        } else {
            response.setResponse("Mag niet!");
            throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity(response).build());
        }
        System.out.println("test");
        return response;
    }

    @GET
    @Path("/auth/{rekeningnummer}/{pincode}")
    public int authenticate(@PathParam("rekeningnummer") String rekeningnummer, @PathParam("pincode") String pincode) throws Exception {
        int attemps_left = Database.getDatabase().authenticate(rekeningnummer, pincode);
        if (attemps_left == -1) {
            return -1;
        } else if (attemps_left > 0) {
            throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED).build());
        } else {
            throw new NotAuthorizedException(Response.status(403).entity(attemps_left).build());
        }
    }

    @GET
    @Path("/maximum_withdraw/{rekeningnummer}")
    public long maximumWithdraw(@PathParam("rekeningnummer") String rekeningnummer) throws SQLException {
        return Database.getDatabase().maximumWithdraw(Integer.parseInt(rekeningnummer));
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public LoginResponse login(LoginRequest req) throws Exception {
        int attempts_left = Database.getDatabase().authenticate(req.getCardId(), req.getPin());
        if (attempts_left == -1) {
            Success success = new Success();
            success.setToken(generateString(new Random(), "abcdefghijklmnopqrstuvwxyz0123456789", 25));
            LoginResponse response = new LoginResponse(success, null);
            return response;
        }
        else if (attempts_left == 0) {
            Error error = new Error();
            error.setCode(16);
            error.setMessage("De pas is geblokkeerd!");
            LoginResponse response = new LoginResponse(new Success(), error);
            return response;
        }
        else{
            Error error = new Error();
            error.setCode(2);
            error.setMessage("De pincode is verkeerd!");
            LoginResponse response = new LoginResponse(new Success(), error);
            return response;
        }
    }

    public static String generateString(Random rng, String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

}
