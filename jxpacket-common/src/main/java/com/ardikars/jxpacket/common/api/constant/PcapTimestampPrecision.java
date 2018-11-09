package com.ardikars.jxpacket.common.api.constant;

import com.ardikars.common.annotation.Immutable;

/**
 * Pcap timestamp precision.
 *
 * @author <a href="mailto:contact@ardikars.com">Ardika Rommy Sanjaya</a>
 * @since 1.1.5
 */
@Immutable
public enum PcapTimestampPrecision {

    MICRO(0), NANO(1);

    private final int value;

    PcapTimestampPrecision(final int value) {
        this.value = value;
    }

    /**
     * Get timestamp precision value.
     * @return returns integer value.
     */
    public int getValue() {
        return this.value;
    }

}
