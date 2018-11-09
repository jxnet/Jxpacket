/**
 * Copyright (C) 2017-2018  Ardika Rommy Sanjaya <contact@ardikars.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ardikars.jxpacket.spring.boot;

import java.util.Map;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * @author jxpacket 2018/11/07
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
public class JxpacketImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(@NonNull AnnotationMetadata importingClassMetadata) {
        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(EnablePcapApi.class.getName(), false);
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(map);
        if (attributes == null) {
            throw new IllegalArgumentException();
        }
        EnablePcapApi.PcapApi pcapApi = (EnablePcapApi.PcapApi) attributes
                .getOrDefault("pcapApi", EnablePcapApi.PcapApi.JXNET);
        if (pcapApi == EnablePcapApi.PcapApi.PCAP4J) {
            return new String[] {"com.ardikars.jxpacket.pcap4j.spring.boot.autoconfigure.Pcap4jPacketAutoConfiguration"};
        } else {
            return new String[] {"com.ardikars.jxpacket.jxnet.spring.boot.autoconfigure.JxnetConfigurationProperties"};
        }
    }

}
