/**
 * Copyright (C) 2017-2018  Ardika Rommy Sanjaya <contact@ardikars.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ardikars.jxpacket.core.ethernet;

import com.ardikars.common.net.MacAddress;
import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.common.layer.NetworkLayer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class Ethernet extends AbstractPacket {

	private final Ethernet.Header header;
	private final Packet payload;

	private Ethernet(final Builder builder) {
		this.header = new Ethernet.Header(builder);
		this.payload = NetworkLayer.valueOf(this.header.getPayloadType().getValue())
				.newInstance(builder.payloadBuffer);
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
	public static class Header implements Packet.Header {

		public static final int ETHERNET_HEADER_LENGTH = 14;

		private final MacAddress destinationMacAddress;
		private final MacAddress sourceMacAddress;
		private final NetworkLayer ethernetType;

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

		public NetworkLayer getEthernetType() {
			return ethernetType;
		}

		@Override
		public NetworkLayer getPayloadType() {
			return ethernetType;
		}

		@Override
		public int getLength() {
			return Header.ETHERNET_HEADER_LENGTH;
		}

		@Override
		public ByteBuf getBuffer() {
			ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(getLength());
			buffer.setBytes(0, destinationMacAddress.toBytes());
			buffer.setBytes(6, sourceMacAddress.toBytes());
			buffer.setShort(12, ethernetType.getValue());
			return buffer;
		}

		@Override
		public String toString() {
			return new StringBuilder()
					.append("\tdestinationMacAddress: ").append(destinationMacAddress).append('\n')
					.append("\tsourceMacAddress: ").append(sourceMacAddress).append('\n')
					.append("\tethernetType: ").append(ethernetType).append('\n')
					.toString();
		}

	}

	@Override
	public String toString() {
		return new StringBuilder("[ Ethernet Header (").append(getHeader().getLength()).append(" bytes) ]")
				.append('\n').append(header).append("\tpayload: ").append(payload != null ? payload.getClass().getSimpleName() : "")
				.toString();
	}

	public static class Builder implements Packet.Builder {

		private MacAddress destinationMacAddress;
		private MacAddress sourceMacAddress;
		private NetworkLayer ethernetType;

		private ByteBuf payloadBuffer;

		public Builder destinationMacAddress(final MacAddress destinationMacAddress) {
			this.destinationMacAddress = destinationMacAddress;
			return this;
		}

		public Builder sourceMacAddress(final MacAddress sourceMacAddress) {
			this.sourceMacAddress = sourceMacAddress;
			return this;
		}

		public Builder ethernetType(final NetworkLayer ethernetType) {
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
			byte[] hardwareAddressBuffer;
			hardwareAddressBuffer = new byte[MacAddress.MAC_ADDRESS_LENGTH];
			buffer.getBytes(0, hardwareAddressBuffer);
			builder.destinationMacAddress = MacAddress.valueOf(hardwareAddressBuffer);
			hardwareAddressBuffer = new byte[MacAddress.MAC_ADDRESS_LENGTH];
			buffer.getBytes(6, hardwareAddressBuffer);
			builder.sourceMacAddress = MacAddress.valueOf(hardwareAddressBuffer);
			builder.ethernetType = NetworkLayer.valueOf(buffer.getShort(12));
			builder.payloadBuffer = buffer.copy(Header.ETHERNET_HEADER_LENGTH, buffer.capacity() - Header.ETHERNET_HEADER_LENGTH);
			release(buffer);
			return new Ethernet(builder);
		}

	}

}
