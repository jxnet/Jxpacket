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

package com.ardikars.jxpacket.common.layer;

import com.ardikars.common.memory.Memory;
import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.common.UnknownPacket;

import java.util.HashMap;
import java.util.Map;

public final class ApplicationLayer extends NamedNumber<Short, ApplicationLayer> implements Packet.Factory {

    private static final Map<ApplicationLayer, Short> registry =
            new HashMap<ApplicationLayer, Short>();

    private static final Map<Short, AbstractPacket.Builder> builder =
            new HashMap<Short, AbstractPacket.Builder>();

    public ApplicationLayer(Short value, String name) {
        super(value, name);
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
     * @param value value.
     * @return returns {@link DataLinkLayer} object.
     */
    public static ApplicationLayer valueOf(short value) {
        for (Map.Entry<ApplicationLayer, Short> entry : registry.entrySet()) {
            if (entry.getValue() == value) {
                return entry.getKey();
            }
        }
        return new ApplicationLayer((short) -1, "Unknown");
    }

    /**
     *
     * @param dataLinkLayer application type.
     */
    public static void register(ApplicationLayer dataLinkLayer) {
        synchronized (registry) {
            registry.put(dataLinkLayer, dataLinkLayer.getValue());
        }
    }

    /**
     *
     * @param dataLinkLayer application type.
     * @param packetBuilder packet builder.
     */
    public static void register(ApplicationLayer dataLinkLayer, AbstractPacket.Builder packetBuilder) {
        synchronized (builder) {
            builder.put(dataLinkLayer.getValue(), packetBuilder);
        }
    }

}
