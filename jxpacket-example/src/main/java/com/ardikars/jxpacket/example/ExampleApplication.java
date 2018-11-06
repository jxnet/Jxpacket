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
import com.ardikars.jxpacket.common.layer.DataLinkLayer;
import com.ardikars.jxpacket.common.layer.NetworkLayer;
import com.ardikars.jxpacket.common.layer.TransportLayer;
import com.ardikars.jxpacket.core.ethernet.Ethernet;
import com.ardikars.jxpacket.core.ethernet.Vlan;
import com.ardikars.jxpacket.core.icmp.Icmp4;
import com.ardikars.jxpacket.core.icmp.Icmp6;
import com.ardikars.jxpacket.core.ip.Ip4;
import com.ardikars.jxpacket.core.ip.Ip6;
import com.ardikars.jxpacket.core.ip.ip6.*;
import com.ardikars.jxpacket.core.tcp.Tcp;
import com.ardikars.jxpacket.core.udp.Udp;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

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

        context.pcapLoop(-1, (user, h, bytes) -> {
            ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(bytes.capacity());
            buffer.setBytes(0, bytes);
            Packet packet = Ethernet.newPacket(buffer);
            packet.forEach(pkt -> CompletableFuture.runAsync(() -> LOGGER.info(pkt.toString())));
        }, "");

        LOGGER.info("Done.");

    }

    private static void register() {
        DataLinkLayer.register(DataLinkLayer.EN10MB, new Ethernet.Builder());
        NetworkLayer.register(NetworkLayer.IPV4, new Ip4.Builder());
        NetworkLayer.register(NetworkLayer.IPV6, new Ip6.Builder());
        NetworkLayer.register(NetworkLayer.DOT1Q_VLAN_TAGGED_FRAMES, new Vlan.Builder());
        NetworkLayer.register(NetworkLayer.IEEE_802_1_AD, new Vlan.Builder());
        TransportLayer.register(TransportLayer.TCP, new Tcp.Builder());
        TransportLayer.register(TransportLayer.UDP, new Udp.Builder());
        TransportLayer.register(TransportLayer.ICMP, new Icmp4.Builder());
        TransportLayer.register(TransportLayer.IPV6_ICMP, new Icmp6.Builder());
        TransportLayer.register(TransportLayer.IPV6_AH, new Authentication.Builder());
        TransportLayer.register(TransportLayer.IPV6_DSTOPT, new DestinationOptions.Builder());
        TransportLayer.register(TransportLayer.IPV6_ROUTING, new Routing.Builder());
        TransportLayer.register(TransportLayer.IPV6_FRAGMENT, new Fragment.Builder());
        TransportLayer.register(TransportLayer.IPV6_HOPOPT, new HopByHopOptions.Builder());
    }

}
