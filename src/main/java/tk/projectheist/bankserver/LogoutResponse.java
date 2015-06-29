/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tk.projectheist.bankserver;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Boris
 */
public class LogoutResponse {
    
    private Error error;
    private SuccessWithdraw success;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public SuccessWithdraw getSuccess() {
        return success;
    }

    public void setSuccess(SuccessWithdraw success) {
        this.success = success;
    }

    public LogoutResponse(@JsonProperty SuccessWithdraw succes,@JsonProperty Error error) {
        super();
        this.success = success;
        this.error = error;
    }
}
