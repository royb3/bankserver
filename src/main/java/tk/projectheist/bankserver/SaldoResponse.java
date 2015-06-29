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
 * @author joey
 */
public class SaldoResponse {

    private Error error;
    private SuccessSaldo success;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
    public SaldoResponse() {
        super();
    }

    public SuccessSaldo getSuccess() {
        return success;
    }

    public void setSuccess(SuccessSaldo success) {
        this.success = success;
    }

    @JsonCreator
    public SaldoResponse(@JsonProperty SuccessSaldo success, @JsonProperty Error error) {
        super();
        this.success = success;
        this.error = error;
    }

}
