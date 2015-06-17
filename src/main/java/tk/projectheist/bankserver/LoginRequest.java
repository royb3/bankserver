/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tk.projectheist.bankserver;

import org.codehaus.jackson.annotate.JsonProperty;
/**
 *
 * @author roy
 */
public class LoginRequest {
    @JsonProperty
    private String pin;
    @JsonProperty
    private String cardId;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
    
}
