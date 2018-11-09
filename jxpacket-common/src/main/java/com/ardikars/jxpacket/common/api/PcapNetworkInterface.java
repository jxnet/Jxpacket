package com.ardikars.jxpacket.common.api;

import com.ardikars.common.net.Inet4Address;
import com.ardikars.common.net.Inet6Address;
import com.ardikars.common.util.Address;

import java.util.List;

/**
 * @author jxpacket 2018/11/08
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
public class PcapNetworkInterface {

    /**
     * Interface is loopback.
     */
    private static final int PCAP_IF_LOOPBACK = 0x00000001;

    /**
     * Interface is up.
     */
    private static final int PCAP_IF_UP = 0x00000002;

    /**
     * Interface is running.
     */
    private static final int PCAP_IF_RUNNING = 0x00000004;

    private final String name;
    private final String description;
    private final List<Address> hardwareAddreses;
    private final List<PcapAddress> addresses;
    private final boolean loopback;
    private final boolean up;
    private final boolean running;

    public PcapNetworkInterface(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.hardwareAddreses = builder.hardwareAddreses;
        this.addresses = builder.addresses;
        this.loopback = builder.loopback;
        this.up = builder.up;
        this.running = builder.running;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Address> getHardwareAddreses() {
        return hardwareAddreses;
    }

    public List<PcapAddress> getAddresses() {
        return addresses;
    }

    public boolean isLoopback() {
        return loopback;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public String toString() {
        return new StringBuilder("PcapNetworkInterface{")
                .append("name='").append(name).append('\'')
                .append(", description='").append(description).append('\'')
                .append(", hardwareAddresses='").append(hardwareAddreses).append('\'')
                .append(", addresses=").append(addresses)
                .append(", loopback=").append(loopback)
                .append(", up=").append(up)
                .append(", running=").append(running)
                .append('}').toString();
    }

    public static class Builder implements com.ardikars.common.util.Builder<PcapNetworkInterface, Void> {

        private String name;
        private String description;
        private List<Address> hardwareAddreses;
        private List<PcapAddress> addresses;
        private boolean loopback;
        private boolean up;
        private boolean running;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder hardwareAddresses(List<Address> hardwareAddresses) {
            this.hardwareAddreses = hardwareAddresses;
            return this;
        }

        public Builder addresses(List<PcapAddress> addresses) {
            this.addresses = addresses;
            return this;
        }

        public Builder flags(int flags) {
            this.loopback = (flags & PCAP_IF_LOOPBACK) != 0;
            this.up = (flags & PCAP_IF_UP) != 0;
            this.running = (flags & PCAP_IF_RUNNING) != 0;
            return this;
        }

        public Builder loopback(boolean loopback) {
            this.loopback = loopback;
            return this;
        }

        public Builder up(boolean up) {
            this.up = up;
            return this;
        }

        public Builder running(boolean running) {
            this.running = running;
            return this;
        }

        @Override
        public PcapNetworkInterface build() {
            return new PcapNetworkInterface(this);
        }

        @Override
        public PcapNetworkInterface build(Void value) {
            throw new UnsupportedOperationException();
        }

    }

    public interface PcapAddress  {

        Address getAddress();

        Address getNetmask();

        Address getBroadcastAddress();

        Address getDestinationAddress();

    }

    public static class PcapInet4Address implements PcapAddress {

        private final Inet4Address address;
        private final Inet4Address netmask;
        private final Inet4Address broadcastAddress;
        private final Inet4Address destinationAddress;

        public PcapInet4Address(Builder builder) {
            this.address = builder.address;
            this.netmask = builder.netmask;
            this.broadcastAddress = builder.broadcastAddress;
            this.destinationAddress = builder.destinationAddress;
        }

        @Override
        public Inet4Address getAddress() {
            return address;
        }

        @Override
        public Inet4Address getNetmask() {
            return netmask;
        }

        @Override
        public Inet4Address getBroadcastAddress() {
            return broadcastAddress;
        }

        @Override
        public Inet4Address getDestinationAddress() {
            return destinationAddress;
        }

        @Override
        public String toString() {
            return new StringBuilder("PcapInet4Address{")
                    .append("address=").append(address)
                    .append(", netmask=").append(netmask)
                    .append(", broadcastAddress=").append(broadcastAddress)
                    .append(", destinationAddress=").append(destinationAddress)
                    .append('}').toString();
        }

        public static class Builder implements com.ardikars.common.util.Builder<PcapInet4Address, Void> {

            private Inet4Address address;
            private Inet4Address netmask;
            private Inet4Address broadcastAddress;
            private Inet4Address destinationAddress;

            public Builder address(Inet4Address address) {
                this.address = address;
                return this;
            }

            public Builder netmask(Inet4Address netmask) {
                this.netmask = netmask;
                return this;
            }

            public Builder broadcastAddress(Inet4Address broadcastAddress) {
                this.broadcastAddress = broadcastAddress;
                return this;
            }

            public Builder destinationAddress(Inet4Address destinationAddress) {
                this.destinationAddress = destinationAddress;
                return this;
            }

            @Override
            public PcapInet4Address build() {
                return new PcapInet4Address(this);
            }

            @Override
            public PcapInet4Address build(Void value) {
                throw new UnsupportedOperationException();
            }

        }

    }

    public static class PcapInet6Address implements PcapAddress {

        private final Inet6Address address;
        private final Inet6Address netmask;
        private final Inet6Address broadcastAddress;
        private final Inet6Address destinationAddress;

        public PcapInet6Address(Builder builder) {
            this.address = builder.address;
            this.netmask = builder.netmask;
            this.broadcastAddress = builder.broadcastAddress;
            this.destinationAddress = builder.destinationAddress;
        }

        @Override
        public Inet6Address getAddress() {
            return address;
        }

        @Override
        public Inet6Address getNetmask() {
            return broadcastAddress;
        }

        @Override
        public Inet6Address getBroadcastAddress() {
            return netmask;
        }

        @Override
        public Inet6Address getDestinationAddress() {
            return destinationAddress;
        }

        @Override
        public String toString() {
            return new StringBuilder("PcapInet6Address{")
                    .append("address=").append(address)
                    .append(", netmask=").append(netmask)
                    .append(", broadcastAddress=").append(broadcastAddress )
                    .append(", destinationAddress=").append(destinationAddress)
                    .append('}').toString();
        }

        public static class Builder implements com.ardikars.common.util.Builder<PcapInet6Address, Void> {

            private Inet6Address address;
            private Inet6Address netmask;
            private Inet6Address broadcastAddress;
            private Inet6Address destinationAddress;

            public Builder address(Inet6Address address) {
                this.address = address;
                return this;
            }

            public Builder netmask(Inet6Address netmask) {
                this.netmask = netmask;
                return this;
            }

            public Builder broadcastAddress(Inet6Address broadcastAddress) {
                this.broadcastAddress = broadcastAddress;
                return this;
            }

            public Builder destinationAddress(Inet6Address destinationAddress) {
                this.destinationAddress = destinationAddress;
                return this;
            }

            @Override
            public PcapInet6Address build() {
                return new PcapInet6Address(this);
            }

            @Override
            public PcapInet6Address build(Void value) {
                throw new UnsupportedOperationException();
            }

        }

    }

}
