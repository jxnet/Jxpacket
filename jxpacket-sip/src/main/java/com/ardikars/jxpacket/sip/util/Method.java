package com.ardikars.jxpacket.sip.util;

import com.ardikars.common.annotation.Incubating;

/**
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Incubating
public enum Method {

    REGISTER("REGISTER"), INVITE("INVITE"), ACK("ACK"), CANCEL("CANCEL"), BYE("BYE"), OPTIONS("OPTIONS");

    private final String name;

    Method(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static Method fromString(String string) {
        for (Method method : values()) {
            if (method.getName().equals(string)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Invalid method name.");
    }

}
