package jxpacket.ethernet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import jxpacket.AbstractPacket;
import jxpacket.Packet;
import jxpacket.ProtocolType;

public class Vlan extends AbstractPacket {

	private final Vlan.Header header;
	private final Packet payload;

	private Vlan(final Builder builder) {
		this.header = new Vlan.Header(builder);
		this.payload = super.getPayloadBuilder(this.header)
				.build(builder.payloadBuffer);
	}

	public static Vlan newPacket(final ByteBuf buffer) {
		return new Vlan.Builder().build(buffer);
	}

	@Override
	public Vlan.Header getHeader() {
		return this.header;
	}

	@Override
	public Packet getPayload() {
		return this.payload;
	}

	public static final class Header extends PacketHeader {

		public static final int VLAN_HEADER_LENGTH = 4;

		private byte priorityCodePoint; // 3 bit
		private byte canonicalFormatIndicator; // 1 bit
		private short vlanIdentifier; // 12 bit
		private ProtocolType type;

		private Header(final Builder builder) {
			this.priorityCodePoint = builder.priorityCodePoint;
			this.canonicalFormatIndicator = builder.canonicalFormatIndicator;
			this.vlanIdentifier = builder.vlanIdentifier;
			this.type = builder.type;
		}

		public int getPriorityCodePoint() {
			return priorityCodePoint & 0x07;
		}

		public int getCanonicalFormatIndicator() {
			return canonicalFormatIndicator & 0x01;
		}

		public int getVlanIdentifier() {
			return vlanIdentifier & 0x0fff;
		}

		public ProtocolType getType() {
			return type;
		}

		@Override
		public ProtocolType getPayloadType() {
			return this.type;
		}

		@Override
		public int getLength() {
			return Vlan.Header.VLAN_HEADER_LENGTH;
		}

		@Override
		public ByteBuf getBuffer() {
			ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(getLength());
			int index = 0;
			buffer.setShort(index, ProtocolType.DOT1Q_VLAN_TAGGED_FRAMES.getValue());
			index += 2;
			buffer.setShort(index, (((priorityCodePoint << 13) & 0x07)
					| ((canonicalFormatIndicator << 14) & 0x01) | (vlanIdentifier & 0x0fff)));
			return buffer;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder("Header{");
			sb.append("priorityCodePoint=").append(getPriorityCodePoint());
			sb.append(", canonicalFormatIndicator=").append(getCanonicalFormatIndicator());
			sb.append(", vlanIdentifier=").append(getVlanIdentifier());
			sb.append(", type=").append(getType());
			sb.append('}');
			return sb.toString();
		}

	}

	public static final class Builder extends PacketBuilder {

		private byte priorityCodePoint; // 3 bit
		private byte canonicalFormatIndicator; // 1 bit
		private short vlanIdentifier; // 12 bit
		private ProtocolType type;

		private ByteBuf payloadBuffer;

		public Builder() {

		}

		public Builder priorityCodePoint(final int priorityCodePoint) {
			this.priorityCodePoint = (byte) (priorityCodePoint & 0x07);
			return this;
		}

		public Builder canonicalFormatIndicator(final int canonicalFormatIndicator) {
			this.canonicalFormatIndicator = (byte) (canonicalFormatIndicator & 0x01);
			return this;
		}

		public Builder vlanIdentifier(final int vlanIdentifier) {
			this.vlanIdentifier = (short) (vlanIdentifier & 0x0fff);
			return this;
		}

		public Builder type(final ProtocolType type) {
			this.type = type;
			return this;
		}

		public Builder payloadBuffer(final ByteBuf buffer) {
			this.payloadBuffer = buffer;
			return this;
		}

		@Override
		public Vlan build() {
			return new Vlan(this);
		}

		@Override
		public Vlan build(final ByteBuf buffer) {
			int index = 0;
			short tci = buffer.getShort(index);
			index += 2;
			short type = buffer.getShort(index);
			Vlan.Builder builder = new Builder();
			builder.priorityCodePoint = (byte) (tci >> 13 & 0x07);
			builder.canonicalFormatIndicator = (byte) (tci >> 14 & 0x01);
			builder.vlanIdentifier = (short) (tci & 0x0fff);
			builder.type = ProtocolType.valueOf(type);
			int size = index + 2;
			builder.payloadBuffer = buffer.copy(size, buffer.capacity() - size);
			return new Vlan(builder);
		}

	}

}
