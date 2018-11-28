package com.ardikars.jxpacket.iso8583;

import com.ardikars.common.annotation.Incubating;
import com.ardikars.common.util.NamedNumber;

import java.util.HashMap;
import java.util.Map;

@Incubating
public class MessageVersion extends NamedNumber<Byte, MessageVersion> {

    public static final MessageVersion ISO9583_1987 = new MessageVersion((byte) 0x30, "1987");
    public static final MessageVersion ISO9583_1993 = new MessageVersion((byte) 0x31, "1993");
    public static final MessageVersion ISO9583_2003 = new MessageVersion((byte) 0x32, "2003");
    public static final MessageVersion NATIONAL_USE = new MessageVersion((byte) 0x38, "National use");
    public static final MessageVersion PRIVATE_USE = new MessageVersion((byte) 0x39, "Private use");
    public static final MessageVersion UNKNOWN = new MessageVersion((byte) -1, "Unknown");

    private static final Map<Byte, MessageVersion> registry =
            new HashMap<>();

    public MessageVersion(Byte value, String name) {
        super(value, name);
    }

    public static MessageVersion register(MessageVersion version) {
        registry.put(version.getValue(), version);
        return version;
    }

    public static MessageVersion valueOf(Byte value) {
        if (registry.containsKey(value)) {
            return registry.get(value);
        }
        return UNKNOWN;
    }

    static {
        registry.put(ISO9583_1987.getValue(), ISO9583_1987);
        registry.put(ISO9583_1993.getValue(), ISO9583_1993);
        registry.put(ISO9583_2003.getValue(), ISO9583_2003);
        registry.put(NATIONAL_USE.getValue(), NATIONAL_USE);
        registry.put(PRIVATE_USE.getValue(), PRIVATE_USE);
    }

}
