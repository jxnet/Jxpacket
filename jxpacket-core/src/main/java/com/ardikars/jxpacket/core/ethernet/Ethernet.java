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

public class Ethernet extends AbstractPacket {

	private final Ethernet.Header header;
	private final Packet payload;

	private Ethernet(final Builder builder) {
		this.header = new Ethernet.Header(builder);
		this.payload = NetworkLayer.valueOf(this.header.getPayloadType().getValue())
				.newInstance(builder.payloadBuffer);
		payloadBuffer = builder.payloadBuffer;
	}

	public static final Ethernet newPacket(final ByteBuf buffer) {
		return new Ethernet.Builder().build(buffer);
	}

	@Override
	public Ethernet.Header getHeader() {
		return header;
	}

	@Override
	public Packet getPayload() {
		return payload;
	}

	/**
	 * @see <a href="https://en.wikipedia.org/wiki/Ethernet_frame">Ethernet II Structure</a>
	 */
	public static class Header extends AbstractPacket.Header {

		public static final int ETHERNET_HEADER_LENGTH = 14;

		private final MacAddress destinationMacAddress;
		private final MacAddress sourceMacAddress;
		private final NetworkLayer ethernetType;

		private Header(final Builder builder) {
			this.destinationMacAddress = builder.destinationMacAddress;
			this.sourceMacAddress = builder.sourceMacAddress;
			this.ethernetType = builder.ethernetType;
			this.buffer = builder.buffer.slice(0, getLength());
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
			if (buffer == null) {
				buffer.writeBytes(destinationMacAddress.toBytes());
				buffer.writeBytes(sourceMacAddress.toBytes());
				buffer.writeShort(ethernetType.getValue());
			}
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

	public static class Builder extends AbstractPacket.Builder {

		private MacAddress destinationMacAddress;
		private MacAddress sourceMacAddress;
		private NetworkLayer ethernetType;

		private ByteBuf buffer;
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
			byte[] hardwareAddressBuffer;
			hardwareAddressBuffer = new byte[MacAddress.MAC_ADDRESS_LENGTH];
			buffer.readBytes(hardwareAddressBuffer);
			this.destinationMacAddress = MacAddress.valueOf(hardwareAddressBuffer);
			hardwareAddressBuffer = new byte[MacAddress.MAC_ADDRESS_LENGTH];
			buffer.readBytes(hardwareAddressBuffer);
			this.sourceMacAddress = MacAddress.valueOf(hardwareAddressBuffer);
			this.ethernetType = NetworkLayer.valueOf(buffer.readShort());
			this.buffer = buffer;
			this.payloadBuffer = buffer.slice();
			return new Ethernet(this);
		}

	}

}
