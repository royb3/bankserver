/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tk.projectheist.bankserver;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.SortedMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;
import org.glassfish.grizzly.http.server.Session;
import org.json.JSONObject;
import org.json.JSONWriter;
import sun.net.www.protocol.http.HttpURLConnection;
/**
 *
 * @author roy
 */
public class ExternalApiConnector {
    private static ExternalApiConnector api;
    private HttpURLConnection connection;
    
    private LinkedHashMap<String, String> hostnames = new LinkedHashMap<String, String>();
    
    public ExternalApiConnector(){
        hostnames.put("ILMG", "http://145.24.222.103:8080/");
        hostnames.put("ATMB", "http://145.24.222.217:8080/");
        hostnames.put("SKER", "http://145.24.222.112:80/");
        hostnames.put("MLBI", "http://145.24.222.177:8080/");
        hostnames.put("COPO", "http://145.24.222.150:8080/");
    }
    
    public static ExternalApiConnector getInstance(){
        if(api == null) {
            api = new ExternalApiConnector();
        }
        return api;
    }
    
    public LoginResponse login(String bankIdentifier, LoginRequest req){
        try {
            String query = String.format("cardId=%s&pin=%s", URLEncoder.encode(req.getCardId(), "UTF-8"), URLEncoder.encode(req.getPin(), "UTF-8"));
            connection = new HttpURLConnection(new URL(hostnames.get(bankIdentifier) + "login"), Proxy.NO_PROXY);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", MediaType.APPLICATION_FORM_URLENCODED);
            
            connection.setUseCaches(true);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.getOutputStream().write(query.getBytes());
            if(connection.getResponseCode() == 200){
                InputStream is = connection.getInputStream();
                String response = "";
                byte[] buffer = new byte[1024];
                while (is.available() > 0) {
                    int read = is.read(buffer);
                    for (int i = 0; i < read; i++) {
                        response += (char) buffer[i];
                    }
                }
                
                LoginResponse loginResponse = new ObjectMapper().readValue(response, LoginResponse.class);
                return loginResponse;
            }
        } catch (UnrecognizedPropertyException ex) {
            Error err = new Error();
            err.setCode(0);
            err.setMessage("Bank geeft antwoord dat niet aan de standaarden voldoet.");
            return new LoginResponse(new Success(), err);
        } catch (MalformedURLException ex) {
            ex.printStackTrace(System.out);
        } catch (ProtocolException ex){
            ex.printStackTrace(System.out);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
        Error err = new Error();
        err.setCode(6);
        err.setMessage("Fout tijdens inloggen bij externe bank.");
        return new LoginResponse(new Success(), err);
    }
    
    public WithdrawResponse withdraw(String bankIdentifier, WithdrawRequest req){
        try {
            String query = String.format("amount=%d", req.getAmount());
            connection = new HttpURLConnection(new URL(hostnames.get(bankIdentifier) + "withdraw"), Proxy.NO_PROXY);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", MediaType.APPLICATION_FORM_URLENCODED);
            connection.setRequestProperty("token", req.getToken());
            connection.setUseCaches(true);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.getOutputStream().write(query.getBytes());
            int rc = connection.getResponseCode();
            if(rc == 200){
                InputStream is = connection.getInputStream();
                String response = "";
                byte[] buffer = new byte[1024];
                while (is.available() > 0) {
                    int read = is.read(buffer);
                    for (int i = 0; i < read; i++) {
                        response += (char) buffer[i];
                    }
                }
                
                WithdrawResponse withdrawResponse = new ObjectMapper().readValue(response, WithdrawResponse.class);
                return withdrawResponse;
            }
        } catch (UnrecognizedPropertyException ex) {
            Error err = new Error();
            err.setCode(0);
            err.setMessage("Bank geeft antwoord dat niet aan de standaarden voldoet.");
            return new WithdrawResponse(new SuccessCode(), err);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
        Error err = new Error();
        err.setCode(6);
        err.setMessage("Fout tijdens inloggen bij externe bank.");
        return new WithdrawResponse(new SuccessCode(), err);
    }
    
    public LogoutResponse logout(String bankIdentifier, String token){
        try {
            connection = new HttpURLConnection(new URL(hostnames.get(bankIdentifier) + "logout"), Proxy.NO_PROXY);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("token", token);
            connection.setUseCaches(true);
            connection.setDoInput(true);
            int rc = connection.getResponseCode();
            if(rc == 200){
                InputStream is = connection.getInputStream();
                String response = "";
                byte[] buffer = new byte[1024];
                while (is.available() > 0) {
                    int read = is.read(buffer);
                    for (int i = 0; i < read; i++) {
                        response += (char) buffer[i];
                    }
                }
                
                LogoutResponse withdrawResponse = new ObjectMapper().readValue(response, LogoutResponse.class);
                return withdrawResponse;
            }
        } catch (UnrecognizedPropertyException ex) {
            Error err = new Error();
            err.setCode(0);
            err.setMessage("Bank geeft antwoord dat niet aan de standaarden voldoet.");
            return new LogoutResponse(new SuccessCode(), err);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
        Error err = new Error();
        err.setCode(6);
        err.setMessage("Fout tijdens uitloggen bij externe bank.");
        return new LogoutResponse(new SuccessCode(), err);
    }
}
