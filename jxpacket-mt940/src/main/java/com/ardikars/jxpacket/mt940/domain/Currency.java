package com.ardikars.jxpacket.mt940.domain;

import com.ardikars.common.util.NamedObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author jxpacket 2018/10/14
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
public class Currency extends NamedObject<String, Currency> {

    private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();

    public static final Currency UNKNOWN = new Currency("UNKNOWN", "Unknown currency");

    public static final Currency KRW = new Currency("KRW", "South Korean won");

    public static final Currency EUR = new Currency("EUR", "Euro");

    public static final Currency USD = new Currency("USD", "United States dollar");

    public static final Currency IDR = new Currency("IDR", "Indonesian rupiah");

    private static final Map<String, Currency> REGISTRY =
            new HashMap<>();

    public Currency(String value, String name) {
        super(value, name);
    }

    public static void register(Currency currency) {
        if (LOCK.writeLock().tryLock()) {
            try {
                REGISTRY.put(currency.getValue(), currency);
            } finally {
                LOCK.writeLock().unlock();
            }
        }
    }

    public static Currency valueOf(String currency) {
        if (LOCK.readLock().tryLock()) {
            Currency result = REGISTRY.get(currency);
            if (result == null) {
                return UNKNOWN;
            }
            return result;
        }
        return UNKNOWN;
    }

    static {
        REGISTRY.put(KRW.getValue(), KRW);
        REGISTRY.put(EUR.getValue(), EUR);
        REGISTRY.put(USD.getValue(), USD);
        REGISTRY.put(IDR.getValue(), IDR);
    }

}
