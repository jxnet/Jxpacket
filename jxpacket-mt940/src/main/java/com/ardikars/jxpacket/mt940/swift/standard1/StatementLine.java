/**
 * Copyright (C) 2017-2018  Ardika Rommy Sanjaya <contact@ardikars.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ardikars.jxpacket.mt940.swift.standard1;

import com.ardikars.jxpacket.mt940.domain.CreditOrDebit;
import com.ardikars.jxpacket.mt940.domain.TransactionCode;
import com.ardikars.jxpacket.mt940.util.Mt940Utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.ToString;

/**
 * @author jxpacket 2018/10/17
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Getter
@ToString
public class StatementLine implements com.ardikars.jxpacket.mt940.StatementLine {

    public static final String TAG = ":61";

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
            TransactionCode parsedTransactionCode = Mt940Utils.parseTransactionCode(transactionCode);

            /**
             * Non standart
             */
            BigInteger parsedTransactionNumber = BigInteger.ZERO;
            BigInteger parsedBankReference = BigInteger.ZERO;
            List<String> parsedSupplementaryDetails = new ArrayList<>();
            if (transactionNumber != null && !transactionNumber.isEmpty()) {
                parsedTransactionNumber = Mt940Utils.parseTransactionNumber(transactionNumber);
            }
            if (bankReference != null && !bankReference.isEmpty()) {
                parsedBankReference = Mt940Utils.parseBankReference(bankReference);
            }
            if (supplementaryDetails != null && !supplementaryDetails.isEmpty()) {
                parsedSupplementaryDetails = Mt940Utils.parseSupplementaryDetails(supplementaryDetails);
            }
            return new StatementLine(parsedValueDate, parsedCreditOrDebit, parsedAmount, parsedTransactionCode, parsedTransactionNumber, parsedBankReference, parsedSupplementaryDetails);
        }

    }

}
