package com.ardikars.jxpacket.mt940.swift.standard2;

import com.ardikars.jxpacket.mt940.domain.CreditOrDebit;
import com.ardikars.jxpacket.mt940.domain.TransactionCode;
import com.ardikars.jxpacket.mt940.util.Mt940Utils;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author jxpacket 2018/10/17
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Getter
@ToString
public class StatementLine implements com.ardikars.jxpacket.mt940.StatementLine {

    public static final String TAG = ":61";

    private final Date valueDate;
    private final Date transactionDate;
    private final CreditOrDebit creditOrDebit;
    private final BigDecimal amount;
    private final TransactionCode transactionCode;
    private final BigInteger transactionNumber;

    private StatementLine(Date valueDate, Date transactionDate, CreditOrDebit creditOrDebit, BigDecimal amount, TransactionCode transactionCode, BigInteger transactionNumber) {
        this.valueDate = valueDate;
        this.transactionDate = transactionDate;
        this.creditOrDebit = creditOrDebit;
        this.amount = amount;
        this.transactionCode = transactionCode;
        this.transactionNumber = transactionNumber;
    }

    public static class Builder {

        @lombok.Builder
        private static StatementLine newInstance(String valueDate, String transactionDate, String creditOrDebit, String amount, String transactionNumber) {
            Date parsedValueDate = Mt940Utils.parseStatementDate(valueDate);
            Date parsedTransactionDate = Mt940Utils.parseTransactionDate(transactionDate);
            CreditOrDebit parsedCreditOrDebit = Mt940Utils.parseCreditOrDebit(creditOrDebit);
            BigDecimal parsedAmount = Mt940Utils.parseAmount(amount);
            TransactionCode parsedTransactionCode = new TransactionCode("NTRF", "NTRF");
            BigInteger parsedTransactionNumber = Mt940Utils.parseTransactionNumber(transactionNumber);
            return new StatementLine(parsedValueDate, parsedTransactionDate, parsedCreditOrDebit, parsedAmount, parsedTransactionCode, parsedTransactionNumber);
        }

    }

}
