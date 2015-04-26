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
public class WithdrawRequest {
    @JsonProperty
    private String TIBAN;
    @JsonProperty
    private long amount;
    
    public String getTIBAN() {
        return TIBAN;
    }

    public void setIBAN(String TIBAN) {
        this.TIBAN = TIBAN;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
