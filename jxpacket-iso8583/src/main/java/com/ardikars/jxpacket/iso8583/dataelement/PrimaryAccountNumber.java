package com.ardikars.jxpacket.iso8583.dataelement;

public class PrimaryAccountNumber implements DataElement<String> {

    private final String primaryAccountNumber;

    public PrimaryAccountNumber(Builder builder) {
        this.primaryAccountNumber = builder.primaryAccountNumber;
    }

    @Override
    public int bit() {
        return 2;
    }

    @Override
    public int length() {
        return primaryAccountNumber.length();
    }

    @Override
    public String field() {
        return primaryAccountNumber;
    }

    public static class Builder extends AbstractBuilder<PrimaryAccountNumber> {

        private String primaryAccountNumber;

        public Builder primaryAccountNumber(String primaryAccountNumber) {
            this.primaryAccountNumber = primaryAccountNumber;
            return this;
        }

        @Override
        public PrimaryAccountNumber build() {
            if (primaryAccountNumber == null
                    || primaryAccountNumber.isEmpty()
                    || primaryAccountNumber.length() > 19) {
                throw new IllegalArgumentException(
                        "Primary account number should be not null, empty, and more then 19 characters.");
            }
            return new PrimaryAccountNumber(this);
        }

    }

}
