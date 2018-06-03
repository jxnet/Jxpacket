/**
 * Copyright (C) 2017  Ardika Rommy Sanjaya
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

package jxpacket.ip;

import jxpacket.common.Inet4Address;
import jxpacket.tcp.TCP;
import jxpacket.udp.UDP;
import jxpacket.Packet;

import java.nio.ByteBuffer;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.0
 */
public class IPv4 extends IP {

    public static final int IPV4_HEADER_LENGTH = 20;

    private byte version;
    private byte headerLength;
    private byte diffServ;
    private byte expCon;
    private short totalLength;
    private short identification;
    private byte flags;
    private short fragmentOffset;
    private byte ttl;
    private IPProtocolType protocol;
    private short checksum;
    private Inet4Address sourceAddress;
    private Inet4Address destinationAddress;
    private byte[] options;

    public IPv4() {
        this.setVersion((byte) 4);
        this.setHeaderLength((byte) 5);
        this.setDiffServ((byte) 0);
        this.setExpCon((byte) 0);
        this.setTotalLength((byte) 0);
        this.setIdentification((byte) 0);
        this.setFlags((byte) 0x02);
        this.setFragmentOffset((byte) 0);
        this.setTtl((byte) 0);
        this.setProtocol(IPProtocolType.UNKNOWN);
        this.setChecksum((short) 0);
        this.setSourceAddress(Inet4Address.LOCALHOST);
        this.setDestinationAddress(Inet4Address.LOCALHOST);
        this.setOptions(null);
        this.nextPacket = null;
    }

    public byte getVersion() {
        return (byte) (this.version & 0xf);
    }

    public IPv4 setVersion(final byte version) {
        this.version = (byte) (version & 0xf);
        return this;
    }

    public byte getHeaderLength() {
        return (byte) (this.headerLength & 0xf);
    }

    public IPv4 setHeaderLength(final byte headerLength) {
        this.headerLength = (byte) (headerLength & 0xf);
        return this;
    }

    public byte getDiffServ() {
        return (byte) (this.diffServ & 0x3f);
    }

    public IPv4 setDiffServ(final byte diffServ) {
        this.diffServ = (byte) (this.diffServ & 0x3f);
        return this;
    }

    public byte getExpCon() {
        return (byte) (this.expCon & 0x3);
    }

    public IPv4 setExpCon(final byte expCon) {
        this.expCon = (byte) (expCon & 0x3);
        return this;
    }

    public short getTotalLength() {
        return (short) (this.totalLength & 0xffff);
    }

    public IPv4 setTotalLength(final short totalLength) {
        this.totalLength = (short) (totalLength & 0xffff);
        return this;
    }

    public short getIdentification() {
        return (short) (this.identification & 0xffff);
    }

    public IPv4 setIdentification(final short identification) {
        this.identification = (short) (identification & 0xffff);
        return this;
    }

    public byte getFlags() {
        return (byte) (this.flags & 0x7);
    }

    public IPv4 setFlags(final byte flags) {
        this.flags = (byte) (flags & 0x7);
        return this;
    }

    public short getFragmentOffset() {
        return (short) (this.fragmentOffset & 0x1fff);
    }

    public IPv4 setFragmentOffset(final short fragmentOffset) {
        this.fragmentOffset = (short) (fragmentOffset & 0x1fff);
        return this;
    }

    public byte getTtl() {
        return (byte) (this.ttl & 0xff);
    }

    public IPv4 setTtl(final byte ttl) {
        this.ttl = (byte) (ttl & 0xff);
        return this;
    }

    public IPProtocolType getProtocol() {
        return this.protocol;
    }

    public IPv4 setProtocol(final IPProtocolType protocol) {
        this.protocol = protocol;
        return this;
    }

    public short getChecksum() {
        return (short) (this.checksum & 0xffff);
    }

    public IPv4 setChecksum(final short checksum) {
        this.checksum = (short) (this.checksum & 0xffff);
        return this;
    }

    public Inet4Address getSourceAddress() {
        return this.sourceAddress;
    }

    public IPv4 setSourceAddress(final Inet4Address sourceAddress) {
        this.sourceAddress = sourceAddress;
        return this;
    }

    public Inet4Address getDestinationAddress() {
        return this.destinationAddress;
    }

    public IPv4 setDestinationAddress(final Inet4Address destinationAddress) {
        this.destinationAddress = destinationAddress;
        return this;
    }

    public byte[] getOptions() {
        return this.options;
    }

    public IPv4 setOptions(final byte[] options) {
        this.options = options;
        return this;
    }

