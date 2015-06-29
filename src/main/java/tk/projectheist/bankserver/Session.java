/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tk.projectheist.bankserver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javafx.util.Duration;

/**
 *
 * @author roy
 */
public class Session {
    private boolean done;
    private LocalDateTime expirationDate;
    private String endPoint;
    private String token;
    private String cardId;
    
    public Session(String endPoint, String token, String cardId){
        expirationDate = LocalDateTime.now().plus(30, ChronoUnit.MINUTES);
        this.endPoint = endPoint;
        this.cardId = cardId;
        this.token = token;
        this.done = false;
    }
    
    public Session(boolean done,String endPoint, String cardId, LocalDateTime expiration) {
        this.expirationDate = expiration;
        this.endPoint = endPoint;
        this.cardId = cardId;
        this.done = done;
    }
    

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
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
    
    public String getCardId(){
        return cardId;
    }
    
    public void setCardId(String cardId){
        this.cardId = cardId;
    }
    
    public boolean expired(){
        return(LocalDateTime.now().isAfter(expirationDate));
    }
}
