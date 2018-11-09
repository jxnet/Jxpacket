package com.ardikars.jxpacket.spring.boot;

/**
 * @author jxpacket 2018/11/09
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jxpacket 2018/11/09
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnablePcapApi {

    PcapApi pcapApi() default PcapApi.JXNET;

    enum PcapApi {
        JXNET, PCAP4J
    }

}
