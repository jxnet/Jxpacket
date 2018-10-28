package com.ardikars.jxpacket.mt940;

import lombok.Getter;
import lombok.ToString;

/**
 * @author middleware 2018/10/17
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Getter
@ToString
public class Bank implements Field {

    public static final String TAG = "1:";

    private final String bank;

    private Bank(String bank) {
        this.bank = bank;
    }

    public static class Builder {

        @lombok.Builder
        private static Bank newInstance(String bank) {
            return new Bank(bank);
        }

    }

}
