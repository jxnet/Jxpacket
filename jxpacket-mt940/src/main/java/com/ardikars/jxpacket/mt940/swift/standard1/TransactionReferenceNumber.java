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

import com.ardikars.jxpacket.mt940.util.Mt940Utils;

import java.math.BigInteger;
import java.util.Date;

import lombok.Getter;
import lombok.ToString;

/**
 * @author jxpacket 2018/10/12
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Getter
@ToString
public class TransactionReferenceNumber implements com.ardikars.jxpacket.mt940.TransactionReferenceNumber {

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
