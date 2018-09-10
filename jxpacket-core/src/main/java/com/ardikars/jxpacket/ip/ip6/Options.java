package com.ardikars.jxpacket.ip.ip6;

import com.ardikars.jxpacket.AbstractPacket;
import com.ardikars.jxpacket.Packet;
import com.ardikars.jxpacket.ip.Ip6;
import com.ardikars.jxpacket.layer.TransportLayer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.util.Arrays;

public abstract class Options extends AbstractPacket {

	abstract static class Header extends Ip6.ExtensionHeader {

		public static int FIXED_OPTIONS_LENGTH = 6;
		public static int LENGTH_UNIT = 8;

		protected TransportLayer nextHeader;
		protected int extensionLength;
		protected byte[] options;

		protected Header(final Builder builder, TransportLayer nextHeader) {
			this.nextHeader = nextHeader;
			this.extensionLength = builder.extensionLength;
			this.options = builder.options;
		}

		public TransportLayer getNextHeader() {
			return nextHeader;
		}

		public int getExtensionLength() {
			return extensionLength & 0xffffffff;
		}

		public byte[] getOptions() {
			return options;
		}

		@Override
		public TransportLayer getPayloadType() {
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
			final StringBuilder sb = new StringBuilder("HeaderAbstract{");
			sb.append("nextHeader=").append(getNextHeader());
			sb.append(", extensionLength=").append(getExtensionLength());
			sb.append(", options=").append(Arrays.toString(getOptions()));
			sb.append('}');
			return sb.toString();
		}

	}

	abstract static class Builder implements Packet.Builder {

		protected TransportLayer nextHeader;
		protected int extensionLength;
		protected byte[] options;

		public Builder(final TransportLayer nextHeader) {
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
