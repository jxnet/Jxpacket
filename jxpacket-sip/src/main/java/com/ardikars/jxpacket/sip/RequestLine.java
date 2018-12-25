package com.ardikars.jxpacket.sip;

import com.ardikars.common.annotation.Incubating;
import com.ardikars.common.util.Validate;
import com.ardikars.jxpacket.sip.util.Method;
import com.ardikars.jxpacket.sip.util.RequestUri;
import com.ardikars.jxpacket.sip.util.Version;

/**
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Incubating
public class RequestLine {

    private final Method method;
    private final RequestUri requestUri;
    private final Version version;

    public RequestLine(Builder builder) {
        this.method = builder.method;
        this.requestUri = builder.requestUri;
        this.version = builder.version;
    }

    public static class Builder implements com.ardikars.common.util.Builder<RequestLine, String> {

        private Method method;
        private RequestUri requestUri;
        private Version version;

        public Builder method(Method method) {
            this.method = method;
            return this;
        }

        public Builder requestUri(RequestUri requestUri) {
            this.requestUri = requestUri;
            return this;
        }

        public Builder version(Version version) {
            this.version = version;
            return this;
        }

        @Override
        public RequestLine build() {
            return new RequestLine(this);
        }

        @Override
        public RequestLine build(String value) {
            Validate.notIllegalArgument(value != null && !value.isEmpty());
            String[] strings = value.split(" ");
            this.method = Method.fromString(strings[0]);
            this.requestUri = new RequestUri.Builder().build(strings[1]);
            this.version = new Version.Builder().build(strings[2]);
            return new RequestLine(this);
        }

    }

}
