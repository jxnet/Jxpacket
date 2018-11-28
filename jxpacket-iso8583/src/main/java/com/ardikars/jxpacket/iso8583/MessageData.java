package com.ardikars.jxpacket.iso8583;

import com.ardikars.common.util.Builder;
import com.ardikars.jxpacket.iso8583.dataelement.AbstractBuilder;
import com.ardikars.jxpacket.iso8583.dataelement.DataElement;
import sun.plugin2.message.Message;

import java.util.Set;

/**
 * @author jxpacket 2018/10/03
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
public class MessageData {

    private final Set<DataElement<?>> dataElements;

    public MessageData(Builder builder) {
        this.dataElements = null;
    }

    public static class Builder extends AbstractBuilder<MessageData> {

        private final DataElement<?>[] dataElements = new DataElement[128];


        @Override
        public MessageData build() {
            return null;
        }

    }

}
