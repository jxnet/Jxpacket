package com.ardikars.jxpacket.mt940.swift.standard2;

import com.ardikars.jxpacket.mt940.swift.Field;
import com.ardikars.jxpacket.mt940.util.Mt940Utils;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author jxpacket 2018/10/12
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Getter
@ToString
public class TransactionReferenceNumber implements Field {

    public static final String TAG = ":20";

    /**
     * Fixed length : 6 digit.
     * Format : YYMMDD
     * Mandatory.
     * Subfield 1.
     */
    private final Date statementDate;

    /**
     * Fixed length : 10 digit.
     * Bank's internal reference number.
     * Mandatory.
     * Subfield 2.
     */
    private final BigInteger referenceNumber;

    private TransactionReferenceNumber(Date statementDate, BigInteger referenceNumber) {
        this.statementDate = statementDate;
        this.referenceNumber = referenceNumber;
    }

    public static class Builder {

        @lombok.Builder
        private static TransactionReferenceNumber newInstance(String statementDate, String referenceNumber) {
            Date parsedStatementDate = Mt940Utils.parseStatementDate(statementDate);
            BigInteger parsedReferenceNumber = Mt940Utils.parseReferenceNumber(referenceNumber);
            return new TransactionReferenceNumber(parsedStatementDate, parsedReferenceNumber);
        }

    }

}
