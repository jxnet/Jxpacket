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

package com.ardikars.jxpacket.jxnet;

import com.ardikars.jxnet.*;
import com.ardikars.jxpacket.common.*;
import com.ardikars.jxpacket.core.ethernet.Ethernet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jxpacket 2018/11/06
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
public class JxnetPacket implements Jxpacket {

    private Context context;

    public JxnetPacket(Context context) {
        this.context = context;
    }

    @Override
    public PacketCode inject(Packet packet) {
        AtomicInteger packetLength = new AtomicInteger();
        packet.iterator().forEachRemaining(pkt -> packetLength.addAndGet(packet.getHeader().getLength()));
        ByteBuffer buffer = ByteBuffer.allocateDirect(packetLength.get());
        packet.iterator().forEachRemaining(pkt -> {
            pkt.getHeader().getBuffer().getBytes(0, buffer);
        });
        PcapCode pcapCode = context.pcapSendPacket(buffer, buffer.capacity());
        return parsePacketCode(pcapCode);
    }

    @Override
    public PacketCode inject(ByteBuf buffer) {
        if (!buffer.isDirect()) {
            throw new IllegalArgumentException("Buffer should be \'direct buffer\'.");
        }
        PcapCode pcapCode = context.pcapSendPacket(buffer.nioBuffer(), buffer.capacity());
        return parsePacketCode(pcapCode);
    }

    @Override
    public <T, U> PacketCode capture(int count, Listener<T, U> packet, U argument) {
        PcapCode pcapCode;
        if (PacketListener.class.isInstance(packet)) {
            DataLinkType dataLinkType = context.pcapDataLink();
            if (dataLinkType == DataLinkType.EN10MB) {
                pcapCode = context.pcapLoop(count, (user, h, bytes) -> {
                    ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(bytes.capacity());
                    buffer.setBytes(0, bytes);
                    Packet pkt = Ethernet.newPacket(buffer);
                    packet.receive((T) pkt, user);
                }, argument);
            } else {
                pcapCode = context.pcapLoop(count, (user, h, bytes) -> {
                    ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(bytes.capacity());
                    buffer.setBytes(0, bytes);
                    Packet pkt = UnknownPacket.newPacket(buffer);
                    packet.receive((T) pkt, user);
                }, argument);
            }
        } else if (ByteBuf.class.isInstance(packet)) {
            pcapCode = context.pcapLoop(count, (user, h, bytes) -> {
                ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(bytes.capacity());
                buffer.setBytes(0, bytes);
                packet.receive((T) buffer, user);
            }, argument);
        } else {
            throw new IllegalArgumentException("Unknown callback listener.");
        }
        return parsePacketCode(pcapCode);
    }

    private PacketCode parsePacketCode(PcapCode pcapCode) {
        if (pcapCode == null) {
            return PacketCode.ERROR;
        }
        if (pcapCode.getValue() == 0) {
            return PacketCode.OK;
        } else if (pcapCode.getValue() > 1) {
            return PacketCode.WARNING;
        } else {
            return PacketCode.ERROR;
        }
    }

}
