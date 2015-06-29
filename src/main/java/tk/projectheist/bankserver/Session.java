/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tk.projectheist.bankserver;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javafx.util.Duration;

/**
 *
 * @author roy
 */
public class Session {
    private boolean done;
    private LocalDate expirationDate;
    private String endPoint;
    private String token;
    private String cardId;
    
    public Session(String endPoint, String token){
        expirationDate = LocalDate.now().plus(30, ChronoUnit.MINUTES);
        this.endPoint = endPoint;
        this.token = token;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    
    
}
