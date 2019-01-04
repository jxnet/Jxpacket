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

package com.ardikars.jxpacket.common;

import com.ardikars.jxpacket.common.util.PacketIterator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Abstraction for protocol codec.
 * @author Ardika Rommy Sanjaya
 * @since 1.5.0
 */
public abstract class AbstractPacket implements Packet {

    protected ByteBuf payloadBuffer;

    /**
     * Returns the {@link ByteBuf} object representing this packet's payload.
     * @return returns empty buffer if a payload doesn't exits, {@link ByteBuf} object otherwise.
     */
    public ByteBuf getPayloadBuffer() {
        if (payloadBuffer == null) {
            payloadBuffer = Unpooled.EMPTY_BUFFER;
        }
        return payloadBuffer;
    }

    @Override
    public <T extends Packet> boolean contains(Class<T> clazz) {
        return !get(clazz).isEmpty();
    }

    @Override
    public <T extends Packet> List<T> get(Class<T> clazz) {
        List<Packet> packets = new ArrayList<Packet>();
        Iterator<Packet> iterator = this.iterator();
        while (iterator.hasNext()) {
            Packet packet = iterator.next();
            if (clazz.isInstance(packet)) {
                packets.add(packet);
            }
        }
        return (List<T>) packets;
    }

    @Override
    public PacketIterator iterator() {
        return new PacketIterator(this);
    }

    public static abstract class Header implements Packet.Header {

        protected static final ByteBufAllocator ALLOCATOR = Properties.BYTE_BUF_ALLOCATOR;

        protected ByteBuf buffer;

        /**
         * Returns header as byte buffer.
         * @return return byte buffer.
         */
        public ByteBuf getBuffer() {
            if (buffer == null) {
                buffer = Unpooled.EMPTY_BUFFER;
            }
            return buffer;
        }

    }

    /**
     * Packet builder.
     */
    public static abstract class Builder implements com.ardikars.common.util.Builder<Packet, ByteBuf> {

    }

    /**
     * Packet factory.
     */
    public static abstract class Factory implements com.ardikars.common.util.Factory<Packet, ByteBuf> {

    }

}
