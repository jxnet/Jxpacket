package com.ardikars.jxpacket.sip.util;

import com.ardikars.common.annotation.Incubating;
import com.ardikars.common.util.Validate;

/**
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Incubating
public class Version {

    private String name;
    private String code;

    private Version() {
        //
    }

    private Version(Builder builder) {
        this.name = builder.name;
        this.code = builder.code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public static class Builder implements com.ardikars.common.util.Builder<Version, String> {

        private String name;
        private String code;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        @Override
        public Version build() {
            return new Version(this);
        }

        @Override
        public Version build(String value) {
            Validate.notIllegalArgument(value != null && !value.isEmpty());
            String[] strings = value.split("/");
            this.name = strings[0];
            this.code = strings[1];
            return new Version(this);
        }

    }

}
