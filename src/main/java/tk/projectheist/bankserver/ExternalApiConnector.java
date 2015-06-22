/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tk.projectheist.bankserver;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.SortedMap;
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
    }
    
    private void connect(){
        
    }
    
    public static ExternalApiConnector getInstance(){
        if(api == null) {
            api = new ExternalApiConnector();
        }
        return api;
    }
}
