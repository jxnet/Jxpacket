package com.ardikars.jxpacket.common.layer;

import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.common.UnknownPacket;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

public final class ApplicationLayer extends NamedNumber<Short, ApplicationLayer> implements Packet.Factory {

    private static final Map<ApplicationLayer, Short> registry =
            new HashMap<>();

    private static final Map<Short, Packet.Builder> builder =
            new HashMap<>();

    public ApplicationLayer(Short value, String name) {
        super(value, name);
    }

    @Override
    public Packet newInstance(ByteBuf buffer) {
        Packet.Builder packetBuilder = builder.get(this.getValue());
        if (packetBuilder == null) {
            if (buffer == null || buffer.capacity() <= 0) {
                return null;
            }
            return new UnknownPacket.Builder().build(buffer);
        }
        return packetBuilder.build(buffer);
    }

    /**
     * @param value value.
     * @return returns {@link DataLinkLayer} object.
     */
    public static ApplicationLayer valueOf(short value) {
        for (Map.Entry<ApplicationLayer, Short> entry : registry.entrySet()) {
            if (entry.getValue() == value) {
                return entry.getKey();
            }
        }
        return new ApplicationLayer((short) -1, "Unknown");
    }

    /**
     *
     * @param dataLinkLayer application type.
     */
    public static void register(ApplicationLayer dataLinkLayer) {
        synchronized (registry) {
            registry.put(dataLinkLayer, dataLinkLayer.getValue());
        }
    }

    /**
     *
     * @param dataLinkLayer application type.
     * @param packetBuilder packet builder.
     */
    public static void register(ApplicationLayer dataLinkLayer, Packet.Builder packetBuilder) {
        synchronized (builder) {
            builder.put(dataLinkLayer.getValue(), packetBuilder);
        }
    }

    static {

    }

}
