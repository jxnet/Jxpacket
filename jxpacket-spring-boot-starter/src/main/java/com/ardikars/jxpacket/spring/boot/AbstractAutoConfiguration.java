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

package com.ardikars.jxpacket.spring.boot;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author jxpacket 2018/11/10
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
public abstract class AbstractAutoConfiguration {

    @Value("${spring.application.name:}")
    private String applicationName;

    @Value("${spring.application.displayName:}")
    private String applicationDisplayName;

    @Value("${spring.application.version:0.0.0}")
    private String applicationVersion;

    public String getApplicationName() {
        return applicationName;
    }

    public String getApplicationDisplayName() {
        return applicationDisplayName;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public abstract String prettyApplicationInformation();

    @Override
    public String toString() {
        return new StringBuilder("{")
                .append("applicationName='").append(applicationName).append('\'')
                .append(", applicationDisplayName='").append(applicationDisplayName).append('\'')
                .append(", applicationVersion='").append(applicationVersion).append('\'')
                .append('}').toString();
    }

}
