package com.ardikars.jxpacket.common.api.constant;

import com.ardikars.common.annotation.Immutable;

/**
 * Direction of packets.
 * typedef enum {
 *   PCAP_D_INOUT = 0,
 *   PCAP_D_IN,
 *   PCAP_D_OUT
 * } pcap_direction_t;
 *
 * @author <a href="mailto:contact@ardikars.com">Ardika Rommy Sanjaya</a>
 * @since 1.0.0
 */
@Immutable
public enum PcapDirection {

    PCAP_D_INOUT,
    PCAP_D_IN,
    PCAP_D_OUT;

}
