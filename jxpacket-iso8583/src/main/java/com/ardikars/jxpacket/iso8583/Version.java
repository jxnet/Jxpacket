package com.ardikars.jxpacket.iso8583;

import com.ardikars.common.annotation.Incubating;
import com.ardikars.common.util.NamedNumber;

import java.util.HashMap;
import java.util.Map;

@Incubating
public class Version extends NamedNumber<Byte, Version> {

    public static final Version ISO9583_1987 = new Version((byte) 0x30, "1987");
    public static final Version ISO9583_1993 = new Version((byte) 0x31, "1993");
    public static final Version ISO9583_2003 = new Version((byte) 0x32, "2003");
    public static final Version NATIONAL_USE = new Version((byte) 0x38, "National use");
    public static final Version PRIVATE_USE = new Version((byte) 0x39, "Private use");
    public static final Version UNKNOWN = new Version((byte) -1, "Unknown");

    private static final Map<Byte, Version> registry =
            new HashMap<>();

    public Version(Byte value, String name) {
        super(value, name);
    }

    public static Version register(Version version) {
        registry.put(version.getValue(), version);
        return version;
    }

    public static Version valueOf(Byte value) {
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
