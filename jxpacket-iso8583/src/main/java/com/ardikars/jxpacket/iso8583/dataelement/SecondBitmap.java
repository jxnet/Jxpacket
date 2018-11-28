package com.ardikars.jxpacket.iso8583.dataelement;

public class SecondBitmap implements DataElement<Boolean> {

    private final boolean secondBitmap;

    public SecondBitmap(Builder builder) {
        this.secondBitmap = builder.secondBitmap;
    }

    @Override
    public int bit() {
        return 1;
    }

    @Override
    public int length() {
        return 1;
    }

    @Override
    public Boolean field() {
        return secondBitmap;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractBuilder<SecondBitmap> {

        private boolean secondBitmap;

        public Builder secondBitmap(boolean secondBitmap) {
            this.secondBitmap = secondBitmap;
            return this;
        }

        @Override
        public SecondBitmap build() {
            return new SecondBitmap(this);
        }

    }

    @Override
    public String toString() {
        return new StringBuilder("SecondBitmap{")
                .append("secondBitmap=").append(secondBitmap)
                .append('}').toString();
    }

}