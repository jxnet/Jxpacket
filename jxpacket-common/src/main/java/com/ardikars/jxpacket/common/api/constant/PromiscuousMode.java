package com.ardikars.jxpacket.common.api.constant;

import com.ardikars.common.annotation.Immutable;

/**
 * Pcap promiscuous mode.
 *
 * @author <a href="mailto:contact@ardikars.com">Ardika Rommy Sanjaya</a>
 * @since 1.0.0
 */
@Immutable
public enum PromiscuousMode {

    PRIMISCUOUS(1), NON_PROMISCUOUS(0);

    private final int value;

    PromiscuousMode(final int value) {
        this.value = value;
    }

    /**
     * Returns 1 if in promicuous mode, 0 otherwise.
     * @return  returns if in promicuous mode, 0 otherwise.
     */
    public int getValue() {
        return this.value;
    }

}
