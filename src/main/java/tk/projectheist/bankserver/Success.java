package tk.projectheist.bankserver;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Boris
 */
public class Success {

    @JsonProperty
    private String token;
    @JsonProperty
    private int error;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }
    
    
}
