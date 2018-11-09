package com.ardikars.jxpacket.common.api.constant;

import com.ardikars.common.annotation.Immutable;

/**
 * Libpcap never guarantee, that packets will be delivered immediately,
 * the instant that they arrive, unless you enable "immediate mode"
 * by calling pcap_set_immediate_mode() between pcap_create() and pcap_activate().
 *
 * @author <a href="mailto:contact@ardikars.com">Ardika Rommy Sanjaya</a>
 * @since 1.1.4
 */
@Immutable
public enum ImmediateMode {

    IMMEDIATE(1), NON_IMMEDIATE(0);

    private final int value;

    ImmediateMode(final int value) {
        this.value = value;
    }

    /**
     * Is immediate mode enabled?.
     * @return returns true if immediate mode, false otherwise.
     */
    public int getValue() {
        return this.value;
    }

}
