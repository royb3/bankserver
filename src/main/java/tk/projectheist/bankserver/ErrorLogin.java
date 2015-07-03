package tk.projectheist.bankserver;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author Boris
 */
public class ErrorLogin extends Error {

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty
    Integer failedAttempts;

    public Integer getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(Integer failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

}
