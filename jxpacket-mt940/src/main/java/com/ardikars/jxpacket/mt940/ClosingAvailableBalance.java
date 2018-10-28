package com.ardikars.jxpacket.mt940;

import com.ardikars.jxpacket.mt940.domain.CreditOrDebit;
import com.ardikars.jxpacket.mt940.domain.Currency;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jxpacket 2018/10/12
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
public interface ClosingAvailableBalance extends Field {

    CreditOrDebit getCreditOrDebit();

    Date getStatementDate();

    Currency getCurrency();

    BigDecimal getAmount();

}
