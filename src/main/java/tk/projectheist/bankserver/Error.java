package tk.projectheist.bankserver;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author Boris
 */
public class Error {

    
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty
    private Integer code;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
