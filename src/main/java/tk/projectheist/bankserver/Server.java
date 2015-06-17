package tk.projectheist.bankserver;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import java.io.IOException;
import java.net.URI;
import org.glassfish.grizzly.Grizzly;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author roy
 */
public class Server {
    public static void main(String[] args){
        new Server();
        
    }
    
    public Server(){
        HttpServer server = initWebServer();
        try{
            ServerConfiguration config =  server.getServerConfiguration();
            server.start();
            
            while(true)
            {
                Thread.sleep(1000);
            }
        }
        catch(IOException e)
        {
            
        }
        catch(InterruptedException e)
        {
            
        }
    }
    
    public HttpServer initWebServer(){
        ResourceConfig config = new ResourceConfig(BankEndpoint.class);
        config.register(JacksonJaxbJsonProvider.class);
        return GrizzlyHttpServerFactory.createHttpServer(URI.create("http://0.0.0.0:80"), config);
    }
}
