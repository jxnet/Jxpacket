package com.ardikars.jxpacket.ethernet;

import com.ardikars.common.net.MacAddress;
import com.ardikars.jxpacket.AbstractPacket;
import com.ardikars.jxpacket.Packet;
import com.ardikars.jxpacket.ProtocolType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class Ethernet extends AbstractPacket {

	private final Ethernet.Header header;
	private final Packet payload;

	private Ethernet(final Builder builder) {
		this.header = new Ethernet.Header(builder);
		this.payload = super.getPayloadBuilder(this.header)
				.build(builder.payloadBuffer);
	}

	public static final Ethernet newPacket(final ByteBuf buffer) {
		return new Ethernet.Builder().build(buffer);
	}

	@Override
	public Ethernet.Header getHeader() {
		return this.header;
	}

	@Override
	public Packet getPayload() {
		return this.payload;
	}

	/**
	 * @see <a href="https://en.wikipedia.org/wiki/Ethernet_frame">Ethernet II Structure</a>
	 */
	public static class Header extends PacketHeader {

		public static final int ETHERNET_HEADER_LENGTH = 14;

		private MacAddress destinationMacAddress;
		private MacAddress sourceMacAddress;
		private ProtocolType ethernetType;

		private Header(final Builder builder) {
			this.destinationMacAddress = builder.destinationMacAddress;
			this.sourceMacAddress = builder.sourceMacAddress;
			this.ethernetType = builder.ethernetType;
		}

		public MacAddress getDestinationMacAddress() {
			return destinationMacAddress;
		}

		public MacAddress getSourceMacAddress() {
			return sourceMacAddress;
		}

		public ProtocolType getEthernetType() {
			return ethernetType;
		}


		@Override
		public ProtocolType getPayloadType() {
			return ethernetType;
		}

		@Override
		public int getLength() {
			return Header.ETHERNET_HEADER_LENGTH;
		}

		@Override
		public ByteBuf getBuffer() {
			ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(getLength());
			int index = 0;
			buffer.setBytes(index, destinationMacAddress.toBytes());
			index += MacAddress.MAC_ADDRESS_LENGTH;
			buffer.setBytes(index, sourceMacAddress.toBytes());
			index += MacAddress.MAC_ADDRESS_LENGTH;
			buffer.setShort(index, ethernetType.getValue());
			return buffer;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder("Header{");
			sb.append("destinationMacAddress=").append(getDestinationMacAddress());
			sb.append(", sourceMacAddress=").append(getSourceMacAddress());
			sb.append(", ethernetType=").append(getEthernetType());
			sb.append('}');
			return sb.toString();
		}

	}

	public static class Builder extends PacketBuilder {

		private MacAddress destinationMacAddress;
		private MacAddress sourceMacAddress;
		private ProtocolType ethernetType;

		private ByteBuf payloadBuffer;

		public Builder() { }

		public Builder destinationMacAddress(final MacAddress destinationMacAddress) {
			this.destinationMacAddress = destinationMacAddress;
			return this;
		}

		public Builder sourceMacAddress(final MacAddress sourceMacAddress) {
			this.sourceMacAddress = sourceMacAddress;
			return this;
		}

		public Builder ethernetType(final ProtocolType ethernetType) {
			this.ethernetType = ethernetType;
			return this;
		}

		public Builder payloadBuffer(final ByteBuf buffer) {
			this.payloadBuffer = buffer;
			return this;
		}

		@Override
		public Ethernet build() {
			return new Ethernet(this);
		}

		@Override
		public Ethernet build(final ByteBuf buffer) {
			Ethernet.Builder builder = new Ethernet.Builder();

			int index = 0;
			byte[] hardwareAddressBuffer;
			hardwareAddressBuffer = new byte[MacAddress.MAC_ADDRESS_LENGTH];
			buffer.getBytes(index, hardwareAddressBuffer);
			builder.destinationMacAddress = MacAddress.valueOf(hardwareAddressBuffer);
			index += MacAddress.MAC_ADDRESS_LENGTH;

			hardwareAddressBuffer = new byte[MacAddress.MAC_ADDRESS_LENGTH];
			buffer.getBytes(index, hardwareAddressBuffer);
			builder.sourceMacAddress = MacAddress.valueOf(hardwareAddressBuffer);
			index += MacAddress.MAC_ADDRESS_LENGTH;

			builder.ethernetType = ProtocolType.valueOf(buffer.getShort(index));

			int size = index + 2;
			builder.payloadBuffer = buffer.copy(size, buffer.capacity() - size);
			buffer.release();
			return new Ethernet(builder);
		}

	}

}
