package jxpacket.ip;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import jxpacket.Packet;
import jxpacket.common.net.Inet6Address;

public class Ipv6 extends Ip {

	private final Ipv6.Header header;
	private final Packet payload;

	private Ipv6(final Builder builder) {
		this.header = new Ipv6.Header(builder);
		this.payload =  getBuilder(this.header)
				.build(builder.payloadBuffer);
	}

	@Override
	public Ipv6.Header getHeader() {
		return header;
	}

	@Override
	public Packet getPayload() {
		return payload;
	}

	/**
	 * @see <a href="https://tools.ietf.org/html/rfc8200">IPv6 Header</a>
	 */
	public static final class Header extends AbstractIpHeader {

		public static final int IPV6_HEADER_LENGTH = 40;

		private byte trafficClass;
		private int flowLabel;
		private short payloadLength;
		private Type nextHeader;
		private byte hopLimit;
		private Inet6Address sourceAddress;
		private Inet6Address destinationAddress;

		protected Header(final Builder builder) {
			super((byte) 0x06);
			this.trafficClass = builder.trafficClass;
			this.flowLabel = builder.flowLabel;
			this.payloadLength = builder.payloadLength;
			this.nextHeader = builder.nextHeader;
			this.hopLimit = builder.hopLimit;
			this.sourceAddress = builder.sourceAddress;
			this.destinationAddress = builder.destinationAddress;
		}

		public int getTrafficClass() {
			return trafficClass & 0xff;
		}

		public int getFlowLabel() {
			return flowLabel & 0xfffff;
		}

		public int getPayloadLength() {
			return payloadLength & 0xffff;
		}

		public Type getNextHeader() {
			return nextHeader;
		}

		public int getHopLimit() {
			return hopLimit & 0xff;
		}

		public Inet6Address getSourceAddress() {
			return sourceAddress;
		}

		public Inet6Address getDestinationAddress() {
			return destinationAddress;
		}

		@Override
		public Type getPayloadType() {
			return nextHeader;
		}

		@Override
		public int getLength() {
			return IPV6_HEADER_LENGTH;
		}

		@Override
		public ByteBuf getBuffer() {
			ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(getLength());
			int index = 0;
			buffer.setInt(index, (super.version & 0xf) << 28 | (trafficClass & 0xff) << 20 | flowLabel & 0xfffff);
			index += 4;
			buffer.setShort(index, payloadLength);
			index += 2;
			buffer.setByte(index, nextHeader.getValue());
			index += 1;
			buffer.setByte(index, hopLimit);
			index += 1;
			buffer.setBytes(index, sourceAddress.toBytes());
			index += Inet6Address.IPV6_ADDRESS_LENGTH;
			buffer.setBytes(index, destinationAddress.toBytes());
			return buffer;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder("Header{");
			sb.append("version=").append(super.getVersion());
			sb.append(", trafficClass=").append(getTrafficClass());
			sb.append(", flowLabel=").append(getFlowLabel());
			sb.append(", payloadLength=").append(getPayloadLength());
			sb.append(", nextHeader=").append(getNextHeader());
			sb.append(", hopLimit=").append(getHopLimit());
			sb.append(", sourceAddress=").append(getSourceAddress());
			sb.append(", destinationAddress=").append(getDestinationAddress());
			sb.append('}');
			return sb.toString();
		}

	}

	public static final class Builder extends PacketBuilder {

		private byte trafficClass;
		private int flowLabel;
		private short payloadLength;
		private Type nextHeader;
		private byte hopLimit;
		private Inet6Address sourceAddress;
		private Inet6Address destinationAddress;

		private ByteBuf payloadBuffer;

		public Builder trafficClass(final int trafficClass) {
			this.trafficClass = (byte) (trafficClass & 0xff);
			return this;
		}

		public Builder flowLabel(final int flowLabel) {
			this.flowLabel = (flowLabel & 0xfffff);
			return this;
		}

		public Builder payloadLength(final int payloadLength) {
			this.payloadLength = (short) (payloadLength & 0xffff);
			return this;
		}

		public Builder nextHeader(final Type nextHeader) {
			this.nextHeader = nextHeader;
			return this;
		}

		public Builder hopLimit(final int hopLimit) {
			this.hopLimit = (byte) (hopLimit & 0xff);
			return this;
		}

		public Builder sourceAddress(final Inet6Address sourceAddress) {
			this.sourceAddress = sourceAddress;
			return this;
		}

		public Builder destinationAddress(final Inet6Address destinationAddress) {
			this.destinationAddress = destinationAddress;
			return this;
		}

		public Builder payloadBuffer(final ByteBuf buffer) {
			this.payloadBuffer = buffer;
			return this;
		}

		@Override
		public Packet build() {
			return new Ipv6(this);
		}

		@Override
		public Packet build(final ByteBuf buffer) {
			int index = 0;
			int iscratch = buffer.getInt(index);
			index += 4;
			Builder builder = new Builder();
			builder.trafficClass = (byte) (iscratch >> 20 & 0xff);
			builder.flowLabel = (iscratch & 0xfffff);
			builder.payloadLength = buffer.getShort(index);
			index += 2;
			builder.nextHeader = Type.valueOf(buffer.getByte(index));
			index += 1;
			builder.hopLimit = buffer.getByte(index);
			index += 1;

			byte[] addrBuf = new byte[Inet6Address.IPV6_ADDRESS_LENGTH];
			buffer.getBytes(index, addrBuf);
			builder.sourceAddress = Inet6Address.valueOf(addrBuf);
			index += Inet6Address.IPV6_ADDRESS_LENGTH;

			addrBuf = new byte[Inet6Address.IPV6_ADDRESS_LENGTH];
			buffer.getBytes(index, addrBuf);
			builder.destinationAddress = Inet6Address.valueOf(addrBuf);
			index += Inet6Address.IPV6_ADDRESS_LENGTH;
			int size = index;
			builder.payloadBuffer = buffer.copy(size, buffer.capacity() - size);
			return new Ipv6(builder);
		}

	}

	public abstract static class ExtensionHeader extends PacketHeader {

	}

}
