package com.ardikars.jxpacket.spring.boot;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

/**
 * @author jxpacket 2018/11/09
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Import(JxpacketImportSelector.class)
public class JxpacketAutoConfiguration {

}
