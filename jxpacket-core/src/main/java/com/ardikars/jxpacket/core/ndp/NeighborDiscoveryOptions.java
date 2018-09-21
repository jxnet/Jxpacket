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

package com.ardikars.jxpacket.core.ndp;

import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NeighborDiscoveryOptions extends AbstractPacket {

    private final NeighborDiscoveryOptions.Header header;
    private final ByteBuf payload;

    public NeighborDiscoveryOptions(Builder builder) {
        this.header = new Header(builder);
        this.payload = null;
    }

    @Override
    public Header getHeader() {
        return header;
    }

    @Override
    public Packet getPayload() {
        return null;
    }

    public static class Header implements Packet.Header {

        private List<Option> options;

        private Header(Builder builder) {
            this.options = builder.options;
        }

        @Override
        public <T extends NamedNumber> T getPayloadType() {
            return null;
        }

        @Override
        public int getLength() {
            int length = 0;
            for (Option option : this.options) {
                length += (option.getLength() << 3);
            }
            return length;
        }

        @Override
        public ByteBuf getBuffer() {
            ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(getLength());
            for (Option option : this.options) {
                buffer.setByte(0, option.getType().getValue());
                buffer.setByte(1, option.getLength());
                buffer.setBytes(2, option.getData());
                int index = 2 + option.getData().length;
                int paddingLength = (option.getLength() << 3) - (option.getData().length + 2);
                for (int i = 0; i < paddingLength; i++) {
                    buffer.setByte(index + i, (byte) 0);
                }
            }
            return buffer;
        }

        @Override
        public String toString() {
            return new StringBuilder("Header{")
                    .append("options=").append(options)
                    .append('}')
                    .toString();
        }

    }

    public static class OptionType extends NamedNumber<Byte, OptionType> {

        public static OptionType SOURCE_LINK_LAYER_ADDRESS =
                new OptionType((byte) 1, "Source link layer addresss");

        public static OptionType TARGET_LINK_LAYER_ADDRESS =
                new OptionType((byte) 2, "Target link layer addresss");

        public static OptionType PREFIX_INFORMATION =
                new OptionType((byte) 3, "Prefix information");

        public static OptionType REDIRECT_HEADER =
                new OptionType((byte) 4, "Redirect header");

        public static OptionType MTU =
                new OptionType((byte) 5, "MTU");

        protected OptionType(Byte value, String name) {
            super(value, name);
        }

        private static Map<Byte, OptionType> registry =
                new HashMap<>();

        static {
            registry.put(SOURCE_LINK_LAYER_ADDRESS.getValue(), SOURCE_LINK_LAYER_ADDRESS);
            registry.put(TARGET_LINK_LAYER_ADDRESS.getValue(), TARGET_LINK_LAYER_ADDRESS);
            registry.put(PREFIX_INFORMATION.getValue(), PREFIX_INFORMATION);
            registry.put(REDIRECT_HEADER.getValue(), REDIRECT_HEADER);
            registry.put(MTU.getValue(), MTU);
        }

    }

    public static final class Option {

        private OptionType type;
        private byte length;
        private byte[] data;

        private Option(OptionType type, byte[] data) {
            this.type = type;
            this.data = data;
            this.length = (byte) ((this.data.length + 2 + 7) >> 3);
        }

        public OptionType getType() {
            return this.type;
        }

        public byte getLength() {
            return this.length;
        }

        public byte[] getData() {
            return this.data;
        }

        @Override
        public String toString() {
            return new StringBuilder("[")
                    .append("Type: ")
                    .append(this.getType())
                    .append(", Data: ")
                    .append(Arrays.toString(this.getData()))
                    .append("]").toString();
        }

    }

    public static class Builder implements Packet.Builder {

        private List<Option> options = new ArrayList<>();

        public Builder options(List<Option> options) {
            this.options = options;
            return this;
        }

        @Override
        public Packet build() {
            return new NeighborDiscoveryOptions(this);
        }

        @Override
        public Packet build(ByteBuf buffer) {
            while (buffer.isReadable(2)) {
                final OptionType type = OptionType.registry.get(buffer.readByte());
                byte lengthField = buffer.readByte();
                int dataLength = lengthField * 8;
                if (dataLength < 2) {
                    break;
                }
                dataLength -= 2;
                if (!buffer.isReadable(dataLength)) {
                    break;
                }
                byte[] data = new byte[dataLength];
                buffer.readBytes(data, 0, dataLength);
                options.add(new Option(type, data));
            }
            return new NeighborDiscoveryOptions(this);
        }

    }

}
