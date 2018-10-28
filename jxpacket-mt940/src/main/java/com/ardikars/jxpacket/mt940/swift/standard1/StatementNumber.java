package com.ardikars.jxpacket.mt940.swift.standard1;

import com.ardikars.jxpacket.mt940.util.Mt940Utils;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;

/**
 *
 * Mandatory.
 *
 * @author jxpacket 2018/10/12
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Getter
@ToString
public class StatementNumber implements com.ardikars.jxpacket.mt940.StatementNumber {

    public static final String TAG = ":28C";

    /**
     * Fixed length : 5 digit.
     * Mandatory.
     */
    private final BigInteger statementNumber;

    private StatementNumber(BigInteger statementNumber) {
        this.statementNumber = statementNumber;
    }

    public static class Builder {

        @lombok.Builder
        private static StatementNumber newInstance(String statementNumber) {
            BigInteger parsedStatementNumber = Mt940Utils.parseStatementNumber(statementNumber);
            return new StatementNumber(parsedStatementNumber);
        }

    }

}
