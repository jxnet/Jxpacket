package com.ardikars.jxpacket.icmp;

import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.AbstractPacket;
import io.netty.buffer.ByteBuf;

public abstract class Icmp extends AbstractPacket {

    protected static abstract class IcmpHeader extends AbstractPacket.PacketHeader {

        public static int ICMP_HEADER_LENGTH = 4;

        private IcmpTypeAndCode typeAndCode;
        private short checksum;

        @Override
        public abstract <T extends NamedNumber> T getPayloadType();

        @Override
        public int getLength() {
            return ICMP_HEADER_LENGTH;
        }

        @Override
        public abstract ByteBuf getBuffer();

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
