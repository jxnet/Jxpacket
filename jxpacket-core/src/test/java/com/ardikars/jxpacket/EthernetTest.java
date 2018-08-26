package com.ardikars.jxpacket;

import com.ardikars.jxpacket.ethernet.Ethernet;
import com.ardikars.jxpacket.ip.Ip;
import com.ardikars.jxpacket.ip.Ip6;
import com.ardikars.jxpacket.ip.ip6.Authentication;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.StringUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.StreamSupport;

public class EthernetTest extends BaseTest {

	private byte[] data = StringUtil.decodeHexDump(ICMP4_ECHO_REPLY);

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
		ethernet.get(Ip6.class, packet -> packet.getHeader().getPayloadType() == Ip.Type.IPV6_AH)
				.stream().map(pkt -> pkt.getHeader())
				.forEach(System.out::println);
	}

	@After
	public void after() {
//		buf.release(); // buffer already release to the pool
	}

}
