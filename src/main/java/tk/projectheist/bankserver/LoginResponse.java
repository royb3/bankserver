/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tk.projectheist.bankserver;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonCreator;

/**
 *
 * @author roy
 */
public class LoginResponse implements Serializable{
    @JsonProperty
    private ErrorLogin error;
    @JsonProperty
    private Success success;

    public ErrorLogin getError() {
        return error;
    }

    public void setError(ErrorLogin error) {
        this.error = error;
    }

    public Success getSuccess() {
        return success;
    }

    public void setSuccess(Success success) {
        this.success = success;
    }

    public LoginResponse() {
        super();
    }

    @JsonCreator
    public LoginResponse(@JsonProperty("success") Success success, @JsonProperty("error") ErrorLogin error) {
        super();
        this.success = success;
        this.error = error;
    }
}
