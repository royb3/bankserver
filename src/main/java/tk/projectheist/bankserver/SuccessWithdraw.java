package tk.projectheist.bankserver;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author Boris
 */
public class SuccessWithdraw {

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty
    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
