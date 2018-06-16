package jxpacket.ip.ipv6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import jxpacket.AbstractPacket;
import jxpacket.Packet;
import jxpacket.common.NamedNumber;
import jxpacket.ip.Ip;

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
		return null;
	}

	@Override
	public Packet getPayload() {
		return null;
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
			int index = 0;
			buffer.setByte(index, nextHeader.getValue());
			index += 1;
			buffer.setByte(index, (byte) 0); // reserved
			index += 1;
			buffer.setShort(index, (short) ((fragmentOffset & 0x1fff) << 3 |
							flagType.getValue() & 0x1));
			index += 1;
			buffer.setInt(index, identification);
			return buffer;
		}

	}

	public static final class Builder extends AbstractPacket.PacketBuilder {

		private Ip.Type nextHeader;
		private short fragmentOffset;
		private FlagType flagType;
		private int identification;

		@Override
		public Fragment build() {
			return new Fragment(this);
		}

		@Override
		public Fragment build(final ByteBuf buffer) {
			Builder builder = new Builder();
			int index = 0;
			builder.nextHeader = Ip.Type.valueOf(buffer.getByte(index));
			index += 1;
			index += 1; // reserved
			short sscratch = buffer.getShort(index);
			builder.fragmentOffset = (short) (sscratch >> 3 & 0x1fff);
			builder.flagType = FlagType.valueOf((byte) (sscratch & 0x1));
			index += 2;
			builder.identification = buffer.getInt(index);
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
