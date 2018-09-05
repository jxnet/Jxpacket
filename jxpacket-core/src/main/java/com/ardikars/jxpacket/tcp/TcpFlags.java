package com.ardikars.jxpacket.tcp;

public final class TcpFlags {

    private boolean ns;
    private boolean cwr;
    private boolean ece;
    private boolean urg;
    private boolean ack;
    private boolean psh;
    private boolean rst;
    private boolean syn;
    private boolean fin;

    private TcpFlags(final Builder builder) {
        this.ns = builder.ns;
        this.cwr = builder.cwr;
        this.ece = builder.ece;
        this.urg = builder.urg;
        this.ack = builder.ack;
        this.psh = builder.psh;
        this.rst = builder.rst;
        this.syn = builder.syn;
        this.fin = builder.fin;
    }

    public short getShortValue() {
        short flags = 0;
        if (this.ns) flags += 256;
        if (this.cwr) flags += 128;
        if (this.ece) flags += 64;
        if (this.urg) flags += 32;
        if (this.ack) flags += 16;
        if (this.psh) flags += 8;
        if (this.rst) flags += 4;
        if (this.syn) flags += 2;
        if (this.fin) flags += 1;
        return flags;
    }

    public static final class Builder implements com.ardikars.common.util.Builder<TcpFlags, Short> {

        private boolean ns;
        private boolean cwr;
        private boolean ece;
        private boolean urg;
        private boolean ack;
        private boolean psh;
        private boolean rst;
        private boolean syn;
        private boolean fin;

        public Builder ns(boolean ns) {
            this.ns = ns;
            return this;
        }

        public Builder cwr(boolean cwr) {
            this.cwr = cwr;
            return this;
        }

        public Builder ece(boolean ece) {
            this.ece = ece;
            return this;
        }

        public Builder urg(boolean urg) {
            this.urg = urg;
            return this;
        }

        public Builder ack(boolean ack) {
            this.ack = ack;
            return this;
        }

        public Builder psh(boolean psh) {
            this.psh = psh;
            return this;
        }

        public Builder rst(boolean rst) {
            this.rst = rst;
            return this;
        }

        public Builder syn(boolean syn) {
            this.syn = syn;
            return this;
        }

        public Builder fin(boolean fin) {
            this.fin = false;
            return this;
        }

        @Override
        public TcpFlags build() {
            return new TcpFlags(this);
        }

        @Override
        public TcpFlags build(Short flags) {
            this.fin = ((flags >> 0) & 0x1) == 1;
            this.syn = ((flags >> 1) & 0x1) == 1;
            this.rst = ((flags >> 2) & 0x1) == 1;
            this.psh = ((flags >> 3) & 0x1) == 1;
            this.ack = ((flags >> 4) & 0x1) == 1;
            this.urg = ((flags >> 5) & 0x1) == 1;
            this.ece = ((flags >> 6) & 0x1) == 1;
            this.cwr = ((flags >> 7) & 0x1) == 1;
            this.ns = ((flags >> 8) & 0x1) == 1;
            return new TcpFlags(this);
        }

    }

}
