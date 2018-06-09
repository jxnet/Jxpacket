package jxpacket.ip.ipv6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import jxpacket.AbstractPacket;
import jxpacket.Packet;
import jxpacket.ip.Ip;

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

	public static final class Header extends PacketHeader {

		public static final byte FIXED_HEADER_LENGTH = 12; // bytes

		private Ip.IpType nextHeader;
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

		public Ip.IpType getNextHeader() {
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
		public Ip.IpType getPayloadType() {
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
			int index = 0;
			buffer.setByte(index, nextHeader.getValue());
			index += 1;
			buffer.setByte(index, payloadLength);
			index += 1;
			buffer.setShort(index, (short) 0); // reserved
			index += 2;
			buffer.setInt(index, sequenceNumber);
			index += 4;
			buffer.setInt(index, securityParameterIndex);
			if (integrityCheckValue != null) {
				index += 4;
				buffer.setBytes(index, integrityCheckValue);
			}
			return buffer;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder("Header{");
			sb.append("nextHeader=").append(getNextHeader());
			sb.append(", payloadLength=").append(getPayloadLength());
			sb.append(", securityParameterIndex=").append(getSecurityParameterIndex());
			sb.append(", sequenceNumber=").append(getSequenceNumber());
			sb.append(", integrityCheckValue=").append(Arrays.toString(getIntegrityCheckValue()));
			sb.append('}');
			return sb.toString();
		}
	}

	public static final class Builder extends PacketBuilder {

		private Ip.IpType nextHeader;
		private byte payloadLength;
		private int securityParameterIndex;
		private int sequenceNumber;
		private byte[] integrityCheckValue;

		public Builder nextHeader(final Ip.IpType nextHeader) {
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
			int index = 0;
			builder.nextHeader = Ip.IpType.valueOf(buffer.getByte(index));
			index += 1;
			builder.payloadLength = buffer.getByte(index);
			index += 1 ;
			index += 2; //reserved
			builder.securityParameterIndex = buffer.getInt(index);
			index += 4;
			builder.sequenceNumber = buffer.getInt(index);
			index += 4;
			builder.integrityCheckValue = new byte[((builder.payloadLength + 2) * 8) - 12];
			buffer.getBytes(index, builder.integrityCheckValue);
			return new Authentication(builder);
		}

	}

}
