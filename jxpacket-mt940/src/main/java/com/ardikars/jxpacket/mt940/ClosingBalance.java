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

package com.ardikars.jxpacket.mt940;

import com.ardikars.jxpacket.mt940.domain.CreditOrDebit;
import com.ardikars.jxpacket.mt940.domain.Currency;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jxpacket 2018/10/12
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
public interface ClosingBalance extends Field {

    /**
     * Get credit or debit mark.
     * @return returns {@link CreditOrDebit}.
     */
    CreditOrDebit getCreditOrDebit();

    /**
     * Get statement date.
     * @return returns statement date.
     */
    Date getStatementDate();

    /**
     * Get currency.
     * @return returns {@link Currency}.
     */
    Currency getCurrency();

    /**
     * Get amount.
     * @return returns amount.
     */
    BigDecimal getAmount();

}
