package com.ardikars.jxpacket.mt940.domain;

import com.ardikars.common.util.NamedObject;
import com.sun.org.apache.regexp.internal.RE;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author jxpacket 2018/10/16
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
public class TransactionCode extends NamedObject<String, TransactionCode> {

    private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();

    public static final TransactionCode UNKNOWN = new TransactionCode("-1", "UNKONWN");

    public static final TransactionCode LBX = new TransactionCode("LBX", "Lock box");
    public static final TransactionCode NTRF = new TransactionCode("NTRF", "NTRF");

    private static final Map<String, TransactionCode> REGISTRY =
            new HashMap<>();

    public TransactionCode(String value, String name) {
        super(value, name);
    }

    public static void register(TransactionCode transactionCode) {
        if (LOCK.writeLock().tryLock()) {
            try {
                REGISTRY.put(transactionCode.getValue(), transactionCode);
            } finally {
                LOCK.writeLock().unlock();
            }
        }
    }

    public static TransactionCode valueOf(String transactionCode) {
        if (LOCK.readLock().tryLock()) {
            TransactionCode result = REGISTRY.get(transactionCode);
            if (result == null) {
                return UNKNOWN;
            }
            return result;
        }
        return UNKNOWN;
    }

    static {
        REGISTRY.put(LBX.getValue(), LBX);
        REGISTRY.put(NTRF.getValue(), NTRF);
    }

}
