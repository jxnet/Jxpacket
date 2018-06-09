module jxpacket {

	exports jxpacket;
	exports jxpacket.ethernet;
	exports jxpacket.arp;
	exports jxpacket.ip;
	exports jxpacket.ip.ipv6;

	requires jxpacket.common;
	requires io.netty.buffer;
	requires io.netty.common;

}