package com.ardikars.jxpacket.iso8583;

import com.ardikars.common.annotation.Incubating;
import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.iso8583.dataelement.DataElement;
import com.ardikars.common.memory.Memory;

import java.util.List;

@Incubating
public class Iso8583 extends AbstractPacket {

    private final Header header;
    private final Packet payload;

    public Iso8583(Builder builder) {
        this.header = new Header(builder);
        if (builder.payloadBuffer != null && builder.payloadBuffer.capacity() > 0) {
            this.payload = Iso8583.builder()
                    .noMit(true)
                    .separator(builder.separator)
                    .build();
        } else {
            this.payload = null;
        }
    }

    @Override
    public Header getHeader() {
        return header;
    }

    @Override
    public Packet getPayload() {
        return payload;
    }

    public static class Header implements Packet.Header {

        private final MessageVersion messageVersion;
        private final MessageClass messageClass;
        private final MessageFunction messageFunction;
        private final MessageOrigin messageOrigin;
        private final byte[] messageBitmap;
        private final List<DataElement<?>> dataElements;

        private Header(Builder builder) {
            this.messageVersion = builder.messageVersion;
            this.messageClass = builder.messageClass;
            this.messageFunction = builder.messageFunction;
            this.messageOrigin = builder.messageOrigin;
            this.messageBitmap = builder.messageBitmap;
            this.dataElements = null;
        }

        @Override
        public <T extends NamedNumber> T getPayloadType() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getLength() {
            return messageBitmap.length + 4;
        }

        @Override
        public Memory getBuffer() {
            return null;
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements Packet.Builder {

        private MessageVersion messageVersion;
        private MessageClass messageClass;
        private MessageFunction messageFunction;
        private MessageOrigin messageOrigin;
        private byte[] messageBitmap;

        private String separator;
        private boolean noMit;
        private Memory payloadBuffer;

        public Builder messageVersion(MessageVersion messageVersion) {
            this.messageVersion = messageVersion;
            return this;
        }

        public Builder messageClass(MessageClass messageClass) {
            this.messageClass = messageClass;
            return this;
        }

        public Builder messageFunction(MessageFunction messageFunction) {
            this.messageFunction = messageFunction;
            return this;
        }

        public Builder messageOrigin(MessageOrigin messageOrigin) {
            this.messageOrigin = messageOrigin;
            return this;
        }

        public Builder messageBitmap(byte[] messageBitmap) {
            this.messageBitmap = messageBitmap;
            return this;
        }

        public Builder separator(String separator) {
            this.separator = separator;
            return this;
        }

        public Builder noMit(boolean noMit) {
            this.noMit = noMit;
            return this;
        }

        public Builder payloadBuffer(Memory payloadBuffer) {
            this.payloadBuffer = payloadBuffer;
            return this;
        }

        @Override
        public Packet build() {
            if (messageVersion == null) {
                throw new IllegalArgumentException("Message version should be not null.");
            }
            if (messageClass == null) {
                throw new IllegalArgumentException("Message class should be not null.");
            }
            if (messageFunction == null) {
                throw new IllegalArgumentException("Message function should be not null.");
            }
            if (messageOrigin == null) {
                throw new IllegalArgumentException("Message origin should be not null.");
            }
            if (messageBitmap == null) {
                throw new IllegalArgumentException("Message bitmap should be not null.");
            }
            return new Iso8583(this);
        }

        @Override
        public Packet build(Memory buffer) {
            int headerIndex = 0;
            if (!noMit) {
                this.messageVersion = MessageVersion.valueOf(buffer.getByte(0));
                this.messageClass = MessageClass.valueOf(buffer.getByte(1));
                this.messageFunction = MessageFunction.valueOf(buffer.getByte(2));
                this.messageOrigin = MessageOrigin.valueOf(buffer.getByte(3));
                headerIndex = 4;
            }
            int dataLength;
            if (separator == null || separator.isEmpty()) {
                dataLength = buffer.getShort(headerIndex);
                this.messageBitmap = new byte[dataLength];
                buffer.getBytes(6, this.messageBitmap);
                this.payloadBuffer = buffer.copy(dataLength + headerIndex, buffer.capacity() - dataLength + headerIndex);
            } else {
                dataLength = buffer.capacity() - headerIndex;
                byte[] data = new byte[dataLength];
                buffer.getBytes(6, data);
                String string = new String(data);
                int index = string.indexOf(separator);
                this.messageBitmap = new byte[index + separator.length() - 1];
                System.arraycopy(data, 0, this.messageBitmap, 0, this.messageBitmap.length);
                this.payloadBuffer = buffer.copy(dataLength + headerIndex, buffer.capacity() - dataLength + headerIndex);
            }
            return new Iso8583(this);
        }

    }

}
