package com.ardikars.jxpacket.mt940.swift.standard2;

import com.ardikars.jxpacket.mt940.util.Mt940Utils;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * @author jxpacket 2018/10/16
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Getter
@ToString
public class InformationToAccountOwner implements com.ardikars.jxpacket.mt940.InformationToAccountOwner {

    public static final String TAG = ":86";

    private final List<String> data;

    private InformationToAccountOwner(List<String> data) {
        this.data = data;
    }

    public static class Builder {

        @lombok.Builder
        private static InformationToAccountOwner newInstance(String informationToAccountOwner) {
            List<String> parsedInformationToAccountOwner = Mt940Utils.parseInformationToAccountOwner(informationToAccountOwner);
            return new InformationToAccountOwner(parsedInformationToAccountOwner);
        }

    }

}
