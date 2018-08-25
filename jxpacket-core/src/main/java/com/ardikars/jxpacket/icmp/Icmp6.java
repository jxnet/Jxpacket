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

package com.ardikars.jxpacket.icmp;

import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.AbstractPacket;
import com.ardikars.jxpacket.Packet;
import io.netty.buffer.ByteBuf;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.0
 */
public class Icmp6 extends AbstractPacket {

    @Override
    public HeaderAbstract getHeader() {
        return null;
    }

    @Override
    public Packet getPayload() {
        return null;
    }

    public static class HeaderAbstract extends Icmp.IcmpHeader {

        @Override
        public <T extends NamedNumber> T getPayloadType() {
            return null;
        }

        @Override
        public ByteBuf getBuffer() {
            return null;
        }

    }

    //    public static Icmp6 newInstance(final ByteBuffer buffer) {
//        Icmp6 icmp = new Icmp6();
//        icmp.setTypeAndCode(IcmpTypeAndCode.getTypeAndCode(buffer.get(), buffer.get()));
//        icmp.setChecksum(buffer.getShort());
//        icmp.nextPacket = buffer.slice();
//        return icmp;
//    }
//
//    public static Icmp6 newInstance(final byte[] bytes) {
//        return newInstance(bytes, 0, bytes.length);
//    }
//
//    public static Icmp6 newInstance(final byte[] bytes, final int offset, final int length) {
//        return newInstance(ByteBuffer.wrap(bytes, offset, length));
//    }
//
//    @Override
//    public Packet setPacket(final Packet packet) {
//        if (packet == null) {
//            return this;
//        }
//        switch (packet.getClass().getName()) {
//            default:
//                this.nextPacket = packet.buffer();
//                return this;
//        }
//    }
//
//    @Override
//    public Packet getPacket() {
//        return getTypeAndCode().decode(nextPacket);
//    }
//
//    @Override
//    public byte[] bytes() {
//        if (this.nextPacket != null) {
//            this.nextPacket.rewind();
//        }
//        byte[] data = new byte[ICMP_HEADER_LENGTH + ((this.nextPacket == null) ? 0 : this.nextPacket.capacity())];
//        ByteBuffer buffer = ByteBuffer.wrap(data);
//        buffer.put(this.getTypeAndCode().getType());
//        buffer.put(this.getTypeAndCode().getCode());
//        buffer.putShort(this.getChecksum());
//        if (this.nextPacket != null) {
//            buffer.put(this.nextPacket);
//        }
//        return data;
//    }
//
//    @Override
//    public ByteBuffer buffer() {
//        if (this.nextPacket != null) {
//            this.nextPacket.rewind();
//        }
//        ByteBuffer buffer = ByteBuffer
//                .allocateDirect(ICMP_HEADER_LENGTH + ((this.nextPacket == null) ? 0 : this.nextPacket.capacity()));
//        buffer.put(this.getTypeAndCode().getType());
//        buffer.put(this.getTypeAndCode().getCode());
//        buffer.putShort(this.getChecksum());
//        if (this.nextPacket != null) {
//            buffer.put(this.nextPacket);
//        }
//        return buffer;
//    }

}
