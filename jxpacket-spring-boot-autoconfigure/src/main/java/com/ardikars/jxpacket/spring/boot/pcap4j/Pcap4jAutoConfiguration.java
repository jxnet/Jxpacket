/**
 * Copyright (C) 2017-2018  Ardika Rommy Sanjaya <contact@ardikars.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ardikars.jxpacket.spring.boot.pcap4j;

import com.ardikars.common.net.Inet4Address;
import com.ardikars.common.net.Inet6Address;
import com.ardikars.common.net.MacAddress;
import com.ardikars.common.util.Address;
import com.ardikars.jxpacket.common.api.Jxpacket;
import com.ardikars.jxpacket.common.api.PcapNetworkInterface;
import com.ardikars.jxpacket.common.api.constant.PcapTimestampPrecision;
import com.ardikars.jxpacket.common.api.constant.PromiscuousMode;
import com.ardikars.jxpacket.common.api.exception.DeviceNotFoundException;
import com.ardikars.jxpacket.common.api.exception.NativeException;
import com.ardikars.jxpacket.pcap4j.Pcap4jPacket;
import com.ardikars.jxpacket.spring.boot.AbstractAutoConfiguration;
import com.ardikars.jxpacket.spring.boot.JxpacketConfigurationProperties;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.pcap4j.core.PcapAddress;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapIpV4Address;
import org.pcap4j.core.PcapIpV6Address;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.namednumber.DataLinkType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @author jxpacket 2018/11/08
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Configuration("com.ardikars.jxpacket.pcap4jAutoConfiguration")
@ConditionalOnClass(Pcaps.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(JxpacketConfigurationProperties.class)
public class Pcap4jAutoConfiguration extends AbstractAutoConfiguration {

    private final JxpacketConfigurationProperties properties;

    @Autowired
    public Pcap4jAutoConfiguration(JxpacketConfigurationProperties properties) {
        this.properties = properties;
    }

    @Bean
    public Jxpacket jxpacket(PcapHandle pcapHandle) {
        return new Pcap4jPacket(pcapHandle);
    }

    /**
     * Pcap4j pcap handle.
     * @param pcapIf network interface.
     * @return returns {@link PcapHandle}.
     */
    @Bean("org.pcap4j.core.pcapHandle")
    public PcapHandle pcapHandle(PcapNetworkInterface pcapIf) {
        switch (properties.getPcapType()) {
            case DEAD:
                try {
                    if (properties.getTimestampPrecision() == PcapTimestampPrecision.NANO) {
                        return Pcaps.openDead(DataLinkType.getInstance(properties.getDatalink()),
                                properties.getSnapshot(), PcapHandle.TimestampPrecision.NANO);
                    } else {
                        return Pcaps.openDead(DataLinkType.getInstance(properties.getDatalink()),
                                properties.getSnapshot(), PcapHandle.TimestampPrecision.MICRO);
                    }
                } catch (PcapNativeException e) {
                    throw new NativeException();
                }
            case OFFLINE:
                try {
                    if (properties.getTimestampPrecision() == PcapTimestampPrecision.NANO) {
                        return Pcaps.openOffline(properties.getFile(), PcapHandle.TimestampPrecision.NANO);
                    } else {
                        return Pcaps.openOffline(properties.getFile(), PcapHandle.TimestampPrecision.MICRO);
                    }
                } catch (PcapNativeException e) {
                    throw new NativeException();
                }
            default:
                try {
                    Optional<org.pcap4j.core.PcapNetworkInterface> networkInterface = Pcaps.findAllDevs().stream()
                            .filter(iface -> iface.getName().equalsIgnoreCase(pcapIf.getName()))
                            .findFirst();
                    if (!networkInterface.isPresent()) {
                        throw new DeviceNotFoundException();
                    }
                    if (properties.getPromiscuous() == PromiscuousMode.NON_PROMISCUOUS) {
                        return networkInterface.get().openLive(properties.getSnapshot(),
                                org.pcap4j.core.PcapNetworkInterface.PromiscuousMode.NONPROMISCUOUS, properties.getTimeout());
                    } else {
                        return networkInterface.get().openLive(properties.getSnapshot(),
                                org.pcap4j.core.PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, properties.getTimeout());
                    }
                } catch (PcapNativeException e) {
                    throw new NativeException();
                }
        }
    }


    /**
     * Network interface.
     * @return returns {@link PcapNetworkInterface}.
     */
    @Bean
    public PcapNetworkInterface networkInterface() {
        try {
            String source = properties.getSource();
            List<org.pcap4j.core.PcapNetworkInterface> networkInterfaces = Pcaps.findAllDevs();
            if (source == null || source.isEmpty()) {
                for (org.pcap4j.core.PcapNetworkInterface networkInterface : networkInterfaces) {
                    for (PcapAddress address : networkInterface.getAddresses()) {
                        if (address instanceof PcapIpV4Address && address.getAddress() != null
                                && address.getAddress().getAddress() != null) {
                            byte[] data = address.getAddress().getAddress();
                            Inet4Address inet4Address = Inet4Address.valueOf(data);
                            if (!inet4Address.equals(Inet4Address.LOCALHOST) && !inet4Address.equals(Inet4Address.ZERO)) {
                                return parsePcapNetworkInterface(networkInterface);
                            }
                        }
                    }
                }
            } else {
                for (org.pcap4j.core.PcapNetworkInterface networkInterface : networkInterfaces) {
                    if (networkInterface.getName().equalsIgnoreCase(source)) {
                        return parsePcapNetworkInterface(networkInterface);
                    }
                }
            }
        } catch (PcapNativeException e) {
            throw new DeviceNotFoundException();
        }
        throw new DeviceNotFoundException();
    }

    private PcapNetworkInterface parsePcapNetworkInterface(org.pcap4j.core.PcapNetworkInterface networkInterface) {
        List<Address> hardwareAddresses = networkInterface.getLinkLayerAddresses().stream()
                .filter(linkLayerAddress -> linkLayerAddress instanceof org.pcap4j.util.MacAddress)
                .map(linkLayerAddress -> MacAddress.valueOf(linkLayerAddress.getAddress()))
                .collect(Collectors.toList());
        List<PcapNetworkInterface.PcapAddress> addresses = networkInterface.getAddresses().stream()
                .filter(pcapAddress -> pcapAddress instanceof PcapIpV4Address || pcapAddress instanceof PcapIpV6Address)
                .map(pcapAddress -> {
                    if (pcapAddress instanceof PcapIpV4Address) {
                        return new PcapNetworkInterface.PcapInet4Address.Builder()
                                .address(Inet4Address.valueOf(pcapAddress.getAddress().getAddress()))
                                .netmask(Inet4Address.valueOf(pcapAddress.getNetmask().getAddress()))
                                .broadcastAddress(Inet4Address.valueOf(pcapAddress.getBroadcastAddress().getAddress()))
                                .destinationAddress(Inet4Address.valueOf(pcapAddress.getDestinationAddress().getAddress()))
                                .build();
                    } else {
                        return new PcapNetworkInterface.PcapInet6Address.Builder()
                                .address(Inet6Address.valueOf(pcapAddress.getAddress().getAddress()))
                                .netmask(Inet6Address.valueOf(pcapAddress.getNetmask().getAddress()))
                                .broadcastAddress(Inet6Address.valueOf(pcapAddress.getBroadcastAddress().getAddress()))
                                .destinationAddress(Inet6Address.valueOf(pcapAddress.getDestinationAddress().getAddress()))
                                .build();
                    }
                })
                .collect(Collectors.toList());
        return new PcapNetworkInterface.Builder()
                .name(networkInterface.getName())
                .description(networkInterface.getDescription())
                .hardwareAddresses(hardwareAddresses)
                .addresses(addresses)
                .loopback(networkInterface.isLoopBack())
                .up(networkInterface.isUp())
                .running(networkInterface.isRunning())
                .addresses(addresses)
                .build();
    }

    @Override
    public String prettyApplicationInformation() {
        return new StringBuilder()
                .append("Pcap4j")
                .append(super.toString()).toString();
    }

    @Override
    public String toString() {
        return prettyApplicationInformation();
    }

}
