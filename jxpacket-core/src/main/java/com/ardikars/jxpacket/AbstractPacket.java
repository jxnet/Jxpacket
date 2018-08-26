package com.ardikars.jxpacket;

import com.ardikars.jxpacket.arp.Arp;
import com.ardikars.jxpacket.ethernet.Vlan;
import com.ardikars.jxpacket.icmp.Icmp4;
import com.ardikars.jxpacket.ip.Ip;
import com.ardikars.jxpacket.ip.Ip4;
import com.ardikars.jxpacket.ip.Ip6;

import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.tcp.Tcp;
import com.ardikars.jxpacket.udp.Udp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class AbstractPacket implements Packet {

	@Override
	public <T extends Packet> List<T> get(Class<T> clazz) {
		return get(clazz, packet -> true);
	}

	@Override
	public <T extends Packet> List<T> get(Class<T> clazz, Predicate<Packet> predicate) {
		return StreamSupport.stream(this.spliterator(), true)
				.filter(packet -> clazz.isInstance(packet))
				.filter(predicate)
				.map(packet -> clazz.cast(packet))
				.collect(Collectors.toList());
	}

	@Override
	public <T extends Packet> Stream<T> getAsStream(Class<T> clazz) {
		return getAsStream(clazz, packet -> true);
	}

	@Override
	public <T extends Packet> Stream<T> getAsStream(Class<T> clazz, Predicate<Packet> predicate) {
		return StreamSupport.stream(this.spliterator(), true)
				.filter(packet -> clazz.isInstance(packet))
				.filter(predicate)
				.map(packet -> clazz.cast(packet));
	}

	@Override
	public <T extends Packet> T getFirst(Class<T> clazz) {
		return getFirst(clazz, packet -> true);
	}

	@Override
	public <T extends Packet> T getFirst(Class<T> clazz, Predicate<Packet> predicate) {
		return StreamSupport.stream(this.spliterator(), true)
				.filter(packet -> clazz.isInstance(packet))
				.filter(predicate)
				.map(packet -> clazz.cast(packet))
				.findFirst().get();
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

	}

	public static abstract class PacketBuilder implements Packet.Builder {

		private static Map<ProtocolType, Builder> dataLinkRegistry = new HashMap<>();
		private static Map<Ip.Type, Builder> networkRegistry = new HashMap<>();

		public static <T extends NamedNumber> Packet.Builder getBuilder(T protocolType) {
			if (protocolType instanceof ProtocolType) {
				Packet.Builder builder = dataLinkRegistry.get(protocolType);
				if (builder == null) {
					return new UnknownPacket.Builder();
				}
				return builder;
			} else if (protocolType instanceof Ip.Type) {
				Packet.Builder builder = networkRegistry.get(protocolType);
				if (builder == null) {
					return new UnknownPacket.Builder();
				}
				return builder;
			}
			return new UnknownPacket.Builder();
		}

		static {
			dataLinkRegistry.put(ProtocolType.IEEE_802_1_AD, new Vlan.Builder());
			dataLinkRegistry.put(ProtocolType.DOT1Q_VLAN_TAGGED_FRAMES, new Vlan.Builder());
			dataLinkRegistry.put(ProtocolType.ARP, new Arp.Builder());
			dataLinkRegistry.put(ProtocolType.IPV4, new Ip4.Builder());
			dataLinkRegistry.put(ProtocolType.IPV6, new Ip6.Builder());
			dataLinkRegistry.put(ProtocolType.UNKNOWN, new UnknownPacket.Builder());

			networkRegistry.put(Ip.Type.TCP, new Tcp.Builder());
			networkRegistry.put(Ip.Type.UDP, new Udp.Builder());
			networkRegistry.put(Ip.Type.ICMP, new Icmp4.Builder());
		}

	}

}
