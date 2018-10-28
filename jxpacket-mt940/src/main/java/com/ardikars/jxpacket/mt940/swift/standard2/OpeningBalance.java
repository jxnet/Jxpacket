package com.ardikars.jxpacket.mt940.swift.standard2;

import com.ardikars.jxpacket.mt940.domain.CreditOrDebit;
import com.ardikars.jxpacket.mt940.domain.Currency;
import com.ardikars.jxpacket.mt940.util.Mt940Utils;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jxpacket 2018/10/12
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Getter
@ToString
public class OpeningBalance implements com.ardikars.jxpacket.mt940.OpeningBalance {

    public static final String TAG = ":60";

    /**
     * C/D
     */
    private final CreditOrDebit creditOrDebit;

    /**
     * Fixed length : 6 digit.
     * Format : YYMMDD
     * Mandatory.
     * Subfield 1.
     */
    private final Date statementDate;

    /**
     * Fixed length : 3 digit.
     */
    private final Currency currency;

    /**
     * Amount
     */
    private final BigDecimal amount;

    private OpeningBalance(CreditOrDebit creditOrDebit, Date statementDate, Currency currency, BigDecimal amount) {
        this.creditOrDebit = creditOrDebit;
        this.statementDate = statementDate;
        this.currency = currency;
        this.amount = amount;
    }

    public static class Builder {

        @lombok.Builder
        private static OpeningBalance newInstance(String creditOrDebitMark, String statementDate, String currency, String amount) {
            CreditOrDebit parsedCreditOrDebit = Mt940Utils.parseCreditOrDebit(creditOrDebitMark);
            Date parsedStatementDate = Mt940Utils.parseStatementDate(statementDate);
            Currency parsedCurrency = Mt940Utils.parseCurrency(currency);
            BigDecimal parsedAmount = Mt940Utils.parseAmount(amount);
            return new OpeningBalance(parsedCreditOrDebit, parsedStatementDate, parsedCurrency, parsedAmount);
        }

    }

}
