package com.ardikars.jxpacket.core;

import com.ardikars.common.memory.Memories;
import com.ardikars.common.memory.MemoryAllocator;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.common.layer.DataLinkLayer;
import com.ardikars.jxpacket.common.layer.NetworkLayer;
import com.ardikars.jxpacket.common.layer.TransportLayer;
import com.ardikars.jxpacket.core.arp.Arp;
import com.ardikars.jxpacket.core.ethernet.Ethernet;
import com.ardikars.jxpacket.core.ethernet.Vlan;
import com.ardikars.jxpacket.core.icmp.Icmp;
import com.ardikars.jxpacket.core.icmp.Icmp6;
import com.ardikars.jxpacket.core.ip.Ip4;
import com.ardikars.jxpacket.core.ip.Ip6;
import com.ardikars.jxpacket.core.ip.ip6.*;
import com.ardikars.jxpacket.core.ndp.NeighborAdvertisement;
import com.ardikars.jxpacket.core.ndp.NeighborSolicitation;
import com.ardikars.jxpacket.core.ndp.RouterAdvertisement;
import com.ardikars.jxpacket.core.ndp.RouterSolicitation;
import com.ardikars.jxpacket.core.tcp.Tcp;
import com.ardikars.jxpacket.core.udp.Udp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Iterator;

@RunWith(JUnit4.class)
public abstract class BaseTest {

	protected static final String ETHERNET_II_IEEE_802_1ad_802_1Q_IPV4 =
			"00000000000000109400001588a8001e810020650800450005c254b00000fffdddbdc0550117c055010f0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000004fdccd64218db64ea845f880dd1739726263f12ece8e831b";

	protected static final String ETHERNET_II_Q_IN_Q_ARP = "ffffffffffffca030db4001c81000064810000c808060001080006040001ca030db4001cc0a802c8000000000000c0a802fe0000000000000000000000000000";
	protected static final String ETHERNET_II_ARP = "ffffffffffff001a6b6c0ccc08060001080006040001001a6b6c0ccc0a0a0a020000000000000a0a0a01000000000000000000000000000000000000";

	protected static final String NEIGHBOR_SOLICITATION = "3333fff50000c20054f5000086dd6e00000000183aff00000000000000000000000000000000ff0200000000000000000001fff500008700673c00000000fe80000000000000c00054fffef50000";
	protected static final String NEIGHBOR_ADVERTISEMENT = "333300000001c20054f5000086dd6e00000000203afffe80000000000000c00054fffef50000ff02000000000000000000000000000188009abba0000000fe80000000000000c00054fffef500000201c20054f50000";
	protected static final String ROUTER_ADVERTISEMENT = "333300000001c20054f5000086dd6e00000000403afffe80000000000000c00054fffef50000ff0200000000000000000000000000018600c4fe4000070800000000000000000101c20054f5000005010000000005dc030440c000278d0000093a800000000020010db8000000010000000000000000";
	protected static final String MULTICST_LISTENER_REPORT_MESSAGE_V2 = "333300000016c20054f5000086dd6e00000000240001fe80000000000000c00054fffef50000ff0200000000000000000000000000163a000502000001008f005c130000000104000000ff020000000000000000000000000002";
	protected static final String MULTICST_LISTENER_REPORT = "3333ff0e4c67000c290e4c6786dd600000000020000100000000000000000000000000000000ff0200000000000000000001ff0e4c673a000502000001008300e7b800000000ff0200000000000000000001ff0e4c67";

	protected static final String IPV6_AH = "333300000005c20168b3000186dd6e00000000403301fe800000000000000000000000000002ff02000000000000000000000000000559040000000001000000002035482148b2435a23dcdd5536030100280202020200000001f17300000000000501000013000a0028020202020101010101010101";

	protected static final String IPV6_ROUTING_ICMP6 = "0013c4c784f000123fae22f786dd6000000000202b04220000000000024402123ffffeae22f7220000000000024000020000000000043a02000100000000220000000000021000020000000000048000d3ab00000000";

	protected static final String IPV6_ROUTING_UDP = "0013c4c784f000123fae22f786dd6000000000202b04220000000000024402123ffffeae22f722000000000002400002000000000004110200010000000022000000000002100002000000000004160d160a000827b6";

	protected static final String ETHERNET_IPV4_TCP_SYN = "001f295e4d26005056bb3aa008004510003c831b40004006150ac0a814464a7d831bd51d00196b7fc72d00000000a0027210a2b50000020405b40402080a0a9944360000000001030307";

	protected static final String ETHERNET_IPV4_UDP = "000c4182b25300d0596c404e08004500003d0a41000080117cebc0a83232c0a80001ff02ff35002907a9002b0100000100000000000002757304706f6f6c036e7470036f72670000010001";

	protected static final String ICMP4_ECHO_REQUEST = "005056e01449000c29340bde08004500003cd743000080012b73c0a89e8bae892a4d08002a5c020021006162636465666768696a6b6c6d6e6f7071727374757677616263646566676869";

	protected static final String ICMP4_ECHO_REPLY = "000c29340bde005056e0144908004500003c76e1000080018bd5ae892a4dc0a89e8b0000325c020021006162636465666768696a6b6c6d6e6f7071727374757677616263646566676869";

	protected static final String IPV6_TCP_SYN = "000573a007d168a3c4f949f686dd600000000020064020010470e5bfdead49572174e82c48872607f8b0400c0c03000000000000001af9c7001903a088300000000080022000da4700000204058c0103030801010402";

	protected static final String IPV6_UDP = "33330001000300123f97920186dd6000000000291101fe800000000000009c09b4160768ff42ff020000000000000000000000010003cd1b14eb0029563fd0c8000000010000000000000f63686d757468752d77372d746573740000ff0001";

