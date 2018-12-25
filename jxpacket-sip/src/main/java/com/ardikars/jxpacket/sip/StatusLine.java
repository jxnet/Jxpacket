package com.ardikars.jxpacket.sip;

import com.ardikars.common.annotation.Incubating;
import com.ardikars.jxpacket.sip.util.Version;

/**
 * @author jxpacket 2018/12/07
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Incubating
public class StatusLine {

    private final Version version;
    private final Integer code;
    private final String reason;

    public StatusLine(Builder builder) {
        this.version = builder.version;
        this.code = builder.code;
        this.reason = builder.reason;
    }

    public static class Builder implements com.ardikars.common.util.Builder<StatusLine, String> {

        private Version version;
        private Integer code;
        private String reason;

        public Builder version(Version version) {
            this.version = version;
            return this;
        }

        public Builder code(Integer code) {
            this.code = code;
            return this;
        }

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        @Override
        public StatusLine build() {
            return new StatusLine(this);
        }

        @Override
        public StatusLine build(String value) {
            return new StatusLine(this);
        }

    }

}
