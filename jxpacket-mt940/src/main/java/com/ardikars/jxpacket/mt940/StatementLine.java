package com.ardikars.jxpacket.mt940;

import com.ardikars.jxpacket.mt940.domain.CreditOrDebit;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author jxpacket 2018/10/12
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
public interface StatementLine extends Field {

    Date getValueDate();
    CreditOrDebit getCreditOrDebit();
    BigDecimal getAmount();
    BigInteger getTransactionNumber();

}
