package com.ardikars.jxpacket.jxnet.spring.boot.autoconfigure;

import com.ardikars.common.net.Inet4Address;
import com.ardikars.common.net.Inet6Address;
import com.ardikars.common.net.MacAddress;
import com.ardikars.common.util.Address;
import com.ardikars.common.util.Platforms;
import com.ardikars.jxnet.*;
import com.ardikars.jxpacket.common.api.Jxpacket;
import com.ardikars.jxpacket.common.api.PcapNetworkInterface;
import com.ardikars.jxpacket.common.api.exception.DeviceNotFoundException;
import com.ardikars.jxpacket.jxnet.JxnetPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jxpacket 2018/11/08
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Configuration
@ConditionalOnClass(JxnetConfigurationProperties.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(JxnetConfigurationProperties.class)
public class JxnetPacketAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(JxnetPacketAutoConfiguration.class.getName());

    private static final byte[] IPV6_ZERO_BYTES = new byte[] {
            (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
            (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
            (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
            (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
    };

    @Value("${spring.application.name:}")
    private String applicationName;

    @Value("${spring.application.displayName:}")
    private String applicationDisplayName;

    @Value("${spring.application.version:0.0.0}")
    private String applicationVersion;

    private JxnetConfigurationProperties properties;

    @Autowired
    public JxnetPacketAutoConfiguration(JxnetConfigurationProperties properties) {
        this.properties = properties;
    }

    @Bean
    public Jxpacket jxpacket(Context context) {
        return new JxnetPacket(context);
    }

    @Bean("com.ardikars.jxnet.contex")
    public Context context(PcapNetworkInterface pcapIf,
                           @Qualifier("com.ardikars.jxnet.errbuf") StringBuilder errbuf) {
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
        Application.run(applicationName, applicationDisplayName, applicationVersion, builder);
        return Application.getApplicationContext();
    }

    @Bean
    public PcapNetworkInterface networkInterface(@Qualifier("com.ardikars.jxnet.errbuf") StringBuilder errbuf) throws DeviceNotFoundException {
        String source = properties.getSource();
        List<PcapIf> alldevsp = new ArrayList<>();
        if (Jxnet.PcapFindAllDevs(alldevsp, errbuf) != Jxnet.OK && LOGGER.isDebugEnabled()) {
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
    @Bean("com.ardikars.jxnet.errbuf")
    public StringBuilder errbuf() {
        return new StringBuilder();
    }

    private PcapNetworkInterface parsePcapNetworkInterface(PcapIf networkInterface) {
        List<Address> hardwareAddresses = new ArrayList<>();
        if (networkInterface.isLoopback()) {
            hardwareAddresses.add(MacAddress.ZERO);
        } else {
            if (Platforms.isWindows()) {
                byte[] hardwareAddress = Jxnet.FindHardwareAddress(networkInterface.getName());
                if (hardwareAddress != null && hardwareAddress.length == MacAddress.MAC_ADDRESS_LENGTH) {
                    hardwareAddresses.add(MacAddress.valueOf(hardwareAddress));
                } else {
                    throw new DeviceNotFoundException();
                }
            } else {
                try {
                    hardwareAddresses.add(MacAddress.fromNicName(networkInterface.getName()));
                } catch (SocketException e) {
                    throw new DeviceNotFoundException();
                }
            }
        }
        List<PcapNetworkInterface.PcapAddress> addresses = networkInterface.getAddresses().stream()
                .filter(pcapAddr -> pcapAddr.getAddr().getSaFamily() == SockAddr.Family.AF_INET ||
                        pcapAddr.getAddr().getSaFamily() == SockAddr.Family.AF_INET6)
                .map(pcapAddr -> {
                    byte[] rawAddress = pcapAddr.getAddr().getData();
                    byte[] rawNetmask = pcapAddr.getNetmask().getData();
                    byte[] rawBroadcastAddress = pcapAddr.getBroadAddr().getData();
                    byte[] rawDestinationAddress = pcapAddr.getBroadAddr().getData();
                    if (pcapAddr.getAddr().getSaFamily() == SockAddr.Family.AF_INET) {
                        Inet4Address address, netmask, broadcastAddress, destinationAddress;
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
                        Inet6Address address, netmask, broadcastAddress, destinationAddress;
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

}
