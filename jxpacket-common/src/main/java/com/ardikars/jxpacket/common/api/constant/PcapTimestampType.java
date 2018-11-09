package com.ardikars.jxpacket.common.api.constant;

import com.ardikars.common.annotation.Immutable;

/**
 * Pcap timestamp type.
 *
 * @author <a href="mailto:contact@ardikars.com">Ardika Rommy Sanjaya</a>
 * @since 1.1.5
 */
@Immutable
public enum PcapTimestampType {

    HOST(0), HOST_LOWPREC(1), HOST_HIPREC(2), ADAPTER(3), ADAPTER_UNSYNCED(4);

    private final int value;

    PcapTimestampType(final int value) {
        this.value = value;
    }

    /**
     * Get timestamp type value.
     * @return returns integer value.
     */
    public int getValue() {
        return this.value;
    }

}
