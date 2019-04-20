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

package com.ardikars.jxpacket.core.icmp;

import com.ardikars.common.memory.Memory;
import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.common.UnknownPacket;
import com.ardikars.jxpacket.core.ndp.NeighborAdvertisement;
import com.ardikars.jxpacket.core.ndp.NeighborSolicitation;
import com.ardikars.jxpacket.core.ndp.Redirect;
import com.ardikars.jxpacket.core.ndp.RouterAdvertisement;
import com.ardikars.jxpacket.core.ndp.RouterSolicitation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class Icmp extends AbstractPacket {

    protected static IcmpTypeAndCode findIcmpTypeAndCode(byte type, byte code, Collection<IcmpTypeAndCode> typeAndCodes) {
        Iterator<IcmpTypeAndCode> icmpTypeAndCodeIterator = typeAndCodes.iterator();
        while (icmpTypeAndCodeIterator.hasNext()) {
            Icmp.IcmpTypeAndCode typeAndCode = icmpTypeAndCodeIterator.next();
            if (typeAndCode.getType() == type && typeAndCode.getCode() == code) {
                return typeAndCode;
            }
        }
        return new Icmp.IcmpTypeAndCode(type, code, "Unknown");
    }

    protected static abstract class AbstractPacketHeader extends Header {

        public static final int ICMP_HEADER_LENGTH = 4;

        protected IcmpTypeAndCode typeAndCode;
        protected short checksum;

        @Override
        public abstract <T extends NamedNumber> T getPayloadType();

        @Override
        public int getLength() {
            return ICMP_HEADER_LENGTH;
        }

        @Override
        public Memory getBuffer() {
            if (buffer == null) {
                buffer = ALLOCATOR.allocate(getLength());
                buffer.writeByte(typeAndCode.getType());
                buffer.writeByte(typeAndCode.getCode());
                buffer.writeShort(checksum);
            }
            return buffer;
        }

    }

    protected static abstract class AbstractPacketBuilder extends Builder {

        protected IcmpTypeAndCode typeAndCode;
        protected short checksum;

        public AbstractPacketBuilder typeAndCode(IcmpTypeAndCode typeAndCode) {
            this.typeAndCode = typeAndCode;
            return this;
        }

        public AbstractPacketBuilder checksum(short checksum) {
            this.checksum = checksum;
            return this;
        }

    }

    public static class IcmpTypeAndCode extends NamedNumber<Byte, IcmpTypeAndCode> implements Packet.Factory {

        public static final IcmpTypeAndCode NEIGHBOR_SOLICITATION
                = new IcmpTypeAndCode((byte) 0x87, (byte) 0x0, "Neighbor Solicitation");

        public static final IcmpTypeAndCode NEIGHBOR_ADVERTISEMENT
                = new IcmpTypeAndCode((byte) 0x88, (byte) 0x0, "Neighbor Advertisement");

        public static final IcmpTypeAndCode ROUTER_SOLICICATION
                = new IcmpTypeAndCode((byte) 0x85, (byte) 0x0, "Router Solicitation");

        public static final IcmpTypeAndCode ROUTER_ADVERTISEMENT
                = new IcmpTypeAndCode((byte) 0x86, (byte) 0x0, "Router Advertisement");

        public static final IcmpTypeAndCode REDIRECT
                = new IcmpTypeAndCode((byte) 0x89, (byte) 0x0, "Redirect");

        public static final IcmpTypeAndCode UNKNOWN = new IcmpTypeAndCode((byte) -1, (byte) -1, "Unknown");

        private static Map<Byte, IcmpTypeAndCode> registry = new HashMap<Byte, IcmpTypeAndCode>();

        private static Map<Byte, AbstractPacket.Builder> builder = new HashMap<Byte, AbstractPacket.Builder>();

        private final byte type;
        private final byte code;
        private final String name;

        /**
         * Create new instance for {@link IcmpTypeAndCode}.
         * @param type icmp type.
         * @param code icmp code.
         * @param name icmp name.
         */
        public IcmpTypeAndCode(byte type, byte code, String name) {
            super(type, name);
            this.type = type;
            this.code = code;
            this.name = name;
        }

        public byte getType() {
            return type;
        }

        public byte getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return new StringBuilder("IcmpTypeAndCode{")
                    .append("type=").append(type)
                    .append(", code=").append(code)
                    .append(", name='").append(name).append('\'')
                    .append('}').toString();
        }

        @Override
        public Packet newInstance(Memory buffer) {
            AbstractPacket.Builder packetBuilder = builder.get(this.getValue());
            if (packetBuilder == null) {
                if (buffer == null || buffer.capacity() <= 0) {
                    return null;
                }
                return new UnknownPacket.Builder().build(buffer);
            }
            return packetBuilder.build(buffer);
        }

        /**
         *
         * @param value value.
         * @return returns {@link IcmpTypeAndCode} object.
         */
        public static IcmpTypeAndCode valueOf(final Byte value) {
            IcmpTypeAndCode icmpTypeAndCode = registry.get(value);
            if (icmpTypeAndCode == null) {
                return UNKNOWN;
            } else {
                return icmpTypeAndCode;
            }
        }

        /**
         *
         * @param type type
         */
        public static void register(final IcmpTypeAndCode type) {
            registry.put(type.getValue(), type);
        }

        /**
         *
         * @param type type.
         * @param packetBuilder packet builder.
         */
        public static void register(IcmpTypeAndCode type, AbstractPacket.Builder packetBuilder) {
            builder.put(type.getValue(), packetBuilder);
        }

        static {
            registry.put(ROUTER_SOLICICATION.getValue(), ROUTER_SOLICICATION);
            registry.put(ROUTER_ADVERTISEMENT.getValue(), ROUTER_ADVERTISEMENT);
            registry.put(NEIGHBOR_SOLICITATION.getValue(), NEIGHBOR_SOLICITATION);
            registry.put(NEIGHBOR_ADVERTISEMENT.getValue(), NEIGHBOR_ADVERTISEMENT);
            registry.put(REDIRECT.getValue(), REDIRECT);
            Icmp.IcmpTypeAndCode.register(Icmp.IcmpTypeAndCode.NEIGHBOR_SOLICITATION, new NeighborSolicitation.Builder());
            Icmp.IcmpTypeAndCode.register(Icmp.IcmpTypeAndCode.NEIGHBOR_ADVERTISEMENT, new NeighborAdvertisement.Builder());
            Icmp.IcmpTypeAndCode.register(Icmp.IcmpTypeAndCode.ROUTER_SOLICICATION, new RouterSolicitation.Builder());
            Icmp.IcmpTypeAndCode.register(Icmp.IcmpTypeAndCode.ROUTER_ADVERTISEMENT, new RouterAdvertisement.Builder());
            Icmp.IcmpTypeAndCode.register(Icmp.IcmpTypeAndCode.REDIRECT, new Redirect.Builder());
        }

    }

}
