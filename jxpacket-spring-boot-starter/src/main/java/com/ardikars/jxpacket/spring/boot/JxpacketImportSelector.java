package com.ardikars.jxpacket.spring.boot;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author jxpacket 2018/11/07
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */

public class JxpacketImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(importingClassMetadata
                        .getAnnotationAttributes(EnablePcapApi.class.getName(), false));
        EnablePcapApi.PcapApi pcapApi = (EnablePcapApi.PcapApi) attributes
                .getOrDefault("pcapApi", EnablePcapApi.PcapApi.JXNET);
        if (pcapApi == EnablePcapApi.PcapApi.PCAP4J) {
            return new String[] {"com.ardikars.jxpacket.pcap4j.spring.boot.autoconfigure.Pcap4jPacketAutoConfiguration"};
        } else {
            return new String[] {"com.ardikars.jxpacket.jxnet.spring.boot.autoconfigure.JxnetConfigurationProperties"};
        }
    }

}
