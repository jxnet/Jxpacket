package com.ardikars.jxpacket.udp;

import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.AbstractPacket;
import com.ardikars.jxpacket.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class Udp extends AbstractPacket {

    private final Udp.Header header;
    private final Packet payload;

    private Udp(final Builder builder) {
        this.header = new Header(builder);
        this.payload = null;
    }

    @Override
    public Udp.Header getHeader() {
        return this.header;
    }

    @Override
    public Packet getPayload() {
        return this.payload;
    }


    public static class Header extends PacketHeader {

        public static final int UDP_HEADER_LENGTH = 8;

        private short sourcePort;
        private short destinationPort;
        private short length;
        private short checksum;

        private Header(final Builder builder) {
            this.sourcePort = builder.sourcePort;
            this.destinationPort = builder.destinationPort;
            this.length = builder.length;
            this.checksum = builder.checksum;
        }

        public int getSourcePort() {
            return sourcePort & 0xffff;
        }

        public int getDestinationPort() {
            return destinationPort & 0xffff;
        }

        public int getChecksum() {
            return checksum & 0xffff;
        }

        @Override
        public <T extends NamedNumber> T getPayloadType() {
            return null;
        }

        @Override
        public int getLength() {
            return length & 0xffff;
        }

        @Override
        public ByteBuf getBuffer() {
            ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(getLength());
            buffer.setShort(0, this.sourcePort);
            buffer.setShort(2, this.destinationPort);
            buffer.setShort(4, this.length);
            buffer.setShort(6, this.checksum);
            return buffer;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("HeaderAbstract{");
            sb.append("sourcePort=").append(sourcePort & 0xffff);
            sb.append(", destinationPort=").append(destinationPort & 0xffff);
            sb.append(", length=").append(length & 0xffff);
            sb.append(", checksum=").append(checksum & 0xffff);
            sb.append('}');
            return sb.toString();
        }

    }

    public static class Builder extends PacketBuilder {

        private short sourcePort;
        private short destinationPort;
        private short length;
        private short checksum;

        private ByteBuf payloadBuffer;

        public Builder sourcePort(int sourcePort) {
            this.sourcePort = (short) (sourcePort & 0xffff);
            return this;
        }

        public Builder destinationPort(int destinationPort) {
            this.destinationPort = (short) (destinationPort & 0xffff);
            return this;
        }

        public Builder length(int length) {
            this.length = (short) (length & 0xffff);
            return this;
        }

        public Builder checksum(int checksum) {
            this.checksum = (short) (checksum & 0xffff);
            return this;
        }

        public Builder payloadBuffer(ByteBuf payloadBuffer) {
            this.payloadBuffer = payloadBuffer;
            return this;
        }

        @Override
        public Packet build() {
            return new Udp(this);
        }

        @Override
        public Packet build(ByteBuf buffer) {
            this.sourcePort = buffer.getShort(0);
            this.destinationPort = buffer.getShort(2);
            this.length = buffer.getShort(4);
            this.checksum = buffer.getShort(6);
            this.payloadBuffer = buffer.copy(8, buffer.capacity() - 8);
            return new Udp(this);
        }

    }

}
