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
public class WithdrawResponse{
    @JsonProperty
    private String response;
    @JsonProperty
    private long transactionNumber;
    
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public long getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(long transactionNumber) {
        this.transactionNumber = transactionNumber;
    }
    
}
