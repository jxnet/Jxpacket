package com.ardikars.jxpacket.mt940.domain;

/**
 * @author jxpacket 2018/10/12
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
public enum CreditOrDebit {

    CREDIT {

        @Override
        public String toString() {
            return "C";
        }

    }, DEBIT {

        @Override
        public String toString() {
            return "D";
        }

    };

    public static CreditOrDebit parse(String creditOrDebit) {
        if (creditOrDebit.equals("D")) {
            return DEBIT;
        } else if (creditOrDebit.equals("C")) {
            return CREDIT;
        } else {
            return null;
        }
    }

}