	protected static final String IPV6_OVER_IPV4 = "68a3c4f949f65475d0c90b8108004500007c000100003f29c4370a000096ac1c006f6000000000403a64fe80000000000000025056fffe8706b6ff020000000000000000000000000001860007ad0008070800000000000000000101005056243bc00501000000000500030440c0ffffffffffffffff0000000020010db8000100010000000000000000";

	protected static final String IPV6_HOP_BY_HOP_OPTION = "333300000000c4041517887186dd600000000020000100000000000000000000000000000000ff0200000000000000000000000000013a0005020000000082007ac103e8000000000000000000000000000000000000";

	protected static final String IPV6_AUTHENTICATION = "333300000005c20068b3000186dd6e000000003c3301fe800000000000000000000000000001ff02000000000000000000000000000559040000000001000000001321d3a95c5ffd4d184622b9f8030100240101010100000001fb8600000000000501000013000a00280000000000000000";

	protected static final String IPV6_FRAGMENTATION = "001d09946538685b35c061b686dd60021289041b2c402607f01003f9000000000000000010012607f01003f900000000000000110000110010f8f88eb46668686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868686868163c1b01011c370735080c746d702d6964656e74697479080412c2999e08034b455908116b736b2d31343333393830383736333036080749442d4345525417fd0100306f6bab96619b712af4a0de3584ed89b6e7093ee95eb9cd1f66375ffabc52ee40f6d1c560ff78f151c91bc99cd0b0ac0f422e1cc1ea5b50ff5099606d02edec2cd6971b9af717deef586bc0636d641277b234be05798a592c2594528e1ed4dc12e76066c909e59b53f2ed371791908de68a47cdcc47abd2becba83f887bcc5880ddb208307995b38bd05bc5138c11ab2b91ac3c55a9520212f1b82fcaead070563c2a366ed2570ce69bbcc85b1c39ed9b7cfaa72b661710409e0fe090d161fdbbc86d1bcfec81f7de24c312ac39fdb3a85735c6fb1a15522329a125ca669805a67b0d49e1763407f79ee5b76639b1b0a01a4490ce0703f21b0ec617edd84f4f";

	protected static final String NDP_NEIGHBOR_SOLICITATION = "3333fff50000c20054f5000086dd6e00000000183aff00000000000000000000000000000000ff0200000000000000000001fff500008700673c00000000fe80000000000000c00054fffef50000";

	protected static final String NDP_NEIGHBOR_ADVERTISEMENT = "333300000001c20054f5000086dd6e00000000203afffe80000000000000c00054fffef50000ff02000000000000000000000000000188009abba0000000fe80000000000000c00054fffef500000201c20054f50000";

	protected static final String NDP_ROUTER_SOLICITATION = "333300000002000c290e4c6786dd6000000000083aff00000000000000000000000000000000ff02000000000000000000000000000285007bb800000000";

	protected static final String NDP_ROUTER_ADVERTISEMENT = "333300000001c20054f5000086dd6e00000000403afffe80000000000000c00054fffef50000ff0200000000000000000000000000018600c4fe4000070800000000000000000101c20054f5000005010000000005dc030440c000278d0000093a800000000020010db8000000010000000000000000";

	protected MemoryAllocator allocator = Memories.allocator();

	protected Ethernet ethernet;

	@Before
	public void registerPacket() {
		DataLinkLayer.register(DataLinkLayer.EN10MB, new Ethernet.Builder());
		NetworkLayer.register(NetworkLayer.DOT1Q_VLAN_TAGGED_FRAMES, new Vlan.Builder());
		NetworkLayer.register(NetworkLayer.ARP, new Arp.Builder());
		NetworkLayer.register(NetworkLayer.IPV4, new Ip4.Builder());
		NetworkLayer.register(NetworkLayer.IPV6, new Ip6.Builder());
		TransportLayer.register(TransportLayer.TCP, new Tcp.Builder());
		TransportLayer.register(TransportLayer.UDP, new Udp.Builder());
		TransportLayer.register(TransportLayer.IPV6, new Ip6.Builder());
		TransportLayer.register(TransportLayer.IPV6_ICMP, new Icmp6.Builder());
		TransportLayer.register(TransportLayer.IPV6_AH, new Authentication.Builder());
		TransportLayer.register(TransportLayer.IPV6_DSTOPT, new DestinationOptions.Builder());
		TransportLayer.register(TransportLayer.IPV6_ROUTING, new Routing.Builder());
		TransportLayer.register(TransportLayer.IPV6_FRAGMENT, new Fragment.Builder());
		TransportLayer.register(TransportLayer.IPV6_HOPOPT, new HopByHopOptions.Builder());
		Icmp.IcmpTypeAndCode.register(Icmp.IcmpTypeAndCode.NEIGHBOR_SOLICITATION, new NeighborSolicitation.Builder());
		Icmp.IcmpTypeAndCode.register(Icmp.IcmpTypeAndCode.NEIGHBOR_ADVERTISEMENT, new NeighborAdvertisement.Builder());
		Icmp.IcmpTypeAndCode.register(Icmp.IcmpTypeAndCode.ROUTER_SOLICICATION, new RouterSolicitation.Builder());
		Icmp.IcmpTypeAndCode.register(Icmp.IcmpTypeAndCode.ROUTER_ADVERTISEMENT, new RouterAdvertisement.Builder());
		before();
	}

	public abstract void before();

	@Test
	public void printAllPackets() {
		if (ethernet != null) {
			Iterator<Packet> packetIterator = ethernet.iterator();
			while (packetIterator.hasNext()) {
				System.out.println(packetIterator.next());
			}
		}
	}

}
