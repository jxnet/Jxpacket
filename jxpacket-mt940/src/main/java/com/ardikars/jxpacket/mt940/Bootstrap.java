package com.ardikars.jxpacket.mt940;

import com.ardikars.common.util.Validate;
import com.ardikars.jxpacket.mt940.swift.Field;
import com.ardikars.jxpacket.mt940.swift.standard1.*;
import com.ardikars.jxpacket.mt940.util.Mt940Utils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author jxpacket 2018/10/16
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Getter
public class Bootstrap {

    private final Stream<Field> data;

    private Bootstrap(Collection<Field> data) {
        this.data = data.stream();
    }

    public <T> Optional<T> getField(Class<T> clazz) {
       return (Optional<T>) data.filter(field -> clazz.isAssignableFrom(field.getClass()))
                .findFirst();
    }

    public static class Builder {

        @lombok.Builder
        private static Bootstrap newInstance(String buffer) {
            Validate.notIllegalArgument(buffer != null || !buffer.isEmpty(),
                    new IllegalArgumentException("Buffer should be not null or empty."));
            List<Field> data = new ArrayList<>();
            char[] characters = buffer.toCharArray();
            List<String> strings = new ArrayList<>();
            int fb = 0;
            int lb;
            for (int index = 0; index < characters.length; index++) {
                if (characters[index] == '{') {
                    fb = index;
                }
                if (characters[index] == '}') {
                    lb = index;
                    strings.add(buffer.substring(fb, lb + 1));
                }
            }
            for (String str : strings) {
                if (str.startsWith("{1:")) {

                } else if (str.startsWith("{2:")) {

                } else if (str.startsWith("{4:")) {
                    String transactionReference = Mt940Utils.parseField(TransactionReferenceNumber.TAG, str);
                    if (transactionReference != null) {
                        data.add(TransactionReferenceNumber.Builder.builder()
                                .statementDate(transactionReference.substring(0, 6))
                                .referenceNumber(transactionReference.substring(8, transactionReference.length() - 1))
                                .build()
                        );
                    }
                    String accountIdentification = Mt940Utils.parseField(AccountIdentification.TAG, str);
                    if (accountIdentification != null) {
                        data.add(AccountIdentification.Builder.builder()
                                .accountIdentification(accountIdentification)
                                .build()
                        );
                    }
                    String statementNumber = Mt940Utils.parseField(StatementNumber.TAG, str);
                    if (statementNumber != null) {
                        data.add(StatementNumber.Builder.builder()
                                .statementNumber(statementNumber)
                                .build()
                        );
                    }
                    String openingBalance = Mt940Utils.parseField(OpeningBalance.TAG, str);
                    if (openingBalance != null) {
                        data.add(OpeningBalance.Builder.builder()
                                .creditOrDebitMark(openingBalance.substring(0, 1))
                                .statementDate(openingBalance.substring(1, 7))
                                .currency(openingBalance.substring(7, 10))
                                .amount(openingBalance.substring(10, openingBalance.length() - 1))
                                .build()
                        );
                    }

                    // statement line
                    String informationToAccountOwner = Mt940Utils.parseField(InformationToAccountOwner.TAG, str);
                    if (informationToAccountOwner != null) {
                        data.add(InformationToAccountOwner.Builder.builder()
                                .informationToAccountOwner(informationToAccountOwner)
                                .build()
                        );
                    }
                    String closingBalance = Mt940Utils.parseField(ClosingBalance.TAG, str);
                    if (closingBalance != null) {
                        data.add(ClosingAvailableBalance.Builder.builder()
                                .creditOrDebitMark(closingBalance.substring(0, 1))
                                .statementDate(closingBalance.substring(1, 7))
                                .currency(closingBalance.substring(7, 10))
                                .amount(closingBalance.substring(10, closingBalance.length() - 1))
                                .build()
                        );
                    }
                    String closingAvaliableBalance = Mt940Utils.parseField(ClosingAvailableBalance.TAG, str);
                    if (closingAvaliableBalance != null) {
                        data.add(ClosingAvailableBalance.Builder.builder()
                                .creditOrDebitMark(closingAvaliableBalance.substring(0, 1))
                                .statementDate(closingAvaliableBalance.substring(1, 6))
                                .currency(closingAvaliableBalance.substring(7, 9))
                                .amount(closingAvaliableBalance.substring(9, closingAvaliableBalance.length() - 1))
                                .build()
                        );
                    }
                }
            }
            return new Bootstrap(data);
        }

    }

}
