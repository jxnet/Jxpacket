package jxpacket.ip.ipv6;

import io.netty.buffer.ByteBuf;
import jxpacket.Packet;
import jxpacket.ip.Ip;

public class HopByHopOptions extends Options {

	private final HopByHopOptions.Header header;
	private final Packet payload;

	private HopByHopOptions(final Builder builder) {
		this.header = new Header(builder);
		this.payload = null;
	}

	@Override
	public Header getHeader() {
		return header;
	}

	@Override
	public Packet getPayload() {
		return payload;
	}

	public static final class Header extends Options.Header {

		protected Header(final HopByHopOptions.Builder builder) {
			super(builder, builder.nextHeader);
		}

	}

	public static final class Builder extends Options.Builder {

		public Builder() {
			super(Ip.Type.IPV6_HOPOPT);
		}

		@Override
		public HopByHopOptions build() {
			return new HopByHopOptions(this);
		}

		@Override
		public HopByHopOptions build(final ByteBuf buffer) {
			int index = 0;
			Builder builder = new Builder();
			index += 1;
			builder.extensionLength = buffer.getInt(index);
			index += 4;
			builder.options = new byte[Options.Header.FIXED_OPTIONS_LENGTH
					+ Options.Header.LENGTH_UNIT * builder.extensionLength];
			buffer.getBytes(index, options);
			buffer.release();
			return new HopByHopOptions(this);
		}

	}

}
