package com.ardikars.jxpacket.tcp;

import com.ardikars.common.util.Validate;
import com.ardikars.jxpacket.AbstractPacket;
import com.ardikars.jxpacket.Packet;
import com.ardikars.jxpacket.ProtocolType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class Tcp extends AbstractPacket {

    private final Tcp.Header header;
    private final Packet payload;

    private Tcp(final Builder builder) {
        this.header = new Tcp.Header(builder);
        this.payload = super.getPayloadBuilder(this.header)
                .build(builder.payloadBuffer);
    }

    @Override
    public Tcp.Header getHeader() {
        return null;
    }

    @Override
    public Packet getPayload() {
        return null;
    }

    /**
     * @see <a href="https://tools.ietf.org/html/rfc793">TCP</a>
     */
    public static final class Header extends PacketHeader {

        public static int TCP_HEADER_LENGTH = 20;

        private short sourcePort;
        private short destinationPort;
        private int sequence;
        private int acknowledge;
        private byte dataOffset;
        private TcpFlags flags;
        private short windowSize;
        private short checksum;
        private short urgentPointer;
        private byte[] options;

        private Header(final Builder builder) {
            this.sourcePort = builder.sourcePort;
            this.destinationPort = builder.destinationPort;
            this.sequence = builder.sequence;
            this.acknowledge = builder.acknowledge;
            this.dataOffset = builder.dataOffset;
            this.flags = builder.flags;
            this.windowSize = builder.windowSize;
            this.checksum = builder.checksum;
            this.urgentPointer = builder.urgentPointer;
            this.options = builder.options;
        }

        public short getSourcePort() {
            return (short) (this.sourcePort & 0xffff);
        }

        public short getDestinationPort() {
            return (short) (this.destinationPort & 0xffff);
        }

        public int getSequence() {
            return (int) (this.sequence & 0xffffffffL);
        }

        public int getAcknowledge() {
            return (int) (this.acknowledge & 0xffffffffL);
        }

        public byte getDataOffset() {
            return (byte) (this.dataOffset & 0xf);
        }

        public TcpFlags getFlags() {
            return flags;
        }

        public short getWindowSize() {
            return (short) (this.windowSize & 0xffff);
        }

        public short getChecksum() {
            return (short) (this.checksum & 0xffff);
        }

        public short getUrgentPointer() {
            return (short) (this.urgentPointer & 0xffff);
        }

        public byte[] getOptions() {
            if (options == null) {
                return new byte[0];
            }
            byte[] buffer = new byte[this.options.length];
            System.arraycopy(options, 0, buffer, 0, buffer.length);
            return buffer;
        }

        @Override
        public ProtocolType getPayloadType() {
            return ProtocolType.UNKNOWN;
        }

        @Override
        public int getLength() {
            int length = TCP_HEADER_LENGTH;
            if (this.options != null) {
                length += this.options.length;
            }
            return length;
        }

        @Override
        public ByteBuf getBuffer() {
            ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(getLength());
            buffer.setShort(0, this.sourcePort);
            buffer.setShort(2, this.destinationPort);
            buffer.setIndex(4, this.sequence);
            buffer.setIndex(8, this.acknowledge);
            buffer.setShort(12, (short) ((this.flags.getShortValue() & 0x1ff) | (this.dataOffset & 0xf) << 12));
            buffer.setShort(14, this.windowSize);
            buffer.setShort(16, this.checksum);
            buffer.setShort(18, this.urgentPointer);
            if (this.options != null) {
                buffer.setBytes(20, this.options);
            }
            return buffer;
        }

    }

    public static class Builder extends PacketBuilder {

        private short sourcePort;
        private short destinationPort;
        private int sequence;
        private int acknowledge;
        private byte dataOffset;
        private TcpFlags flags;
        private short windowSize;
        private short checksum;
        private short urgentPointer;
        private byte[] options;

        private ByteBuf payloadBuffer;

        public Builder sourcePort(short sourcePort) {
            this.sourcePort = (short) (sourcePort & 0xffff);
            return this;
        }

        public Builder destinationPort(short destinationPort) {
            this.destinationPort = (short) (destinationPort & 0xffff);
            return this;
        }

        public Builder sequence(int sequence) {
            this.sequence = (int) (sequence & 0xffffffffL);
            return this;
        }

        public Builder acknowledge(int acknowledge) {
            this.acknowledge = (int) (acknowledge & 0xffffffffL);
            return this;
        }

        public Builder dataOffset(byte dataOffset) {
            this.dataOffset = (byte) (dataOffset & 0xf);
            return this;
        }

        public Builder flags(TcpFlags flags) {
            this.flags = flags;
            return this;
        }

        public Builder windowsSize(short windowSize) {
            this.windowSize = (byte) (windowSize & 0xffff);
            return this;
        }

        public Builder checksum(short checksum) {
            this.checksum = (byte) (checksum & 0xffff);
            return this;
        }

        public Builder urgentPointer(short urgentPointer) {
            this.urgentPointer = (byte) (urgentPointer & 0xffff);
            return this;
        }

        public Builder options(byte[] options) {
            this.options = Validate.nullPointer(options, new byte[0]);
            return this;
        }

        public Builder payloadBuffer(ByteBuf buffer) {
            this.payloadBuffer = buffer;
            return this;
        }

        @Override
        public Packet build() {
            return new Tcp(this);
        }

        @Override
        public Packet build(ByteBuf buffer) {
            this.sourcePort = buffer.getShort(0);
            this.destinationPort = buffer.getShort(2);
            this.sequence = buffer.getInt(4);
            this.acknowledge = buffer.getInt(8);
            short flags = buffer.getShort(buffer.getShort(12));
            this.dataOffset = (byte) (flags >> 12 & 0xf);
            this.flags = new TcpFlags.Builder().build((short) (flags & 0x1ff));
            this.windowSize = buffer.getShort(14);
            this.checksum = buffer.getShort(16);
            this.urgentPointer = buffer.getShort(18);
            if (this.dataOffset > 5) {
                int optionLength = (this.dataOffset << 2) - Header.TCP_HEADER_LENGTH;
                if (buffer.capacity() < Header.TCP_HEADER_LENGTH + optionLength) {
                    optionLength = buffer.capacity() - Header.TCP_HEADER_LENGTH;
                }
                this.options = new byte[optionLength];
                buffer.getBytes(20, options);
            }
            return new Tcp(this);
        }

    }

}
