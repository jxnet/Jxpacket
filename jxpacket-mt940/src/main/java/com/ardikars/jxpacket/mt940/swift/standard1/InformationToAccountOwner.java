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

package com.ardikars.jxpacket.mt940.swift.standard1;

import com.ardikars.jxpacket.mt940.util.Mt940Utils;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

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
