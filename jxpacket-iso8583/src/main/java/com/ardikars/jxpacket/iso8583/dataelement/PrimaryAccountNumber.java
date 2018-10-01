package com.ardikars.jxpacket.iso8583.dataelement;

public class PrimaryAccountNumber implements DataElement<String> {
    @Override
    public int bit() {
        return 2;
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public String field() {
        return null;
    }
}
