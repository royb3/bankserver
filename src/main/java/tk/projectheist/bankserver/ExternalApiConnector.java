/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tk.projectheist.bankserver;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.SortedMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.grizzly.http.server.Session;
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
        hostnames.put("SKER", "http://145.24.222.112:8080/");
        hostnames.put("MLBI", "http://145.24.222.177:8080/");
        hostnames.put("COPO", "http://145.24.222.150:8080/");
    }
    
    public static ExternalApiConnector getInstance(){
        if(api == null) {
            api = new ExternalApiConnector();
        }
        return api;
    }
    
    public void login(String bankIdentity){
        try {
            connection = new HttpURLConnection(new URL(hostnames.get(bankIdentity)), Proxy.NO_PROXY);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setUseCaches(true);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONWriter jsonWriter = new JSONWriter(writer);
            jsonWriter
                    .object();  
        } catch (MalformedURLException ex) {
            Logger.getLogger(ExternalApiConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex){
            
        } catch (IOException ex) {
            
        }
        
    }
}
