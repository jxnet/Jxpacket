package com.ardikars.jxpacket.ip.ipv6;

import com.ardikars.jxpacket.AbstractPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import com.ardikars.jxpacket.ip.Ip;
import com.ardikars.jxpacket.ip.Ipv6;

import java.util.Arrays;

public abstract class Options extends AbstractPacket {

	abstract static class Header extends Ipv6.ExtensionHeader {

		public static int FIXED_OPTIONS_LENGTH = 6;
		public static int LENGTH_UNIT = 8;

		protected Ip.Type nextHeader;
		protected int extensionLength;
		protected byte[] options;

		protected Header(final Builder builder, Ip.Type nextHeader) {
			this.nextHeader = nextHeader;
			this.extensionLength = builder.extensionLength;
			this.options = builder.options;
		}

		public Ip.Type getNextHeader() {
			return nextHeader;
		}

		public int getExtensionLength() {
			return extensionLength;
		}

		public byte[] getOptions() {
			return options;
		}

		@Override
		public Ip.Type getPayloadType() {
			return nextHeader;
		}

		@Override
		public int getLength() {
			return FIXED_OPTIONS_LENGTH + LENGTH_UNIT * extensionLength;
		}

		@Override
		public ByteBuf getBuffer() {
			ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(getLength());
			buffer.setByte(0, nextHeader.getValue());
			buffer.setInt(1, extensionLength);
			if (options != null) {
				buffer.setBytes(5, options);
			}
			return buffer;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder("Header{");
			sb.append("nextHeader=").append(getNextHeader());
			sb.append(", extensionLength=").append(getExtensionLength());
			sb.append(", options=").append(Arrays.toString(getOptions()));
			sb.append('}');
			return sb.toString();
		}

	}

	abstract static class Builder extends AbstractPacket.PacketBuilder {

		protected Ip.Type nextHeader;
		protected int extensionLength;
		protected byte[] options;

		public Builder(final Ip.Type nextHeader) {
			this.nextHeader = nextHeader;
		}

		public Builder extensionLength(final int extensionLength) {
			this.extensionLength = extensionLength & 0xffffffff;
			return this;
		}

		public Builder options(final byte[] options) {
			this.options = options;
			return this;
		}

	}

}