    public static IPv4 newInstance(final ByteBuffer buffer) {
        IPv4 ipv4 = new IPv4();
        ipv4.version = buffer.get();
        ipv4.setHeaderLength((byte) (ipv4.version & 0xf));
        ipv4.setVersion((byte) ((ipv4.version >> 4) & 0xf));
        byte tmp = buffer.get();
        ipv4.setDiffServ((byte) ((tmp >> 2) & 0x3f));
        ipv4.setExpCon((byte) (tmp & 0x3));
        ipv4.setTotalLength(buffer.getShort());
        ipv4.setIdentification(buffer.getShort());
        short sscratch = buffer.getShort();
        ipv4.setFlags((byte) (sscratch >> 13 & 0x7));
        ipv4.setFragmentOffset((short) (sscratch & 0x1fff));
        ipv4.setTtl(buffer.get());
        ipv4.setProtocol(IPProtocolType.getInstance(buffer.get()));
        ipv4.setChecksum(buffer.getShort());

        byte[] addrBuf = new byte[Inet4Address.IPV4_ADDRESS_LENGTH];
        buffer.get(addrBuf);
        ipv4.setSourceAddress(Inet4Address.valueOf(addrBuf));

        addrBuf = new byte[Inet4Address.IPV4_ADDRESS_LENGTH];
        buffer.get(addrBuf);
        ipv4.setDestinationAddress(Inet4Address.valueOf(addrBuf));

        if (ipv4.getHeaderLength() > 5) {
            int optionsLength = (ipv4.getHeaderLength() - 5) * 4;
            ipv4.options = new byte[optionsLength];
            buffer.get(ipv4.options);
            ipv4.nextPacket = buffer.slice();
        } else {
            ipv4.nextPacket = buffer.slice();
        }
        return ipv4;
    }

    public static IPv4 newInstance(final byte[] bytes) {
        return newInstance(bytes, 0, bytes.length);
    }

    public static IPv4 newInstance(final byte[] bytes, final int offset, final int length) {
        return newInstance(ByteBuffer.wrap(bytes, offset, length));
    }

    @Override
    public Packet setPacket(final Packet packet) {
        if (packet == null) {
            return this;
        }
        ByteBuffer bb;
        int length = 0;
        int accumulation = 0;

        switch (packet.getClass().getName()) {
            case "TCP":
                TCP tcp = (TCP) packet;
                if (tcp.getChecksum() == 0) {
                    bb = tcp.buffer();
                    length += tcp.getDataOffset() << 2;
                    if (tcp.getPacket() != null) {
                        length += tcp.getPacket().buffer().capacity();
                    }
                    accumulation += (this.getSourceAddress().toInt() >> 16 & 0xffff)
                            + (this.getSourceAddress().toInt() & 0xffff);
                    accumulation += (this.getDestinationAddress().toInt() >> 16 & 0xffff)
                            + (this.getDestinationAddress().toInt() & 0xffff);
                    accumulation += this.getProtocol().getValue() & 0xff;
                    accumulation += length & 0xffff;

                    for (int i = 0; i < length / 2; ++i) {
                        accumulation += 0xffff & bb.getShort();
                    }
                    if (length % 2 > 0) {
                        accumulation += (bb.get() & 0xff) << 8;
                    }

                    accumulation = (accumulation >> 16 & 0xffff)
                            + (accumulation & 0xffff);
                    tcp.setChecksum((short) (~accumulation & 0xffff));
                }
                this.setProtocol(IPProtocolType.TCP);
                this.nextPacket = tcp.buffer();
                return this;
            case "com.ardikars.jxnet.packet.tcp.UDP":
                UDP udp = (UDP) packet;
                if (udp.getChecksum() == 0) {
                    bb = udp.buffer();
                    if (udp.getPacket() != null) {
                        ByteBuffer udpPayload = udp.getPacket().buffer();
                        length += udp.getLength() + ((udpPayload == null) ? 0 : udpPayload.capacity());
                    }
                    if (udp.getChecksum() == 0) {
                        accumulation += (this.getSourceAddress().toInt() >> 16 & 0xffff)
                                + (this.getSourceAddress().toInt() & 0xffff);
                        accumulation += (this.getDestinationAddress().toInt() >> 16 & 0xffff)
                                + (this.getDestinationAddress().toInt() & 0xffff);
                        accumulation += this.getProtocol().getValue() & 0xff;
                        accumulation += length & 0xffff;
                    }
                    for (int i = 0; i < length / 2; ++i) {
                        accumulation += 0xffff & bb.getShort();
                    }
                    if (length % 2 > 0) {
                        accumulation += (bb.get() & 0xff) << 8;
                    }
                    accumulation = (accumulation >> 16 & 0xffff)
                            + (accumulation & 0xffff);
                    udp.setChecksum((short) (~accumulation & 0xffff));
                }
                this.setProtocol(IPProtocolType.UDP);
                this.nextPacket = udp.buffer();
                return this;
            case "ICMPv4":
                this.setProtocol(IPProtocolType.ICMP);
                this.nextPacket = packet.buffer();
                return this;
            default:
                this.nextPacket = packet.buffer();
                return this;
        }
    }

    @Override
    public Packet getPacket() {
        return this.getProtocol().decode(this.nextPacket);
    }

