package com.ardikars.jxpacket.core.ip.ip6;

import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.common.layer.TransportLayer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.util.Arrays;

public class Authentication extends AbstractPacket {

	private final Authentication.Header header;
	private final Packet payload;

	private Authentication(final Builder builder) {
		this.header = new Authentication.Header(builder);
		this.payload = null;
	}

	@Override
	public Packet.Header getHeader() {
		return header;
	}

	@Override
	public Packet getPayload() {
		return payload;
	}

	/**
	 * @see <a href="https://tools.ietf.org/html/rfc4302">Authentication HeaderAbstract</a>
	 */
	public static final class Header implements Packet.Header {

		public static final byte FIXED_HEADER_LENGTH = 12; // bytes

		private TransportLayer nextHeader;
		private byte payloadLength;
		private int securityParameterIndex;
		private int sequenceNumber;
		private byte[] integrityCheckValue;

		public Header(final Builder builder) {
			this.nextHeader = builder.nextHeader;
			this.payloadLength = builder.payloadLength;
			this.securityParameterIndex = builder.securityParameterIndex;
			this.sequenceNumber = builder.sequenceNumber;
			this.integrityCheckValue = builder.integrityCheckValue;
		}

		public TransportLayer getNextHeader() {
			return nextHeader;
		}

		public int getPayloadLength() {
			return payloadLength & 0xff;
		}

		public int getSecurityParameterIndex() {
			return securityParameterIndex & 0xffffffff;
		}

		public int getSequenceNumber() {
			return sequenceNumber & 0xffffffff;
		}

		public byte[] getIntegrityCheckValue() {
			return integrityCheckValue;
		}

		@Override
		public TransportLayer getPayloadType() {
			return nextHeader;
		}

		@Override
		public int getLength() {
			return FIXED_HEADER_LENGTH + ((integrityCheckValue == null) ? 0 : integrityCheckValue.length);
		}

		@Override
		public ByteBuf getBuffer() {
			ByteBuf buffer = PooledByteBufAllocator.DEFAULT
					.directBuffer(getLength());
			buffer.setByte(0, nextHeader.getValue());
			buffer.setByte(1, payloadLength);
			buffer.setShort(2, (short) 0); // reserved
			buffer.setInt(4, sequenceNumber);
			buffer.setInt(8, securityParameterIndex);
			if (integrityCheckValue != null) {
				buffer.setBytes(12, integrityCheckValue);
			}
			return buffer;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder("HeaderAbstract{");
			sb.append("nextHeader=").append(getNextHeader());
			sb.append(", payloadLength=").append(getPayloadLength());
			sb.append(", securityParameterIndex=").append(getSecurityParameterIndex());
			sb.append(", sequenceNumber=").append(getSequenceNumber());
			sb.append(", integrityCheckValue=").append(Arrays.toString(getIntegrityCheckValue()));
			sb.append('}');
			return sb.toString();
		}
	}

	public static final class Builder implements Packet.Builder {

		private TransportLayer nextHeader;
		private byte payloadLength;
		private int securityParameterIndex;
		private int sequenceNumber;
		private byte[] integrityCheckValue;

		public Builder nextHeader(final TransportLayer nextHeader) {
			this.nextHeader = nextHeader;
			return this;
		}

		public Builder payloadLength(final int payloadLength) {
			this.payloadLength = (byte) (payloadLength & 0xff);
			return this;
		}

		public Builder securityParameterIndex(final int securityParameterIndex) {
			this.securityParameterIndex = securityParameterIndex & 0xffffffff;
			return this;
		}

		public Builder sequenceNumber(final int sequenceNumber) {
			this.sequenceNumber = sequenceNumber & 0xffffffff;
			return this;
		}

		public Builder integrityCheckValue(final byte[] integrityCheckValue) {
			this.integrityCheckValue = integrityCheckValue;
			return this;
		}

		@Override
		public Packet build() {
			return new Authentication(this);
		}

		@Override
		public Packet build(final ByteBuf buffer) {
			Builder builder = new Builder();
			builder.nextHeader = TransportLayer.valueOf(buffer.getByte(0));
			builder.payloadLength = buffer.getByte(1);
			builder.securityParameterIndex = buffer.getInt(4);
			builder.sequenceNumber = buffer.getInt(8);
			builder.integrityCheckValue = new byte[((builder.payloadLength + 2) * 8) - 12];
			if (builder.integrityCheckValue != null) {
				buffer.getBytes(12, builder.integrityCheckValue);
			}
			buffer.release();
			return new Authentication(builder);
		}

	}

}
