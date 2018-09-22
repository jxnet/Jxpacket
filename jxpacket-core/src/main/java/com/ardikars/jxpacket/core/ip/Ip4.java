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

package com.ardikars.jxpacket.core.ip;

import com.ardikars.common.net.Inet4Address;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.common.layer.TransportLayer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.util.Arrays;

public class Ip4 extends Ip {

	private final Ip4.Header header;
	private final Packet payload;

	private Ip4(final Builder builder) {
		this.header = new Ip4.Header(builder);
		this.payload = TransportLayer.valueOf(this.header.getPayloadType().getValue())
				.newInstance(builder.payloadBuffer);
	}

	@Override
	public Ip4.Header getHeader() {
		return this.header;
	}

	@Override
	public Packet getPayload() {
		return this.payload;
	}

	/**
	 * @see <a href="https://tools.ietf.org/html/rfc760">IPV4 HeaderAbstract</a>
	 */
	public static final class Header extends IpHeader {

		public static final int IPV4_HEADER_LENGTH = 20;

		private final byte headerLength;
		private final byte diffServ;
		private final byte expCon;
		private final short totalLength;
		private final short identification;
		private final byte flags;
		private final short fragmentOffset;
		private final byte ttl;
		private final TransportLayer protocol;
		private final short checksum;
		private final Inet4Address sourceAddress;
		private final Inet4Address destinationAddress;
		private final byte[] options;

		protected Header(final Builder builder) {
			super((byte) 0x04);
			this.headerLength = builder.headerLength;
			this.diffServ = builder.diffServ;
			this.expCon = builder.expCon;
			this.totalLength = builder.totalLength;
			this.identification = builder.identification;
			this.flags = builder.flags;
			this.fragmentOffset = builder.fragmentOffset;
			this.ttl = builder.ttl;
			this.protocol = builder.protocol;
			this.checksum = builder.checksum;
			this.sourceAddress = builder.sourceAddress;
			this.destinationAddress = builder.destinationAddress;
			this.options = builder.options;
		}

		public int getHeaderLength() {
			return headerLength & 0xf;
		}

		public int getDiffServ() {
			return diffServ & 0x3f;
		}

		public int getExpCon() {
			return expCon & 0x3;
		}

		public int getTotalLength() {
			return totalLength & 0xffff;
		}

		public int getIdentification() {
			return identification & 0xffff;
		}

		public int getFlags() {
			return flags & 0x7;
		}

		public int getFragmentOffset() {
			return fragmentOffset & 0x1fff;
		}

		public int getTtl() {
			return ttl & 0xff;
		}

		public TransportLayer getProtocol() {
			return protocol;
		}

		public int getChecksum() {
			return checksum & 0xffff;
		}

		public Inet4Address getSourceAddress() {
			return sourceAddress;
		}

		public Inet4Address getDestinationAddress() {
			return destinationAddress;
		}

		/**
		 * Get options.
		 * @return returns options.
		 */
		public byte[] getOptions() {
			byte[] options = new byte[this.options.length];
			System.arraycopy(this.options, 0, options, 0, options.length);
			return options;
		}

		@Override
		public TransportLayer getPayloadType() {
			return this.protocol;
		}

		@Override
		public int getLength() {
			return IPV4_HEADER_LENGTH + ((options == null) ? 0 : options.length);
		}

		@Override
		public ByteBuf getBuffer() {
			ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(getLength());
			int index = 0;
			buffer.setByte(index, (byte) ((super.version & 0xf) << 4 | headerLength & 0xf));
			index += 1;
			buffer.setByte(index, (byte) (((diffServ << 2) & 0x3f) | expCon & 0x3));
			index += 1;
			buffer.setShort(index, totalLength);
			index += 2;
			buffer.setShort(index, identification);
			index += 2;
			buffer.setShort(index, (flags & 0x7) << 13 | fragmentOffset & 0x1fff);
			index += 2;
			buffer.setByte(index, ttl);
			index += 1;
			buffer.setByte(index, protocol.getValue());
			index += 1;
			buffer.setShort(index, checksum & 0xffff);
			index += 2;
			buffer.setBytes(index, sourceAddress.toBytes());
			index += Inet4Address.IPV4_ADDRESS_LENGTH;
			buffer.setBytes(index, destinationAddress.toBytes());
			index += Inet4Address.IPV4_ADDRESS_LENGTH;
			if (options != null && headerLength > 5) {
				buffer.setBytes(index, options);
			}
			short checksum = this.checksum;
			if (checksum == 0) {
				int accumulation = 0;
				for (int i = 0; i < headerLength * 2; ++i) {
					accumulation += 0xffff & buffer.getShort(0);
				}
				accumulation = (accumulation >> 16 & 0xffff)
						+ (accumulation & 0xffff);
				checksum = (short) (~accumulation & 0xffff);
				buffer.setShort(10, checksum & 0xffff);
			}
			return buffer;
		}

