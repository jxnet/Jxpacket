package com.ardikars.jxpacket;

import com.ardikars.common.util.NamedNumber;

import java.util.HashMap;
import java.util.Map;

/**
 * @see <a href="https://www.rfc-editor.org/rfc/rfc7133.txt">Data link layer</a>
 */
public class DataLinkType extends NamedNumber<Short,DataLinkType> {

	/**
	 * Ethernet (10Mb, 100Mb, 1000Mb, and up): 1
	 */
	public static final DataLinkType EN10MB = new DataLinkType((short) 1, "Ethernet");

	public static final DataLinkType UNKNOWN = new DataLinkType((short) -1, "Unknown");

	private static final Map<Short, DataLinkType> registry
			= new HashMap<>();

	protected DataLinkType(Short value, String name) {
		super(value, name);
	}

	public static DataLinkType valueOf(final Short value) {
		DataLinkType dataLinkType = registry.get(value);
		if (dataLinkType == null) {
			return UNKNOWN;
		}
		return dataLinkType;
	}

	public static DataLinkType register(final DataLinkType dataLinkType) {
		registry.put(dataLinkType.getValue(), dataLinkType);
		return dataLinkType;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	static {
		registry.put(EN10MB.getValue(), EN10MB);
		registry.put(UNKNOWN.getValue(), UNKNOWN);
	}

}
