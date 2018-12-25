package com.ardikars.jxpacket.sip.util;

import com.ardikars.common.annotation.Incubating;
import com.ardikars.common.util.Validate;

import java.io.Serializable;

/**
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Incubating
public class RequestUri implements Serializable {

    private String type;
    private String user;
    private String host;
    private Integer port;

    private RequestUri() {
        //
    }

    private RequestUri(Builder builder) {
        this.type = builder.type;
        this.user = builder.user;
        this.host = builder.host;
        this.port = builder.port == null ? 0 : builder.port;
    }

    public static class Builder implements com.ardikars.common.util.Builder<RequestUri, String> {

        private String type;
        private String user;
        private String host;
        private Integer port;

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(Integer port) {
            this.port = port;
            return this;
        }

        @Override
        public RequestUri build() {
            return new RequestUri(this);
        }

        @Override
        public RequestUri build(String value) {
            Validate.notIllegalArgument(value != null && !value.isEmpty());
            String[] strings = value.split(":");
            Validate.notIllegalArgument(strings.length != 0);
            this.type = strings[0];
            if (strings[1].contains("@")) {
                String[] userHostAndPort = strings[1].split("@");
                this.user = userHostAndPort[0];
                this.host = userHostAndPort[1];
            } else {
                this.host = strings[1];
            }
            if (strings.length > 2) {
                this.port = Integer.parseInt(strings[2]);
            }
            return new RequestUri(this);
        }

    }

}
