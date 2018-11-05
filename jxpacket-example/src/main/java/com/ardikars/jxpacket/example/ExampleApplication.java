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

    @Autowired
    private Context context;

    public static void main(String[] args) {
        register();
        SpringApplication.run(ExampleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        context.pcapLoop(1000, (user, h, bytes) -> {
            ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(bytes.capacity());
            buffer.setBytes(0, bytes);
            Packet packet = Ethernet.newPacket(buffer);
            packet.forEach(pkt -> System.out.println(pkt.getHeader()));
        }, "");
    }

    private static void register() {
        NetworkLayer.register(NetworkLayer.IPV4, new Ip4.Builder());
        TransportLayer.register(TransportLayer.TCP, new Tcp.Builder());
    }

}
