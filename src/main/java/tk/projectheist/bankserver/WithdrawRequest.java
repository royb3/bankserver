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
    private Integer amount;
    @JsonProperty
    private String token;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
