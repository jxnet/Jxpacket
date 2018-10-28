package com.ardikars.jxpacket.mt940.swift.standard2;

import com.ardikars.jxpacket.mt940.domain.CreditOrDebit;
import com.ardikars.jxpacket.mt940.domain.Currency;
import com.ardikars.jxpacket.mt940.util.Mt940Utils;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jxpacket 2018/10/16
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Getter
@ToString
public class ClosingBalance implements com.ardikars.jxpacket.mt940.ClosingBalance {

    public static final String TAG = ":62";

    private final CreditOrDebit creditOrDebit;
    private final Date statementDate;
    private final Currency currency;
    private final BigDecimal amount;

    private ClosingBalance(CreditOrDebit creditOrDebit, Date statementDate, Currency currency, BigDecimal amount) {
        this.creditOrDebit = creditOrDebit;
        this.statementDate = statementDate;
        this.currency = currency;
        this.amount = amount;
    }

    public static class Builder {

        @lombok.Builder
        private static ClosingBalance newInstance(String creditOrDebitMark, String statementDate, String currency, String amount) {
            CreditOrDebit parsedCreditOrDebit = Mt940Utils.parseCreditOrDebit(creditOrDebitMark);
            Date parsedStatementDate = Mt940Utils.parseStatementDate(statementDate);
            Currency parsedCurrency = Mt940Utils.parseCurrency(currency);
            BigDecimal parsedAmount = Mt940Utils.parseAmount(amount);
            return new ClosingBalance(parsedCreditOrDebit, parsedStatementDate, parsedCurrency, parsedAmount);
        }

    }

}
