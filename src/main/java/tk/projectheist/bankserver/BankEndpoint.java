/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tk.projectheist.bankserver;

import java.sql.SQLException;
import java.util.Random;
import java.util.regex.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.grizzly.Context;

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
        if (request.getToken() == "") {
            SuccessWithdraw success = new SuccessWithdraw();
            success.setCode("1337");
            WithdrawResponse response = new WithdrawResponse(success, null);
            return response;
        }
        return null;
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
        if (req.getCardId() == null || req.getCardId().equals("")) {
            Error error = new Error();
            error.setCode(10);
            error.setMessage("Het pasnummer is niet ontvangen!");
            LoginResponse response = new LoginResponse(new Success(), error);
            return response;
        }
        if (req.getPin() == null || req.getPin().equals("")) {
            Error error = new Error();
            error.setCode(11);
            error.setMessage("De pincode is niet ontvangen!");
            LoginResponse response = new LoginResponse(new Success(), error);
            return response;
        }
        if (req.getCardId().length() != 14) {
            Error error = new Error();
            error.setCode(12);
            error.setMessage("Het passnummer moet uit veertien CHARACTZERS bestaan!");
            LoginResponse response = new LoginResponse(new Success(), error);
            return response;
        }
        if (req.getPin().length() != 4) {
            Error error = new Error();
            error.setCode(13);
            error.setMessage("De pincode moet uit vier cijfers bestaan!");
            LoginResponse response = new LoginResponse(new Success(), error);
            return response;
        }
        if (Pattern.matches(".*[a-zA-Z]+.*", req.getPin()) == true) {
            Error error = new Error();
            error.setCode(13);
            error.setMessage("De pincode mag geen letters bevatten!");
            LoginResponse response = new LoginResponse(new Success(), error);
            return response;
        }
        try {
            int attempts_left = Database.getDatabase().authenticate(req.getCardId(), req.getPin());
            if (attempts_left == -1) {
                Success success = new Success();
                success.setToken(generateString(new Random(), "abcdefghijklmnopqrstuvwxyz0123456789", 25));
                LoginResponse response = new LoginResponse(success, null);
                return response;
            } else if (attempts_left == 0) {
                Error error = new Error();
                error.setCode(16);
                error.setMessage("De pas is geblokkeerd!");
                LoginResponse response = new LoginResponse(new Success(), error);
                return response;
            } else {
                ErrorLogin error = new ErrorLogin();
                error.setCode(15);
                error.setMessage("De pincode is verkeerd!");
                error.setFailedAttempts(3 - attempts_left);
                LoginResponse response = new LoginResponse(new Success(), error);
                return response;
            }
        } catch (SQLException e) {
            Error error = new Error();
            error.setCode(14);
            error.setMessage("Het pasnummer bestaat niet!");
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
