//package com.ardikars.jxpacket;
//
//import com.ardikars.jxpacket.ethernet.Ethernet;
//import com.ardikars.jxpacket.radiotap.RadioTap;
//import com.ardikars.jxpacket.sll.SLL;
//import org.jxpcap.util.ByteUtils;
//
//import java.nio.ByteBuffer;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.concurrent.Executor;
//
///**
// * Created by Ardika on 16/08/17.
// * All will be fine
// */
//public class PacketListener {
//
////    @FunctionalInterface // JAVA 8
//    public interface Map<T> {
//
//        /**
//         * Next available packet.
//         * @param arg user illegalArgument.
//         * @param pktHdr PcapPktHdr.
//         * @param packets packet.
//         */
//        void nextPacket(T arg, PcapPktHdr pktHdr, java.util.Map<Class, Packet> packets);
//
//    }
//
////    @FunctionalInterface // JAVA 8
//    public interface List<T> {
//
//        /**
//         * Next available packet.
//         * @param arg user illegalArgument.
//         * @param pktHdr PcapPktHdr.
//         * @param packets packet.
//         */
//        void nextPacket(T arg, PcapPktHdr pktHdr, java.util.List<Packet> packets);
//
//    }
//
//    public static <T> int loop(final Pcap pcap, final int count, final PacketListener.List<T> packetList, final T arg) {
//        PcapHandler<List<T>> handler = new PcapHandler<List<T>>() {
//            @Override
//            public void nextPacket(List<T> user, PcapPktHdr h, ByteBuffer bytes) {
//                java.util.List<Packet> packets = parseList(pcap.getDataLinkType(), ByteUtils.toByteArray(bytes));
//                user.nextPacket(arg, h, packets);
//            }
//        };
//        return Jxnet.PcapLoop(pcap, count, handler, packetList);
//    }
//
//    public static <T> int loop(final Pcap pcap, final int count, final PacketListener.Map<T> packetMap, final T arg) {
//        PcapHandler<PacketListener.Map<T>> handler = new PcapHandler<Map<T>>() {
//            @Override
//            public void nextPacket(Map<T> user, PcapPktHdr h, ByteBuffer bytes) {
//                java.util.Map<Class, Packet> packets = parseMap(pcap.getDataLinkType(), ByteUtils.toByteArray(bytes));
//                user.nextPacket(arg, h, packets);
//            }
//        };
//        return Jxnet.PcapLoop(pcap, count, handler, packetMap);
//    }
//
//    /*public static <T, V extends Packet> int loop(final Pcap pcap, final int count, final PacketProcessor<T, V> handler, final T arg) {
//        PcapHandler<PacketProcessor<T, V>> callback = new PcapHandler<PacketProcessor<T, V>>() {
//            @Override
//            public void nextPacket(PacketProcessor<T, V> user, PcapPktHdr h, ByteBuffer bytes) {
//                user.initialize(arg, pcap, h);
//                user.decode(ByteUtils.toByteArray(bytes));
//            }
//        };
//        return Jxnet.PcapLoop(pcap, count, callback, handler);
//    }*/
//
//    public static java.util.List<Packet> nextList(Pcap pcap, PcapPktHdr pcapPktHdr) {
//        ByteBuffer buffer = Jxnet.PcapNext(pcap, pcapPktHdr);
//        return parseList(pcap.getDataLinkType(), ByteUtils.toByteArray(buffer));
//    }
//
//    public static java.util.Map<Class, Packet> nextMap(Pcap pcap, PcapPktHdr pcapPktHdr) {
//        ByteBuffer buffer = Jxnet.PcapNext(pcap, pcapPktHdr);
//        if (buffer == null) {
//            return null;
//        }
//        return parseMap(pcap.getDataLinkType(), ByteUtils.toByteArray(buffer));
//    }
//
//    public static int nextExList(Pcap pcap, PcapPktHdr pktHdr, java.util.List<Packet> packets) {
//        packets.clear();
//        ByteBuffer buffer = ByteBuffer.allocateDirect(pcap.getSnapshotLength());
//        int ret = Jxnet.PcapNextEx(pcap, pktHdr, buffer);
//        java.util.List<Packet> pkts = parseList(pcap.getDataLinkType(), ByteUtils.toByteArray(buffer));
//        packets.addAll(pkts);
//        return ret;
//    }
//
//    public static int nextExMap(Pcap pcap, PcapPktHdr pktHdr, HashMap<Class, Packet> packets) {
//        packets.clear();
//        ByteBuffer buffer = ByteBuffer.allocateDirect(pcap.getSnapshotLength());
//        int ret = Jxnet.PcapNextEx(pcap, pktHdr, buffer);
//        java.util.Map<Class, Packet> pkts = parseMap(pcap.getDataLinkType(), ByteUtils.toByteArray(buffer));
//        packets.putAll(pkts);
//        return ret;
//    }
//
//    public static java.util.Map<Class, Packet> parseMap(DataLinkType linkType, byte[] bytes) {
//        java.util.Map<Class, Packet> packets = new HashMap<Class, Packet>();
//        if (bytes == null) {
//            packets.clear();
//            return packets;
//        }
//        Packet packet = null;
//        switch (linkType) {
//            case EN10MB:
//                packet = Ethernet.newInstance(bytes);
//                packets.put(packet.getClass(), packet);
//                break;
//            case IEEE802_11_RADIO:
//                packet = RadioTap.newInstance(bytes);
//                packets.put(packet.getClass(), packet);
//                break;
//            case LINUX_SLL:
//                packet = SLL.newInstance(bytes);
//                packets.put(packet.getClass(), packet);
//                break;
//            default:
//                packet = UnknownPacket.newInstance(bytes);
//        }
//        while ((packet = packet.getPacket()) != null) {
//            packets.put(packet.getClass(), packet);
//        }
//        return packets;
//    }
//
//    public static java.util.List<Packet> parseList(DataLinkType linkType, byte[] bytes) {
//        java.util.List<Packet> packets = new ArrayList<>();
//        if (bytes == null) {
//            packets.clear();
//            return packets;
//        }
//        Packet packet = null;
//        switch (linkType) {
//            case EN10MB:
//                packet = Ethernet.newInstance(bytes);
//                packets.add(packet);
//                break;
//            case IEEE802_11_RADIO:
//                packet = RadioTap.newInstance(bytes);
//                packets.add(packet);
//                break;
//            case LINUX_SLL:
//                packet = SLL.newInstance(bytes);
//                packets.add(packet);
//                break;
//            default:
//                packet = UnknownPacket.newInstance(bytes);
//        }
//        while ((packet = packet.getPacket()) != null) {
//            packets.add(packet);
//        }
//        return packets;
//    }
//
//    @SuppressWarnings("unchecked")
//    public static <T> int loop(final Pcap pcap, final int count, final PacketListener.List<T> packetList, final T arg, final Executor executor) {
//        PcapHandler<PacketListener.List<T>> handler = new PcapHandler<List<T>>() {
//            @Override
//            public void nextPacket(final List<T> user, final PcapPktHdr h, final ByteBuffer bytes) {
//                executor.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        java.util.List<Packet> packets = parseList(pcap.getDataLinkType(), ByteUtils.toByteArray(bytes));
//                        user.nextPacket(arg, h, packets);
//                    }
//                });
//            }
//        };
//        return Jxnet.PcapLoop(pcap, count, handler, packetList);
//    }
//
//    @SuppressWarnings("unchecked")
//    public static <T> int loop(final Pcap pcap, final int count, final PacketListener.Map<T> packetMap, final T arg, final Executor executor) {
//        PcapHandler<PacketListener.Map<T>> handler = new PcapHandler<Map<T>>() {
//            @Override
//            public void nextPacket(final Map<T> user, final PcapPktHdr h, final ByteBuffer bytes) {
//                executor.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        java.util.Map<Class, Packet> packets = parseMap(pcap.getDataLinkType(), ByteUtils.toByteArray(bytes));
//                        user.nextPacket(arg, h, packets);
//                    }
//                });
//            }
//        };
//        return Jxnet.PcapLoop(pcap, count, handler, packetMap);
//    }
//
//    /*public static <T, V extends Packet> int loop(final Pcap pcap, final int count, final PacketProcessor<T, V> handler, final T arg, final Executor executor) {
//        PcapHandler<PacketProcessor<T, V>> callback = new PcapHandler<PacketProcessor<T, V>>() {
//            @Override
//            public void nextPacket(final PacketProcessor<T, V> user, final PcapPktHdr h, final ByteBuffer bytes) {
//                executor.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        user.initialize(arg, pcap, h);
//                        user.decode(ByteUtils.toByteArray(bytes));
//                    }
//                });
//            }
//        };
//        return Jxnet.PcapLoop(pcap, count, callback, handler);
//    }*/
//
//}
