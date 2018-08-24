package com.ardikars.jxpacket.ip.ipv6;

import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.AbstractPacket;
import com.ardikars.jxpacket.Packet;
import com.ardikars.jxpacket.ip.Ip;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

public class Fragment extends AbstractPacket {

	private final Fragment.Header header;
	private final Packet payload;

	private Fragment(final Builder builder) {
		this.header = new Header(builder);
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

	public static final class Header extends AbstractPacket.PacketHeader {

		public static final int FIXED_FRAGMENT_HEADER_LENGTH = 8;

		private Ip.Type nextHeader;
		private short fragmentOffset;
		private FlagType flagType;
		private int identification;

		private Header(final Builder builder) {
			this.nextHeader = builder.nextHeader;
			this.fragmentOffset = builder.fragmentOffset;
			this.flagType = builder.flagType;
			this.identification = builder.identification;
		}

		public Ip.Type getNextHeader() {
			return nextHeader;
		}

		public int getFragmentOffset() {
			return fragmentOffset & 0x1fff;
		}

		public FlagType getFlagType() {
			return flagType;
		}

		public int getIdentification() {
			return identification & 0xffffffff;
		}

		@Override
		public Ip.Type getPayloadType() {
			return nextHeader;
		}

		@Override
		public int getLength() {
			return FIXED_FRAGMENT_HEADER_LENGTH;
		}

		@Override
		public ByteBuf getBuffer() {
			ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(getLength());
			buffer.setByte(0, nextHeader.getValue());
			buffer.setByte(1, (byte) 0); // reserved
			buffer.setShort(2, (short) ((fragmentOffset & 0x1fff) << 3 |
							flagType.getValue() & 0x1));
			buffer.setInt(3, identification);
			return buffer;
		}

	}

	public static final class Builder extends AbstractPacket.PacketBuilder {

		private Ip.Type nextHeader;
		private short fragmentOffset;
		private FlagType flagType;
		private int identification;

		public Builder nextHeader(Ip.Type nextHeader) {
			this.nextHeader = nextHeader;
			return this;
		}

		public Builder fragmentOffset(int fragmentOffset) {
			this.fragmentOffset = (short) (fragmentOffset & 0x1fff);
			return this;
		}

		public Builder flagType(FlagType flagType) {
			this.flagType = flagType;
			return this;
		}

		public Builder identification(int identification) {
			this.identification = identification & 0xffffffff;
			return this;
		}

		@Override
		public Fragment build() {
			return new Fragment(this);
		}

		@Override
		public Fragment build(final ByteBuf buffer) {
			Builder builder = new Builder();
			builder.nextHeader = Ip.Type.valueOf(buffer.getByte(0));
			short sscratch = buffer.getShort(2);
			builder.fragmentOffset = (short) (sscratch >> 3 & 0x1fff);
			builder.flagType = FlagType.valueOf((byte) (sscratch & 0x1));
			builder.identification = buffer.getInt(4);
			buffer.release();
			return new Fragment(this);
		}

	}

	public static final class FlagType extends NamedNumber<Byte, FlagType> {

		public static final FlagType LAST_FRAGMENT = new FlagType((byte) 0, "Last fragment.");

		public static final FlagType MORE_FRAGMENT = new FlagType((byte) 1, "More fragment.");

		public static final FlagType UNKNOWN = new FlagType((byte) -1, "UNKNOWN.");

		protected FlagType(Byte value, String name) {
			super(value, name);
		}

		private static final Map<Byte, FlagType> registry
				= new HashMap<>();

		public static FlagType register(final FlagType flagType) {
			registry.put(flagType.getValue(), flagType);
			return flagType;
		}

		public static FlagType valueOf(final byte flag) {
			FlagType flagType = registry.get(flag);
			if (flagType == null) {
				return UNKNOWN;
			}
			return flagType;
		}

	}


}
