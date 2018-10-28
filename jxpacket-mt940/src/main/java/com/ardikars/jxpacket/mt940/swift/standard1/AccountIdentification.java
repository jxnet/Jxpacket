package com.ardikars.jxpacket.mt940.swift.standard1;

import com.ardikars.jxpacket.mt940.util.Mt940Utils;
import lombok.Getter;
import lombok.ToString;

/**
 * @author jxpacket 2018/10/12
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

    private AccountIdentification(String accountIdentification) {
        this.accountIdentification = accountIdentification;
    }

    public static class Builder {

        private String accountIdentification;

        @lombok.Builder
        private static AccountIdentification newInstance(String accountIdentification) {
            return new AccountIdentification(Mt940Utils.parseAccountIdentification(accountIdentification));
        }

    }

}
