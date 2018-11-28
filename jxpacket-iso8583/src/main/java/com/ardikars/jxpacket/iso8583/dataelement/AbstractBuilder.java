package com.ardikars.jxpacket.iso8583.dataelement;

import com.ardikars.common.annotation.Incubating;

@Incubating
public abstract class AbstractBuilder<T> implements com.ardikars.common.util.Builder<T, Void> {

    @Override
    public T build(Void value) {
        throw new UnsupportedOperationException();
    }

}
