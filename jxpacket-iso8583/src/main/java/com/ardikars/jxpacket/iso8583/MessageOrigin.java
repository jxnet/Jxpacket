package com.ardikars.jxpacket.iso8583;

import com.ardikars.common.annotation.Incubating;
import com.ardikars.common.util.NamedNumber;

import java.util.HashMap;
import java.util.Map;

@Incubating
public class MessageOrigin extends NamedNumber<Byte, MessageOrigin> {

    public static final MessageOrigin ACQUIRER = new MessageOrigin((byte) 0x30, "Acquirer");
    public static final MessageOrigin ACQUIRER_REPEAT = new MessageOrigin((byte) 0x31, "Acquirer repeat");
    public static final MessageOrigin ISSUER = new MessageOrigin((byte) 0x32, "Issuer");
    public static final MessageOrigin ISSUER_REPEAT = new MessageOrigin((byte) 0x33, "Issuer repeat");
    public static final MessageOrigin OTHER = new MessageOrigin((byte) 0x34, "Other");
    public static final MessageOrigin OTHER_REPEAT = new MessageOrigin((byte) 0x35, "Other repeat");
    public static final MessageOrigin UNKNOWN = new MessageOrigin((byte) -1, "Unknown");

    private static final Map<Byte, MessageOrigin> register =
            new HashMap<>();

    public MessageOrigin(Byte value, String name) {
        super(value, name);
    }

    public static MessageOrigin register(MessageOrigin messageOrigin) {
        register.put(messageOrigin.getValue(), messageOrigin);
        return messageOrigin;
    }

    public static MessageOrigin valueOf(Byte value) {
        if (register.containsKey(value)) {
            return register.get(value);
        }
        return UNKNOWN;
    }

    static {
        register.put(ACQUIRER.getValue(), ACQUIRER);
        register.put(ACQUIRER_REPEAT.getValue(), ACQUIRER_REPEAT);
        register.put(ISSUER.getValue(), ISSUER);
        register.put(ISSUER_REPEAT.getValue(), ISSUER_REPEAT);
        register.put(OTHER.getValue(), OTHER);
        register.put(OTHER_REPEAT.getValue(), OTHER_REPEAT);
    }

}
