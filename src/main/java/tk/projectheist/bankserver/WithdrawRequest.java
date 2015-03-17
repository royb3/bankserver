/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tk.projectheist.bankserver;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author roy
 */
public class WithdrawRequest {
    @JsonProperty
    private String IBAN;
    @JsonProperty
    private long amount;
    
    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
