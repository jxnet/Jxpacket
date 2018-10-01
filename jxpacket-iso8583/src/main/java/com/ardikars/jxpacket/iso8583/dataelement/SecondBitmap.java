package com.ardikars.jxpacket.iso8583.dataelement;

public class SecondBitmap implements DataElement<Boolean> {

    private final boolean secondBitmap;

    public SecondBitmap(boolean secondBitmap) {
        this.secondBitmap = secondBitmap;
    }

    @Override
    public int bit() {
        return 1;
    }

    @Override
    public int length() {
        return 1;
    }

    @Override
    public Boolean field() {
        return secondBitmap;
    }

}