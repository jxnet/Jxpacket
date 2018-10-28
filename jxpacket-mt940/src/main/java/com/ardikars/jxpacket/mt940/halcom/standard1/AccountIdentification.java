package com.ardikars.jxpacket.mt940.halcom.standard1;

import com.ardikars.jxpacket.mt940.util.Mt940Utils;
import lombok.Getter;
import lombok.ToString;

/**
 * @author middleware 2018/10/18
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Getter
@ToString
public class AccountIdentification implements com.ardikars.jxpacket.mt940.AccountIdentification {

    public static final String TAG = ":25";

    /**
     * Format: RS35 + NBS format of account number.
     * Max 35 character.
     */
    private final String accountIdentification;

    public AccountIdentification(String accountIdentification) {
        this.accountIdentification = accountIdentification;
    }

    public static class Builder {

        @lombok.Builder
        private static AccountIdentification newInstance(String accountIdentification) {
            return new AccountIdentification(Mt940Utils.parseAccountIdentification(accountIdentification));
        }

    }

}