    @Override
    public byte[] bytes() {
        if (this.nextPacket != null) {
            this.nextPacket.rewind();
        }
        int optionsLength = 0;
        if (this.getOptions() != null) {
            optionsLength = this.getOptions().length / 4;
        }
        this.setHeaderLength((byte) (5 + optionsLength));
        this.setTotalLength((short) (this.getHeaderLength() * 4 + (this.nextPacket == null ? 0
                : this.nextPacket.capacity())));

        byte[] data = new byte[this.getTotalLength()];

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.put((byte) ((this.getVersion() & 0xf) << 4 | this.getHeaderLength() & 0xf));
        buffer.put((byte) (((this.getDiffServ() << 2) & 0x3f) | this.getExpCon() & 0x3));
        buffer.putShort(this.getTotalLength());
        buffer.putShort(this.getIdentification());
        buffer.putShort((short) ((this.getFlags() & 0x7) << 13 | this.getFragmentOffset() & 0x1fff));
        buffer.put(this.getTtl());
        buffer.put(this.getProtocol().getValue());
        buffer.putShort((byte) (this.getChecksum() & 0xffff));
        buffer.put(this.getSourceAddress().toBytes());
        buffer.put(this.getDestinationAddress().toBytes());
        if (this.getOptions() != null && this.getHeaderLength() > 5) {
            buffer.put(this.getOptions());
        }
        if (this.getChecksum() == 0) {
            buffer.rewind();
            int accumulation = 0;
            for (int i = 0; i < this.getHeaderLength() * 2; ++i) {
                accumulation += 0xffff & buffer.getShort();
            }
            accumulation = (accumulation >> 16 & 0xffff)
                    + (accumulation & 0xffff);
            this.checksum = ((short) (~accumulation & 0xffff));
            buffer.putShort(10, (short) (this.getChecksum() & 0xffff));
        }
        if (this.nextPacket != null) {
            buffer.put(this.nextPacket);
        }
        return data;
    }

    @Override
    public ByteBuffer buffer() {
        if (this.nextPacket != null) {
            this.nextPacket.rewind();
        }
        int optionsLength = 0;
        if (this.getOptions() != null) {
            optionsLength = this.getOptions().length / 4;
        }
        this.setHeaderLength((byte) (5 + optionsLength));
        this.setTotalLength((short) (this.getHeaderLength() * 4 + (this.nextPacket == null ? 0
                : this.nextPacket.capacity())));

        ByteBuffer buffer = ByteBuffer.allocateDirect(this.getTotalLength());
        buffer.put((byte) ((this.getVersion() & 0xf) << 4 | this.getHeaderLength() & 0xf));
        buffer.put((byte) (((this.getDiffServ() << 2) & 0x3f) | this.getExpCon() & 0x3));
        buffer.putShort(this.getTotalLength());
        buffer.putShort(this.getIdentification());
        buffer.putShort((short) ((this.getFlags() & 0x7) << 13 | this.getFragmentOffset() & 0x1fff));
        buffer.put(this.getTtl());
        buffer.put(this.getProtocol().getValue());
        buffer.putShort((byte) (this.getChecksum() & 0xffff));
        buffer.put(this.getSourceAddress().toBytes());
        buffer.put(this.getDestinationAddress().toBytes());
        if (this.getOptions() != null && this.getHeaderLength() > 5) {
            buffer.put(this.getOptions());
        }
        if (this.getChecksum() == 0) {
            buffer.rewind();
            int accumulation = 0;
            for (int i = 0; i < this.getHeaderLength() * 2; ++i) {
                accumulation += 0xffff & buffer.getShort();
            }
            accumulation = (accumulation >> 16 & 0xffff)
                    + (accumulation & 0xffff);
            this.checksum = ((short) (~accumulation & 0xffff));
            buffer.putShort(10, (short) (this.getChecksum() & 0xffff));
        }
        if (this.nextPacket != null) {
            buffer.put(this.nextPacket);
        }
        return buffer;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("[")
                .append("Version: " + this.getVersion())
                .append(", Internet Header Length: " + this.getHeaderLength())
                .append(", Differentiated Services Code Point: " + this.getDiffServ())
                .append(", Explicit Congestion Notification: " + this.getExpCon())
                .append(", Total Length: " + this.getTotalLength())
                .append(", Identification: " + this.getIdentification())
                .append(", Flags: " + this.getFlags())
                .append(", Fragment Offset: " + this.getFragmentOffset())
                .append(", Time To Live: " + this.getTtl())
                .append(", IPProtocolType: " + this.getProtocol().getName())
                .append(", Header Checksum: " + this.getChecksum())
                .append(", Source Address: " + this.getSourceAddress().toString())
                .append(", Destination Address: " + this.getDestinationAddress().toString())
                .append(", NeighborDiscoveryOptions: " + this.getOptions())
                .append("]").toString();
    }

}
