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

import com.ardikars.common.memory.Memory;
import com.ardikars.common.memory.MemoryAllocator;
import com.ardikars.common.util.CommonConsumer;
import com.ardikars.jxpacket.common.util.PacketIterator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Abstraction for protocol codec.
 * @author Ardika Rommy Sanjaya
 * @since 1.5.0
 */
public abstract class AbstractPacket implements Packet {

    protected static final IllegalArgumentException ILLEGAL_HEADER_EXCEPTION
            = new IllegalArgumentException("Missing required header field(s).");

    protected Memory payloadBuffer;

    /**
     * Returns the {@link Memory} object representing this packet's payload.
     * @return returns empty buffer if a payload doesn't exits, {@link Memory} object otherwise.
     */
    public Memory getPayloadBuffer() {
        if (payloadBuffer == null) {
            payloadBuffer = Properties.BYTE_BUF_ALLOCATOR.allocate(0);
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

    @Override
    public void forEach(CommonConsumer<? super Packet> action) throws NullPointerException {
        PacketIterator iterator = iterator();
        while (iterator.hasNext()) {
            try {
                action.consume(iterator.next());
            } catch (Exception e) {
                // do nothing
            }
        }
    }

    public static abstract class Header implements Packet.Header {

        protected static final MemoryAllocator ALLOCATOR = Properties.BYTE_BUF_ALLOCATOR;

        protected Memory buffer;

        /**
         * Returns header as byte buffer.
         * @return return byte buffer.
         */
        public Memory getBuffer() {
            if (buffer == null) {
                buffer = ALLOCATOR.allocate(0);
            }
            return buffer;
        }

        public abstract Builder getBuilder();

    }

    /**
     * Packet builder.
     */
    public static abstract class Builder implements com.ardikars.common.util.Builder<Packet, Memory>, Serializable {

        public void reset() {
            reset(-1, -1);
        }

        public void reset(int offset, int length) {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

    }

    /**
     * Packet factory.
     */
    public static abstract class Factory implements com.ardikars.common.util.Factory<Packet, Memory> {

    }

}
