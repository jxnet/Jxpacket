package com.ardikars.jxpacket.sip;

import com.ardikars.common.annotation.Incubating;

/**
 * @author <a href="mailto:contact@ardikars.com">Ardika Rommy Sanjaya</a>
 */
@Incubating
public enum StartLine {

    REQUEST_LINE {
        @Override
        public String toString() {
            return "Request-Line";
        }
    },
    STATUS_LINE {
        @Override
        public String toString() {
            return "Status-Line";
        }
    }

}
