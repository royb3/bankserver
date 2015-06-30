/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tk.projectheist.bankserver;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author roy
 */
public class WithdrawResponse {

    private Error error;
    private SuccessCode success;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public SuccessCode getSuccess() {
        return success;
    }

    public void setSuccess(SuccessCode success) {
        this.success = success;
    }

    @JsonCreator
    public WithdrawResponse(@JsonProperty("success") SuccessCode success, @JsonProperty("error") Error error) {
        super();
        this.success = success;
        this.error = error;
    }

}
