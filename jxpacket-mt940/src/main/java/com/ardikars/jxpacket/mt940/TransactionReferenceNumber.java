package com.ardikars.jxpacket.mt940;

import java.math.BigInteger;

/**
 *
 * Transaction Reference Number.
 *
 * Mandatory.
 *
 * @author jxpacket 2018/10/12
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
public interface TransactionReferenceNumber extends Field {

    BigInteger getReferenceNumber();

}
