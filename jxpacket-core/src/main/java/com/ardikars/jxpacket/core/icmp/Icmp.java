package com.ardikars.jxpacket.core.icmp;

import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.common.AbstractPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.ByteBuffer;

public abstract class Icmp extends AbstractPacket {

    protected static abstract class IcmpHeader implements Header {

        public static int ICMP_HEADER_LENGTH = 4;

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
            ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(getLength());
            buffer.setShort(0, typeAndCode.getType());
            buffer.setShort(1, typeAndCode.getCode());
            buffer.setShort(2, checksum);
            return buffer;
        }

    }

    protected static abstract class IcmpPacketBuilder implements Builder {

        protected IcmpTypeAndCode typeAndCode;
        protected short checksum;

        protected ByteBuffer payloadBuffer;

        public IcmpPacketBuilder typeAndCode(IcmpTypeAndCode typeAndCode) {
            this.typeAndCode = typeAndCode;
            return this;
        }

        public IcmpPacketBuilder payloadBuffer(ByteBuffer buffer) {
            this.payloadBuffer = buffer;
            return this;
        }

    }

    public static class IcmpTypeAndCode {

        private byte type;
        private byte code;
        private String name;

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
            final StringBuilder sb = new StringBuilder("IcmpTypeAndCode{");
            sb.append("type=").append(type);
            sb.append(", code=").append(code);
            sb.append(", name='").append(name).append('\'');
            sb.append('}');
            return sb.toString();
        }

    }

}
