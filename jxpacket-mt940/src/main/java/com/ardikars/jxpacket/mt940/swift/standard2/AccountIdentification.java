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
import lombok.Getter;
import lombok.ToString;

/**
 * @author jxpacket 2018/10/12
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Getter
@ToString
public class AccountIdentification implements com.ardikars.jxpacket.mt940.AccountIdentification {

    public static final String TAG = ":25";

    /**
     * Format: RS35 + NBS format of account number.
     * Max 35 character.
     */
    private final String accountIdentification;

    private AccountIdentification(String accountIdentification) {
        this.accountIdentification = accountIdentification;
    }

    public static class Builder {

        private String accountIdentification;

        @lombok.Builder
        private static AccountIdentification newInstance(String accountIdentification) {
            return new AccountIdentification(Mt940Utils.parseAccountIdentification(accountIdentification));
        }

    }

}
