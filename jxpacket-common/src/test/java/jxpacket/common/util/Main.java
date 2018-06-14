package jxpacket.common.util;

import jxpacket.common.NamedTwoKeyMap;
import jxpacket.common.TwoKeyMap;
import jxpacket.common.net.MacAddress;

import java.util.HashMap;
import java.util.Map;

public class Main {

	public static void main(String[] args) {

		Map<TwoKeyMap<String, Integer>, Long> mapMap = new HashMap();
		mapMap.put(TwoKeyMap.newInstance("Satu", 1), 1L);

		System.out.println(mapMap);
	}

}
