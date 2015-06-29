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
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.Request;

/**
 *
 * @author roy
 */
@Path("/")
public class BankEndpoint {

    @Context
    Request request;

    @POST
    @Path("/balance/")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public SaldoResponse getSaldo(MultivaluedMap<String, String> formParams) throws SQLException {
        Error error = new Error();
        SuccessSaldo success = new SuccessSaldo();

        if (!formParams.containsKey("amount")) {
            error.setCode(30);
            error.setMessage("Geen amount ontvangen!");
        } else {
            String token = request.getHeader("token");
            Session session = Database.getDatabase().getSession(token);
            if (session == null) {
                error.setCode(4);
                error.setMessage("Token nooit uitgegeven.");
            } else if (session.expired() || session.isDone()) {
                error.setCode(4);
                error.setMessage("Token is verlopen of er is uitgelogd.");
            } else {
                long saldo = Database.getDatabase().getBalance(Integer.parseInt(session.getCardId().substring(4)));
                success.setSaldo(saldo);
                
            }

        }
        return new SaldoResponse(success, error);
    }
    
    @POST
    @Path("/withdraw")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public WithdrawResponse withdraw(MultivaluedMap<String, String> formParams) throws SQLException {
        Error error = new Error();
        SuccessWithdraw success = new SuccessWithdraw();

        if (!formParams.containsKey("amount")) {
            error.setCode(30);
            error.setMessage("Geen amount ontvangen!");
        } else {

            WithdrawRequest req = new WithdrawRequest();
            req.setAmount(Integer.parseInt(formParams.getFirst("amount")));
            req.setToken(request.getHeader("token"));

            if (req.getToken() == null || req.getToken().equals("")) {
                error.setCode(4);
            } else {
                Session session = Database.getDatabase().getSession(req.getToken());
                if (session == null) {
                    error.setCode(4);
                    error.setMessage("Token nooit uitgegeven.");
                } else if (session.expired() || session.isDone()) {
                    error.setCode(4);
                    error.setMessage("Token is verlopen of er is uitgelogd.");
                } else if (req.getAmount() < maximumWithdraw(session.getCardId().substring(4))) {
                    Database.getDatabase().performWithdraw(Integer.parseInt(session.getCardId().substring(4)), req.getAmount());
                    success.setCode(1337);
                } else {
                    error.setCode(32);
                    error.setMessage("Er is te weinig saldo voor deze transactie!");
                }
            }
        }
        return new WithdrawResponse(success, error);
    }

    public long maximumWithdraw(String rekeningnummer) throws SQLException {
        return Database.getDatabase().maximumWithdraw(Integer.parseInt(rekeningnummer));
    }

    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public LogoutResponse logout(MultivaluedMap<String, String> formParams) throws Exception {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setToken(request.getHeader("token"));
        if (logoutRequest.getToken() == null || logoutRequest.equals("")) {
            Error error = new Error();
            error.setCode(201);
            error.setMessage("Geen token meegegeven!");
            LogoutResponse response = new LogoutResponse(new SuccessWithdraw(), error);
            return response;
        } else {
            SuccessWithdraw success = new SuccessWithdraw();
            success.setCode(137);
            LogoutResponse response = new LogoutResponse(success, null);
            return response;
        }

    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public LoginResponse login(MultivaluedMap<String, String> formParams) throws Exception {
        LoginRequest req = new LoginRequest();
        req.setCardId(formParams.getFirst("cardId"));
        req.setPin(formParams.getFirst("pin"));
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
            error.setMessage("Het passnummer moet uit veertien characters bestaan!");
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

                Session s = new Session(request.getRemoteAddr(), success.getToken(), req.getCardId());

                Database.getDatabase().StoreSession(s);
                LoginResponse response = new LoginResponse(success, new Error());
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
                error.setMessage(String.format("De pincode is verkeerd! U heeft %d pogingen over.", attempts_left));
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
