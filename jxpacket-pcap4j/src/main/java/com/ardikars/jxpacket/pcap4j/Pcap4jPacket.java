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

package com.ardikars.jxpacket.pcap4j;

import com.ardikars.jxpacket.common.*;
import com.ardikars.jxpacket.common.api.Jxpacket;
import com.ardikars.jxpacket.common.api.Listener;
import com.ardikars.jxpacket.common.api.PacketCode;
import com.ardikars.jxpacket.common.api.PacketListener;
import com.ardikars.jxpacket.core.ethernet.Ethernet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.RawPacketListener;
import org.pcap4j.packet.namednumber.DataLinkType;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jxpacket 2018/11/06
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
public class Pcap4jPacket implements Jxpacket {

    private PcapHandle context;

    public Pcap4jPacket(PcapHandle context) {
        this.context = context;
    }

    @Override
    public PacketCode inject(Packet packet) {
        AtomicInteger packetLength = new AtomicInteger();
        packet.iterator().forEachRemaining(pkt -> packetLength.addAndGet(packet.getHeader().getLength()));
        byte[] buffer = new byte[packetLength.get()];
        AtomicInteger index = new AtomicInteger();
        packet.iterator().forEachRemaining(pkt -> {
            int length = pkt.getHeader().getLength();
            pkt.getHeader().getBuffer().getBytes(0, buffer, index.addAndGet(length), length);
        });
        try {
            context.sendPacket(buffer);
        } catch (NotOpenException e) {
            return PacketCode.ERROR;
        } catch (PcapNativeException e) {
            return PacketCode.ERROR;
        }
        return PacketCode.OK;
    }

    @Override
    public PacketCode inject(ByteBuf buffer) {
        byte[] rawBuffer = new byte[buffer.capacity()];
        buffer.getBytes(0, rawBuffer);
        try {
            context.sendPacket(rawBuffer);
        } catch (NotOpenException e) {
            return PacketCode.ERROR;
        } catch (PcapNativeException e) {
            return PacketCode.ERROR;
        }
        return PacketCode.OK;
    }

    @Override
    public <T, U> PacketCode capture(int count, Listener<T, U> packet, U argument) {
        if (PacketListener.class.isInstance(packet)) {
            DataLinkType dataLinkType = context.getDlt();
            if (dataLinkType.compareTo(DataLinkType.EN10MB) == 0) {
                try {
                    context.loop(count, (RawPacketListener) bytes -> {
                        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(bytes.length);
                        buffer.setBytes(0, bytes);
                        Packet pkt = Ethernet.newPacket(buffer);
                        packet.receive((T) pkt, argument);
                    });
                } catch (PcapNativeException e) {
                    return PacketCode.ERROR;
                } catch (InterruptedException e) {
                    return PacketCode.WARNING;
                } catch (NotOpenException e) {
                    return PacketCode.ERROR;
                }
                return PacketCode.OK;
            } else {
                try {
                    context.loop(count, (RawPacketListener) bytes -> {
                        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(bytes.length);
                        buffer.setBytes(0, bytes);
                        Packet pkt = UnknownPacket.newPacket(buffer);
                        packet.receive((T) pkt, argument);
                    });
                } catch (PcapNativeException e) {
                    return PacketCode.ERROR;
                } catch (InterruptedException e) {
                    return PacketCode.WARNING;
                } catch (NotOpenException e) {
                    return PacketCode.ERROR;
                }
                return PacketCode.OK;
            }
        } else {
            try {
                context.loop(count, (RawPacketListener) bytes -> {
                    ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(bytes.length);
                    buffer.setBytes(0, bytes);
                    packet.receive((T) buffer, argument);
                });
            } catch (PcapNativeException e) {
                return PacketCode.ERROR;
            } catch (InterruptedException e) {
                return PacketCode.WARNING;
            } catch (NotOpenException e) {
                return PacketCode.ERROR;
            }
            return PacketCode.OK;
        }
    }

}
