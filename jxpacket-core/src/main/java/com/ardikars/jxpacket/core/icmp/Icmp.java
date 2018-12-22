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

import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.common.AbstractPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public abstract class Icmp extends AbstractPacket {

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
        public ByteBuf getBuffer() {
            if (buffer == null) {
                buffer = ALLOCATOR.directBuffer(getLength());
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

    public static class IcmpTypeAndCode {

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

    }

}
