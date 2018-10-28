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

package com.ardikars.jxpacket.mt940.swift.standard2;

import com.ardikars.jxpacket.mt940.util.Mt940Utils;

import java.math.BigInteger;

import lombok.Getter;
import lombok.ToString;

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
