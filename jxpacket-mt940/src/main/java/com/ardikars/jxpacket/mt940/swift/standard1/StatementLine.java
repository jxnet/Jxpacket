package com.ardikars.jxpacket.mt940.swift.standard1;

import com.ardikars.jxpacket.mt940.domain.CreditOrDebit;
import com.ardikars.jxpacket.mt940.domain.TransactionCode;
import com.ardikars.jxpacket.mt940.swift.Field;
import com.ardikars.jxpacket.mt940.util.Mt940Utils;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author jxpacket 2018/10/17
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Getter
@ToString
public class StatementLine implements Field {

    private final Date valueDate;
    private final CreditOrDebit creditOrDebit;
    private final BigDecimal amount;
    private final TransactionCode transactionCode;
    private final BigInteger transactionNumber;
    private final BigInteger bankReference;
    private final Collection<String> supplementaryDetails;

    public StatementLine(Date valueDate, CreditOrDebit creditOrDebit, BigDecimal amount, TransactionCode transactionCode, BigInteger transactionNumber, BigInteger bankReference, Collection<String> supplementaryDetails) {
        this.valueDate = valueDate;
        this.creditOrDebit = creditOrDebit;
        this.amount = amount;
        this.transactionCode = transactionCode;
        this.transactionNumber = transactionNumber;
        this.bankReference = bankReference;
        this.supplementaryDetails = supplementaryDetails;
    }

    public static class Builder {

        @lombok.Builder
        private static StatementLine newInstance(String valueDate, String creditOrDebit, String amount, String transactionCode, String transactionNumber, String bankReference, String supplementaryDetails) {
            Date parsedValueDate = Mt940Utils.parseStatementDate(valueDate);
            CreditOrDebit parsedCreditOrDebit = Mt940Utils.parseCreditOrDebit(creditOrDebit);
            BigDecimal parsedAmount = Mt940Utils.parseAmount(amount);
            TransactionCode parsedTransactionCode = null;
            BigInteger parsedTransactionNumber = Mt940Utils.parseTransactionNumber(transactionNumber);
            BigInteger parsedBankReference = Mt940Utils.parseBankReference(bankReference);
            List<String> parsedSupplementaryDetails = null;
            return new StatementLine(parsedValueDate, parsedCreditOrDebit, parsedAmount, parsedTransactionCode, parsedTransactionNumber, parsedBankReference, parsedSupplementaryDetails);
        }

    }

}
