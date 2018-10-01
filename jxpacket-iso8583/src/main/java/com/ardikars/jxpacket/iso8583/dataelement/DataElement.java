package com.ardikars.jxpacket.iso8583.dataelement;

import com.ardikars.common.annotation.Incubating;

import java.io.Serializable;

@Incubating
public interface DataElement<T> extends Serializable {

    int bit();

    int length();

    T field();

}
