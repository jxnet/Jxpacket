package com.ardikars.jxpacket.iso8583.dataelement.transaction;

import com.ardikars.common.util.NamedNumber;

/**
 * @author jxpacket 2018/10/03
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
public class Transaction extends NamedNumber<Short, Transaction> {

    protected Transaction(Short value, String name) {
        super(value, name);
    }
}
