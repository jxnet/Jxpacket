package jxpacket;

import io.netty.buffer.ByteBuf;
import io.netty.util.internal.StringUtil;
import jxpacket.ethernet.Ethernet;
import jxpacket.ethernet.Vlan;
import jxpacket.ip.Ip;
import jxpacket.ip.Ipv6;
import jxpacket.ip.ipv6.Authentication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.StreamSupport;

public class EthernetTest extends BaseTest {

	private byte[] data = StringUtil.decodeHexDump(IPV6_AH);

	private Ethernet ethernet;
	private ByteBuf buf = allocator.directBuffer(1500);

	@Before
	public void before() {
		buf.setBytes(0, data);
		ethernet = Ethernet.newPacket(buf);
	}

	@Test
	public void payloadType() {
		StreamSupport.stream(ethernet.spliterator(), false)
				.map(packet -> packet.getHeader())
				.forEach(System.out::println);
	}

	@Test
	public void filter() {
		ethernet.get(Authentication.class)
				.stream().map(pkt -> pkt.getHeader())
				.forEach(System.out::println);
	}

	@Test
	public void filterWithPredicate() {
		ethernet.get(Ipv6.class, packet -> packet.getHeader().getPayloadType() == Ip.IpType.IPV6_AH)
				.stream().map(pkt -> pkt.getHeader())
				.forEach(System.out::println);
	}

	@After
	public void after() {
		buf.release();
	}

}
