package jxpacket;

import jxpacket.arp.Arp;
import jxpacket.common.NamedNumber;
import jxpacket.ethernet.Vlan;
import jxpacket.ip.Ipv4;
import jxpacket.ip.Ipv6;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class AbstractPacket implements Packet {

//	@Override
//	public Header getHeader() {
//		throw new UnsupportedOperationException();
//	}
//
//	@Override
//	public Packet getPayload() {
//		throw new UnsupportedOperationException();
//	}

	public <T extends Packet> List<T> get(Class<T> clazz) {
		return StreamSupport.stream(this.spliterator(), false)
				.filter(packet -> clazz.isInstance(packet))
				.map(packet -> clazz.cast(packet))
				.collect(Collectors.toList());
	}

	public <T extends Packet> List<T> get(Class<T> clazz, Predicate<Packet> predicate) {
		return StreamSupport.stream(this.spliterator(), false)
				.filter(packet -> clazz.isInstance(packet))
				.filter(predicate)
				.map(packet -> clazz.cast(packet))
				.collect(Collectors.toList());
	}

	@Override
	public <T extends Packet> boolean contains(Class<T> clazz) {
		return get(clazz) != null;
	}

	@Override
	public Iterator<Packet> iterator() {
		return new PacketIterator(this);
	}

	protected Builder getPayloadBuilder(final Header header) {
		return AbstractPacket.PacketBuilder.getBuilder(header.getPayloadType());
	}

	public static abstract class PacketHeader implements Packet.Header {

//		@Override
//		public <T extends NamedNumber> T getPayloadType() {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public int getLength() {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public ByteBuf getBuffer() {
//			throw new UnsupportedOperationException();
//		}

	}

	public static abstract class PacketBuilder implements Packet.Builder {

		private static Map<ProtocolType, Builder> registry = new HashMap<>();

//		@Override
//		public Packet build() {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public Packet build(final ByteBuf buffer) {
//			throw new UnsupportedOperationException();
//		}

		public static <T extends NamedNumber> Packet.Builder getBuilder(T protocolType) {
			Packet.Builder builder = registry.get(protocolType);
			if (builder == null) {
				return new UnknownPacket.Builder();
			}
			return builder;
		}

		static {
			registry.put(ProtocolType.IEEE_802_1_AD, new Vlan.Builder());
			registry.put(ProtocolType.DOT1Q_VLAN_TAGGED_FRAMES, new Vlan.Builder());
			registry.put(ProtocolType.ARP, new Arp.Builder());
			registry.put(ProtocolType.IPV4, new Ipv4.Builder());
			registry.put(ProtocolType.IPV6, new Ipv6.Builder());
			registry.put(ProtocolType.UNKNOWN, new UnknownPacket.Builder());
		}

	}

}