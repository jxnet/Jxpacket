package com.ardikars.jxpacket.iso8583;

import com.ardikars.common.annotation.Incubating;
import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import io.netty.buffer.ByteBuf;

@Incubating
public class Iso8583 extends AbstractPacket {


    public Iso8583(Builder builder) {

    }

    @Override
    public Header getHeader() {
        return null;
    }

    @Override
    public Packet getPayload() {
        return null;
    }

    public static class Header implements Packet.Header {

        private short messageLength;
        private int mit;
        private byte[] bitmap;

        @Override
        public <T extends NamedNumber> T getPayloadType() {
            return null;
        }

        @Override
        public int getLength() {
            return 0;
        }

        @Override
        public ByteBuf getBuffer() {
            return null;
        }

    }

    public static class Builder implements Packet.Builder {

        @Override
        public Packet build() {
            return null;
        }

        @Override
        public Packet build(ByteBuf value) {
            return null;
        }

    }

}
