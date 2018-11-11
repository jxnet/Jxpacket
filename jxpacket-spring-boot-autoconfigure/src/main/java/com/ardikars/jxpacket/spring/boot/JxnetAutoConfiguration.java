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

package com.ardikars.jxpacket.spring.boot;

import static com.ardikars.jxnet.Jxnet.FindHardwareAddress;
import static com.ardikars.jxnet.Jxnet.OK;
import static com.ardikars.jxnet.Jxnet.PcapFindAllDevs;

import com.ardikars.common.net.Inet4Address;
import com.ardikars.common.net.Inet6Address;
import com.ardikars.common.net.MacAddress;
import com.ardikars.common.util.Address;
import com.ardikars.common.util.Platforms;
import com.ardikars.jxnet.Application;
import com.ardikars.jxnet.Context;
import com.ardikars.jxnet.DataLinkType;
import com.ardikars.jxnet.ImmediateMode;
import com.ardikars.jxnet.Jxnet;
import com.ardikars.jxnet.Pcap;
import com.ardikars.jxnet.PcapAddr;
import com.ardikars.jxnet.PcapDirection;
import com.ardikars.jxnet.PcapIf;
import com.ardikars.jxnet.PcapTimestampPrecision;
import com.ardikars.jxnet.PcapTimestampType;
import com.ardikars.jxnet.PromiscuousMode;
import com.ardikars.jxnet.RadioFrequencyMonitorMode;
import com.ardikars.jxnet.SockAddr;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.common.api.Jxpacket;
import com.ardikars.jxpacket.common.api.PcapNetworkInterface;
import com.ardikars.jxpacket.common.api.exception.DeviceNotFoundException;
import com.ardikars.jxpacket.jxnet.JxnetPacket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jxpacket 2018/11/10
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Configuration("com.ardikars.jxpacket.JxnetAutoConfiguration")
@ConditionalOnClass({Jxnet.class, Context.class, Packet.class})
@EnableConfigurationProperties(JxpacketConfigurationProperties.class)
public class JxnetAutoConfiguration extends AbstractAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(JxnetAutoConfiguration.class.getName());

    private static final byte[] IPV6_ZERO_BYTES = new byte[] {
            (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
            (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
            (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
            (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
    };

    private final JxpacketConfigurationProperties properties;

    @Autowired
    public JxnetAutoConfiguration(JxpacketConfigurationProperties properties) {
        this.properties = properties;
    }

    @Bean
    public Jxpacket jxpacket(Context context) {
        return new JxnetPacket(context);
    }

    /**
     * Jxnet application context.
     * @param pcapIf network interface.
     * @param errbuf error buffer.
     * @return returns {@link Context}.
     */
    @Bean
    public Context context(PcapNetworkInterface pcapIf, StringBuilder errbuf) {
        String source = pcapIf.getName();
        PromiscuousMode promiscuousMode = properties.getPromiscuous()
                == com.ardikars.jxpacket.common.api.constant.PromiscuousMode.NON_PROMISCUOUS
                ? PromiscuousMode.NON_PROMISCUOUS : PromiscuousMode.PRIMISCUOUS;
        ImmediateMode immediateMode = properties.getImmediate()
                == com.ardikars.jxpacket.common.api.constant.ImmediateMode.NON_IMMEDIATE
                ? ImmediateMode.NON_IMMEDIATE : ImmediateMode.IMMEDIATE;
        PcapTimestampType timestampType;
        switch (properties.getTimestampType()) {
            case ADAPTER:
                timestampType = PcapTimestampType.ADAPTER;
                break;
            case HOST_HIPREC:
                timestampType = PcapTimestampType.HOST_HIPREC;
                break;
            case HOST_LOWPREC:
                timestampType = PcapTimestampType.HOST_LOWPREC;
                break;
            case ADAPTER_UNSYNCED:
                timestampType = PcapTimestampType.ADAPTER_UNSYNCED;
                break;
            default:
                timestampType = PcapTimestampType.HOST;
                break;
        }
        PcapDirection direction;
        switch (properties.getDirection()) {
            case PCAP_D_IN:
                direction = PcapDirection.PCAP_D_IN;
                break;
            case PCAP_D_OUT:
                direction = PcapDirection.PCAP_D_OUT;
                break;
            default:
                direction = PcapDirection.PCAP_D_INOUT;
                break;
        }
        PcapTimestampPrecision timestampPrecision = properties.getTimestampPrecision()
                == com.ardikars.jxpacket.common.api.constant.PcapTimestampPrecision.NANO
                ? PcapTimestampPrecision.NANO : PcapTimestampPrecision.MICRO;
        RadioFrequencyMonitorMode radioFrequencyMonitorMode = properties.getRfmon()
                == com.ardikars.jxpacket.common.api.constant.RadioFrequencyMonitorMode.NON_RFMON
                ? RadioFrequencyMonitorMode.NON_RFMON : RadioFrequencyMonitorMode.RFMON;
        DataLinkType dataLinkType = DataLinkType.valueOf(properties.getDatalink().shortValue());
        Pcap.Builder builder = new Pcap.Builder()
                .source(source)
                .snaplen(properties.getSnapshot())
                .promiscuousMode(promiscuousMode)
                .timeout(properties.getTimeout())
                .immediateMode(immediateMode)
                .timestampType(timestampType)
                .direction(direction)
                .timestampPrecision(timestampPrecision)
                .rfmon(radioFrequencyMonitorMode)
                .enableNonBlock(!properties.getBlocking())
                .dataLinkType(dataLinkType)
                .fileName(properties.getFile())
                .errbuf(errbuf);
        switch (properties.getPcapType()) {
            case DEAD:
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Opening pcap dead handler : {}", builder);
                }
                builder.pcapType(Pcap.PcapType.DEAD);
                break;
            case OFFLINE:
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Opening pcap offline handler : {}", builder);
                }
                builder.pcapType(Pcap.PcapType.OFFLINE);
                break;
            default:
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Opening pcap live handler : {}", builder);
                }
                builder.pcapType(Pcap.PcapType.LIVE);
                break;
        }
        Application.run(getApplicationName(), getApplicationDisplayName(), getApplicationVersion(), builder);
        return Application.getApplicationContext();
    }

    /**
     * Network interface.
     * @param errbuf error buffer.
     * @return returns {@link PcapNetworkInterface}.
     * @throws DeviceNotFoundException device not found exception.
     */
    @Bean
    public PcapNetworkInterface networkInterface(StringBuilder errbuf) throws DeviceNotFoundException {
        String source = properties.getSource();
        List<PcapIf> alldevsp = new ArrayList<>();
        if (PcapFindAllDevs(alldevsp, errbuf) != OK && LOGGER.isDebugEnabled()) {
            LOGGER.debug("Error: {}", errbuf.toString());
        }
        if (source == null || source.isEmpty()) {
            for (PcapIf dev : alldevsp) {
                for (PcapAddr addr : dev.getAddresses()) {
                    if (addr.getAddr().getSaFamily() == SockAddr.Family.AF_INET && addr.getAddr().getData() != null) {
                        Inet4Address d = Inet4Address.valueOf(addr.getAddr().getData());
                        if (!d.equals(Inet4Address.LOCALHOST) && !d.equals(Inet4Address.ZERO)) {
                            return parsePcapNetworkInterface(dev);
                        }
                    }
                }
            }
        } else {
            for (PcapIf dev : alldevsp) {
                if (dev.getName().equals(source)) {
                    return parsePcapNetworkInterface(dev);
                }
            }
        }
        throw new DeviceNotFoundException();
    }

    /**
     * Error buffer.
     * @return error buffer.
     */
    @Bean
    public StringBuilder errbuf() {
        return new StringBuilder();
    }

    private PcapNetworkInterface parsePcapNetworkInterface(PcapIf networkInterface) {
        List<Address> hardwareAddresses = findHardwareAddress(networkInterface);
        List<PcapNetworkInterface.PcapAddress> addresses = findAddresses(networkInterface);
        return new PcapNetworkInterface.Builder()
                .name(networkInterface.getName())
                .description(networkInterface.getDescription())
                .hardwareAddresses(hardwareAddresses)
                .addresses(addresses)
                .loopback(networkInterface.isLoopback())
                .up(networkInterface.isUp())
                .running(networkInterface.isRunning())
                .addresses(addresses)
                .build();
    }

    private List<Address> findHardwareAddress(PcapIf networkInterface) {
        List<Address> hardwareAddresses = new ArrayList<>();
        if (networkInterface.isLoopback()) {
            hardwareAddresses.add(MacAddress.ZERO);
        } else {
            if (Platforms.isWindows()) {
                byte[] hardwareAddress = FindHardwareAddress(networkInterface.getName());
                if (hardwareAddress != null && hardwareAddress.length == MacAddress.MAC_ADDRESS_LENGTH) {
                    hardwareAddresses.add(MacAddress.valueOf(hardwareAddress));
                } else {
                    return hardwareAddresses;
                }
            } else {
                try {
                    hardwareAddresses.add(MacAddress.fromNicName(networkInterface.getName()));
                } catch (SocketException e) {
                    return hardwareAddresses;
                }
            }
        }
        return hardwareAddresses;
    }

    private List<PcapNetworkInterface.PcapAddress> findAddresses(PcapIf networkInterface) {
        return networkInterface.getAddresses().stream()
                .filter(pcapAddr -> pcapAddr.getAddr().getSaFamily() == SockAddr.Family.AF_INET
                        || pcapAddr.getAddr().getSaFamily() == SockAddr.Family.AF_INET6)
                .map(pcapAddr -> {
                    byte[] rawAddress = pcapAddr.getAddr().getData();
                    byte[] rawNetmask = pcapAddr.getNetmask().getData();
                    byte[] rawBroadcastAddress = pcapAddr.getBroadAddr().getData();
                    byte[] rawDestinationAddress = pcapAddr.getBroadAddr().getData();
                    if (pcapAddr.getAddr().getSaFamily() == SockAddr.Family.AF_INET) {
                        Inet4Address address;
                        Inet4Address netmask;
                        Inet4Address broadcastAddress;
                        Inet4Address destinationAddress;
                        if (rawAddress != null && rawAddress.length == Inet4Address.IPV4_ADDRESS_LENGTH) {
                            address = Inet4Address.valueOf(rawAddress);
                        } else {
                            address = Inet4Address.valueOf(0);
                        }
                        if (rawNetmask != null && rawNetmask.length == Inet4Address.IPV4_ADDRESS_LENGTH) {
                            netmask = Inet4Address.valueOf(rawNetmask);
                        } else {
                            netmask = Inet4Address.valueOf(0);
                        }
                        if (rawBroadcastAddress != null && rawBroadcastAddress.length == Inet4Address.IPV4_ADDRESS_LENGTH) {
                            broadcastAddress = Inet4Address.valueOf(rawBroadcastAddress);
                        } else {
                            broadcastAddress = Inet4Address.valueOf(0);
                        }
                        if (rawDestinationAddress != null && rawDestinationAddress.length == Inet4Address.IPV4_ADDRESS_LENGTH) {
                            destinationAddress = Inet4Address.valueOf(rawDestinationAddress);
                        } else {
                            destinationAddress = Inet4Address.valueOf(0);
                        }
                        return new PcapNetworkInterface.PcapInet4Address.Builder()
                                .address(address)
                                .netmask(netmask)
                                .broadcastAddress(broadcastAddress)
                                .destinationAddress(destinationAddress)
                                .build();
                    } else {
                        Inet6Address address;
                        Inet6Address netmask;
                        Inet6Address broadcastAddress;
                        Inet6Address destinationAddress;
                        if (rawAddress != null && rawAddress.length == Inet6Address.IPV6_ADDRESS_LENGTH) {
                            address = Inet6Address.valueOf(rawAddress);
                        } else {
                            address = Inet6Address.valueOf(IPV6_ZERO_BYTES);
                        }
                        if (rawNetmask != null && rawNetmask.length == Inet6Address.IPV6_ADDRESS_LENGTH) {
                            netmask = Inet6Address.valueOf(rawNetmask);
                        } else {
                            netmask = Inet6Address.valueOf(IPV6_ZERO_BYTES);
                        }
                        if (rawBroadcastAddress != null && rawBroadcastAddress.length == Inet6Address.IPV6_ADDRESS_LENGTH) {
                            broadcastAddress = Inet6Address.valueOf(rawBroadcastAddress);
                        } else {
                            broadcastAddress = Inet6Address.valueOf(IPV6_ZERO_BYTES);
                        }
                        if (rawDestinationAddress != null && rawDestinationAddress.length == Inet6Address.IPV6_ADDRESS_LENGTH) {
                            destinationAddress = Inet6Address.valueOf(rawDestinationAddress);
                        } else {
                            destinationAddress = Inet6Address.valueOf(IPV6_ZERO_BYTES);
                        }
                        return new PcapNetworkInterface.PcapInet6Address.Builder()
                                .address(address)
                                .netmask(netmask)
                                .broadcastAddress(broadcastAddress)
                                .destinationAddress(destinationAddress)
                                .build();
                    }
                }).collect(Collectors.toList());
    }

    @Override
    public String prettyApplicationInformation() {
        return new StringBuilder()
                .append("Jxnet")
                .append(super.toString()).toString();
    }

    @Override
    public String toString() {
        return prettyApplicationInformation();
    }

}
