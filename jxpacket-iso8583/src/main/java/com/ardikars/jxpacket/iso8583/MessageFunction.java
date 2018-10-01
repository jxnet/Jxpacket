package com.ardikars.jxpacket.iso8583;

import com.ardikars.common.annotation.Incubating;
import com.ardikars.common.util.NamedNumber;

import java.util.HashMap;
import java.util.Map;

@Incubating
public class MessageFunction extends NamedNumber<Byte, MessageFunction> {

    public static final MessageFunction REQUEST = new MessageFunction((byte) 0x030, "Request");
    public static final MessageFunction REQUEST_RESPONSE = new MessageFunction((byte) 0x031, "Request response");
    public static final MessageFunction ADVICE = new MessageFunction((byte) 0x032, "Advice");
    public static final MessageFunction ADVICE_RESPONSE = new MessageFunction((byte) 0x033, "Advice response");
    public static final MessageFunction NOTIFICATION = new MessageFunction((byte) 0x034, "Notification");
    public static final MessageFunction NOTIFICATION_ACKNOWLEDGEMENT = new MessageFunction((byte) 0x035, "Notification acknowledgement");

    /**
     * ISO8583:2003 only.
     */
    public static final MessageFunction INTRUCTION = new MessageFunction((byte) 0x036, "Instruction");
    public static final MessageFunction INTRUCTION_ACKNOWLEDGEMENT = new MessageFunction((byte) 0x037, "Instruction acknowledgement");

    public static final MessageFunction UNKNOWN = new MessageFunction((byte) -1, "Unknown");

    private static final Map<Byte, MessageFunction> registry =
            new HashMap<>();

    public MessageFunction(Byte value, String name) {
        super(value, name);
    }

    public static MessageFunction register(MessageFunction messageFunction) {
        registry.put(messageFunction.getValue(), messageFunction);
        return messageFunction;
    }

    public static MessageFunction valueOf(Byte value) {
        if (registry.containsKey(value)) {
            return registry.get(value);
        }
        return UNKNOWN;
    }

    static {
        registry.put(REQUEST.getValue(), REQUEST);
        registry.put(REQUEST_RESPONSE.getValue(), REQUEST_RESPONSE);
        registry.put(ADVICE.getValue(), ADVICE);
        registry.put(ADVICE_RESPONSE.getValue(), ADVICE_RESPONSE);
        registry.put(NOTIFICATION.getValue(), NOTIFICATION);
        registry.put(NOTIFICATION_ACKNOWLEDGEMENT.getValue(), NOTIFICATION_ACKNOWLEDGEMENT);
        registry.put(INTRUCTION.getValue(), INTRUCTION);
        registry.put(INTRUCTION_ACKNOWLEDGEMENT.getValue(), INTRUCTION_ACKNOWLEDGEMENT);
    }

}
