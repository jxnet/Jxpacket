package com.ardikars.jxpacket.iso8583;

import com.ardikars.common.annotation.Incubating;
import com.ardikars.common.util.NamedNumber;

import java.util.HashMap;
import java.util.Map;

@Incubating
public class MessageClass extends NamedNumber<Byte, MessageClass> {

    public static final MessageClass AUTHORIZATION = new MessageClass((byte) 0x31, "Authorization");
    public static final MessageClass FINANCIAL = new MessageClass((byte) 0x32, "Financial");
    public static final MessageClass FILE_ACTIONS = new MessageClass((byte) 0x33, "File actions");
    public static final MessageClass REVERSAL_AND_CHARGEBACK = new MessageClass((byte) 0x34, "Reversal and chargeback");
    public static final MessageClass RECONCILIATION = new MessageClass((byte) 0x35, "Reconciliation");
    public static final MessageClass ADMINISTRATIVE = new MessageClass((byte) 0x36, "Administrative");
    public static final MessageClass FEE_COLLECTION = new MessageClass((byte) 0x37, "Fee collection");
    public static final MessageClass NETWORK_MANAGEMENT = new MessageClass((byte) 0x38, "Network management");
    public static final MessageClass UNKNOWN = new MessageClass((byte) -1, "Unknown");

    private static final Map<Byte, MessageClass> registry =
            new HashMap<>();

    public MessageClass(Byte value, String name) {
        super(value, name);
    }

    public static MessageClass register(MessageClass messageClass) {
        registry.put(messageClass.getValue(), messageClass);
        return messageClass;
    }

    public static MessageClass valueOf(Byte value) {
        if (registry.containsKey(value)) {
            return registry.get(value);
        }
        return UNKNOWN;
    }

    static {
        registry.put(AUTHORIZATION.getValue(), AUTHORIZATION);
        registry.put(FINANCIAL.getValue(), FINANCIAL);
        registry.put(FILE_ACTIONS.getValue(), FILE_ACTIONS);
        registry.put(REVERSAL_AND_CHARGEBACK.getValue(), REVERSAL_AND_CHARGEBACK);
        registry.put(RECONCILIATION.getValue(), RECONCILIATION);
        registry.put(ADMINISTRATIVE.getValue(), ADMINISTRATIVE);
        registry.put(FEE_COLLECTION.getValue(), FEE_COLLECTION);
        registry.put(NETWORK_MANAGEMENT.getValue(), NETWORK_MANAGEMENT);
    }

}
