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

package com.ardikars.jxpacket.mt940.domain;

/**
 * @author jxpacket 2018/10/12
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
public enum CreditOrDebit {

    CREDIT {

        @Override
        public String toString() {
            return "C";
        }

    }, DEBIT {

        @Override
        public String toString() {
            return "D";
        }

    };

    /**
     * Parse credit or debit mark to {@link CreditOrDebit} object.
     * @param creditOrDebit credit or debit mark.
     * @return returns {@link CreditOrDebit}.
     */
    public static CreditOrDebit parse(String creditOrDebit) {
        if (creditOrDebit.equals("D")) {
            return DEBIT;
        } else if (creditOrDebit.equals("C")) {
            return CREDIT;
        } else {
            return null;
        }
    }

}
