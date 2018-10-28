package com.ardikars.jxpacket.mt940.halcom.standard1;

import com.ardikars.jxpacket.mt940.util.Mt940Utils;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * @author middleware 2018/10/18
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Getter
@ToString
public class InformationToAccountOwner implements com.ardikars.jxpacket.mt940.InformationToAccountOwner {

    public static final String TAG = ":86";

    private final List<String> data;

    public InformationToAccountOwner(List<String> data) {
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
