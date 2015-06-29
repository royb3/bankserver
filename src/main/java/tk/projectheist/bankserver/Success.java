package tk.projectheist.bankserver;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author Boris
 */
public class Success implements Serializable{

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
