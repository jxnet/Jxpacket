package jxpacket.common.util;

import jxpacket.common.net.MacAddress;

public class Main {

	public static void main(String[] args) {
		MacAddress macAddress = MacAddress.valueOf("48-50-73:da:ed:ea");
		System.out.println(macAddress.getOui());
	}
}
