package tk.projectheist.bankserver;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author Boris
 */
public class ErrorLogin extends Error  {

    int failedAttempts;

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

}
