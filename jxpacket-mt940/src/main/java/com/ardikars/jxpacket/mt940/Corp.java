package com.ardikars.jxpacket.mt940;

import lombok.Getter;
import lombok.ToString;

/**
 * @author middleware 2018/10/17
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Getter
@ToString
public class Corp implements Field {

    public static final String TAG = ":2";

    private final String corp;

    private Corp(String corp) {
        this.corp = corp;
    }

    public static class Builder {

        @lombok.Builder
        private static Corp newInstance(String corp) {
            return new Corp(corp);
        }

    }

}