		@Override
		public String toString() {
			return new StringBuilder("Ip4Header{")
					.append("version=").append(super.getVersion())
					.append(", headerLength=").append(getHeaderLength())
					.append(", diffServ=").append(getDiffServ())
					.append(", expCon=").append(getExpCon())
					.append(", totalLength=").append(getTotalLength())
					.append(", identification=").append(getIdentification())
					.append(", flags=").append(getFlags())
					.append(", fragmentOffset=").append(getFragmentOffset())
					.append(", ttl=").append(getTtl())
					.append(", protocol=").append(getProtocol())
					.append(", checksum=").append(getChecksum())
					.append(", sourceAddress=").append(getSourceAddress())
					.append(", destinationAddress=").append(getDestinationAddress())
					.append(", options=").append(Arrays.toString(getOptions()))
					.append('}').toString();
		}

	}

	public static final class Builder extends IpPaketBuilder {

		private byte headerLength;
		private byte diffServ;
		private byte expCon;
		private short totalLength;
		private short identification;
		private byte flags;
		private short fragmentOffset;
		private byte ttl;
		private TransportLayer protocol;
		private short checksum;
		private Inet4Address sourceAddress;
		private Inet4Address destinationAddress;
		private byte[] options;

		private ByteBuf payloadBuffer;

		public Builder headerLength(final int headerLength) {
			this.headerLength = (byte) (headerLength & 0xf);
			return this;
		}

		public Builder diffServ(final int diffServ) {
			this.diffServ = (byte) (this.diffServ & 0x3f);
			return this;
		}

		public Builder expCon(final int expCon) {
			this.expCon = (byte) (expCon & 0x3);
			return this;
		}

		public Builder totalLength(final int totalLength) {
			this.totalLength = (short) (totalLength & 0xffff);
			return this;
		}

		public Builder identification(final int identification) {
			this.identification = (short) (identification & 0xffff);
			return this;
		}

		public Builder flags(final int flags) {
			this.flags = (byte) (flags & 0x7);
			return this;
		}

		public Builder fragmentOffset(final int fragmentOffset) {
			this.fragmentOffset = (short) (fragmentOffset & 0x1fff);
			return this;
		}

		public Builder ttl(final int ttl) {
			this.ttl = (byte) (ttl & 0xff);
			return this;
		}

		public Builder protocol(TransportLayer protocol) {
			this.protocol = protocol;
			return this;
		}

		public Builder checksum(final int checksum) {
			this.checksum = (short) (this.checksum & 0xffff);
			return this;
		}

		public Builder sourceAddress(final Inet4Address sourceAddress) {
			this.sourceAddress = sourceAddress;
			return this;
		}

		public Builder destinationAddress(final Inet4Address destinationAddress) {
			this.destinationAddress = destinationAddress;
			return this;
		}

		/**
		 * Add options.
		 * @param options options.
		 * @return returns this {@link Builder} object.
		 */
		public Builder options(final byte[] options) {
			this.options = new byte[options.length];
			System.arraycopy(options, 0, this.options, 0, this.options.length);
			return this;
		}

		public ByteBuf getPayloadBuffer() {
			return this.payloadBuffer;
		}

		@Override
		public Packet build() {
			return new Ip4(this);
		}

		@Override
		public Packet build(final ByteBuf buffer) {
			Builder builder = new Builder();
			builder.headerLength = (byte) (buffer.getByte(0) & 0xf);
			byte tmp = buffer.getByte(1);
			builder.diffServ = (byte) ((tmp >> 2) & 0x3f);
			builder.expCon = (byte) (tmp & 0x3);
			builder.totalLength = buffer.getShort(2);
			builder.identification = buffer.getShort(4);
			short sscratch = buffer.getShort(6);
			builder.flags = (byte) (sscratch >> 13 & 0x7);
			builder.fragmentOffset = (short) (sscratch & 0x1fff);
			builder.ttl = buffer.getByte(8);
			builder.protocol = TransportLayer.valueOf(buffer.getByte(9));
			builder.checksum = (short) (buffer.getShort(10) & 0xffff);
			byte[] ipv4Buffer;
			ipv4Buffer = new byte[Inet4Address.IPV4_ADDRESS_LENGTH];
			buffer.getBytes(12, ipv4Buffer);
			builder.sourceAddress = Inet4Address.valueOf(ipv4Buffer);
			ipv4Buffer = new byte[Inet4Address.IPV4_ADDRESS_LENGTH];
			buffer.getBytes(16, ipv4Buffer);
			builder.destinationAddress = Inet4Address.valueOf(ipv4Buffer);
			int size = 20;
			if (builder.headerLength > 5) {
				int optionsLength = (builder.headerLength - 5) * 4;
				builder.options = new byte[optionsLength];
				buffer.getBytes(20, builder.options);
				size += optionsLength;
			}
			builder.payloadBuffer = buffer.copy(size, buffer.capacity() - size);
			buffer.release();
			return new Ip4(builder);
		}

	}

}
