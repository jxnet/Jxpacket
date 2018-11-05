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

package com.ardikars.jxpacket.example;

import com.ardikars.jxnet.Context;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.common.layer.NetworkLayer;
import com.ardikars.jxpacket.common.layer.TransportLayer;
import com.ardikars.jxpacket.core.ethernet.Ethernet;
import com.ardikars.jxpacket.core.ip.Ip4;
import com.ardikars.jxpacket.core.tcp.Tcp;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jxpacket 2018/11/05
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@SpringBootApplication
public class ExampleApplication implements CommandLineRunner {

    private static final Logger LOGGER = Logger.getLogger(ExampleApplication.class.getName());

    @Autowired
    private Context context;

    public static void main(String[] args) {
        register();
        SpringApplication.run(ExampleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        context.pcapLoop(100, (user, h, bytes) -> {
            ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(bytes.capacity());
            buffer.setBytes(0, bytes);
            Packet packet = Ethernet.newPacket(buffer);
            packet.forEach(pkt -> LOGGER.info(packet.toString()));
        }, "");
    }

    private static void register() {
        NetworkLayer.register(NetworkLayer.IPV4, new Ip4.Builder());
        TransportLayer.register(TransportLayer.TCP, new Tcp.Builder());
    }

}
