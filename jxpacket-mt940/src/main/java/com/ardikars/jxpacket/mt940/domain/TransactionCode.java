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

import com.ardikars.common.util.NamedObject;

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

    /**
     * Add new transaction code to registry.
     * @param transactionCode transaction code.
     */
    public static void register(TransactionCode transactionCode) {
        if (LOCK.writeLock().tryLock()) {
            try {
                REGISTRY.put(transactionCode.getValue(), transactionCode);
            } finally {
                LOCK.writeLock().unlock();
            }
        }
    }

    /**
     * Parse transaction code to {@link TransactionCode} object.
     * @param transactionCode transaction code.
     * @return returns {@link TransactionCode}.
     */
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
