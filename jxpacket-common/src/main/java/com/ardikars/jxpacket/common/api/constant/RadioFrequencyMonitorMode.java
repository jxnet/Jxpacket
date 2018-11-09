package com.ardikars.jxpacket.common.api.constant;

import com.ardikars.common.annotation.Immutable;

/**
 * @see <a href="https://en.wikipedia.org/wiki/Promiscuous_mode">Wikipedia</a>
 *
 * @author <a href="mailto:contact@ardikars.com">Ardika Rommy Sanjaya</a>
 * @since 1.1.4
 */
@Immutable
public enum RadioFrequencyMonitorMode {

    RFMON(1), NON_RFMON(0);

    private final int value;

    RadioFrequencyMonitorMode(final int value) {
        this.value = value;
    }

    /**
     * Returns 1 if in RFMon, 0 otherwise.
     * @return returns 1 if in RFMon, 0 otherwise.
     */
    public int getValue() {
        return this.value;
    }

}
