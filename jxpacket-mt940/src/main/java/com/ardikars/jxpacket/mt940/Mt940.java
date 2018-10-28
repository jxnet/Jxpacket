package com.ardikars.jxpacket.mt940;

import com.ardikars.common.util.Validate;
import com.ardikars.jxpacket.mt940.domain.Standard;
import com.ardikars.jxpacket.mt940.util.Mt940Utils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jxpacket 2018/10/16
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
@Getter
public class Mt940 {

    private final List<Field> data;

    private Mt940(List<Field> data) {
        this.data = data;
    }

    public <T extends Field> List<T> getField(Class<T> clazz) {
        return (List<T>) data.stream().filter(field -> clazz.isAssignableFrom(field.getClass()))
                .collect(Collectors.toList());
    }

    public static class Builder {

        @lombok.Builder
        private static Mt940 newInstance(String buffer, Standard standard) {
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
            switch (standard) {
                case SWIFT_1:
                    for (String str : strings) {
                        if (str.startsWith("{1:")) {
                            data.add(Bank.Builder.builder()
                                    .bank(str.substring(6).replace("}", "").trim())
                                    .build()
                            );
                        } else if (str.startsWith("{2:")) {
                            data.add(Corp.Builder.builder()
                                    .corp(str.substring(7).replace("}", "").trim())
                                    .build()
                            );
                        } else if (str.startsWith("{4:")) {
                            Mt940Utils.parseFields(com.ardikars.jxpacket.mt940.swift.standard1.TransactionReferenceNumber.TAG, str).forEach(transactionReference -> {
                                data.add(com.ardikars.jxpacket.mt940.swift.standard1.TransactionReferenceNumber.Builder.builder()
                                        .statementDate(transactionReference.substring(0, 6))
                                        .referenceNumber(transactionReference.substring(6))
                                        .build()
                                );
                            });
                            Mt940Utils.parseFields(com.ardikars.jxpacket.mt940.swift.standard1.AccountIdentification.TAG, str).forEach(accountIdentification -> {
                                data.add(com.ardikars.jxpacket.mt940.swift.standard1.AccountIdentification.Builder.builder()
                                        .accountIdentification(accountIdentification)
                                        .build()
                                );
                            });
                            Mt940Utils.parseFields(com.ardikars.jxpacket.mt940.swift.standard1.StatementNumber.TAG, str).forEach(statementNumber -> {
                                data.add(com.ardikars.jxpacket.mt940.swift.standard1.StatementNumber.Builder.builder()
                                        .statementNumber(statementNumber)
                                        .build()
                                );
                            });
                            Mt940Utils.parseFields(com.ardikars.jxpacket.mt940.swift.standard1.OpeningBalance.TAG, str).forEach(openingBalance -> {
                                data.add(com.ardikars.jxpacket.mt940.swift.standard1.OpeningBalance.Builder.builder()
                                        .creditOrDebitMark(openingBalance.substring(0, 1))
                                        .statementDate(openingBalance.substring(1, 7))
                                        .currency(openingBalance.substring(7, 10))
                                        .amount(openingBalance.substring(10, openingBalance.length() - 1))
                                        .build()
                                );
                            });
                            Mt940Utils.parseFields(com.ardikars.jxpacket.mt940.swift.standard1.StatementLine.TAG, str).forEach(statementLine -> {
                                int li = 0;
                                char[] amount = statementLine.substring(7).toCharArray();
                                for (int i = 0; i < amount.length; i++) {
                                    if (!Character.isDigit(amount[i]) && amount[i] != ',') {
                                        li = i;
                                        break;
                                    }
                                }
                                li += 7;
                                data.add(com.ardikars.jxpacket.mt940.swift.standard1.StatementLine.Builder.builder()
                                        .valueDate(statementLine.substring(0, 6))
                                        .creditOrDebit(statementLine.substring(6, 7))
                                        .amount(statementLine.substring(7, li))
                                        .transactionCode(statementLine.substring(li, li + 4))
                                        .transactionNumber(statementLine.substring(li + 4))
                                        .bankReference(statementLine.substring(li + 4))
                                        .supplementaryDetails(statementLine.substring(li + 4))
                                        .build()
                                );
                            });
                            Mt940Utils.parseFields(com.ardikars.jxpacket.mt940.swift.standard1.InformationToAccountOwner.TAG, str).forEach(informationToAccountOwner -> {
                                data.add(com.ardikars.jxpacket.mt940.swift.standard1.InformationToAccountOwner.Builder.builder()
                                        .informationToAccountOwner(informationToAccountOwner)
                                        .build()
                                );
                            });
                            Mt940Utils.parseFields(com.ardikars.jxpacket.mt940.swift.standard1.ClosingBalance.TAG, str).forEach(closingBalance -> {
                                data.add(com.ardikars.jxpacket.mt940.swift.standard1.ClosingBalance.Builder.builder()
                                        .creditOrDebitMark(closingBalance.substring(0, 1))
                                        .statementDate(closingBalance.substring(1, 7))
                                        .currency(closingBalance.substring(7, 10))
                                        .amount(closingBalance.substring(10, closingBalance.length() - 1))
                                        .build()
                                );
                            });
                            Mt940Utils.parseFields(com.ardikars.jxpacket.mt940.swift.standard1.ClosingAvailableBalance.TAG, str).forEach(closingAvaliableBalance -> {
                                data.add(com.ardikars.jxpacket.mt940.swift.standard1.ClosingAvailableBalance.Builder.builder()
                                        .creditOrDebitMark(closingAvaliableBalance.substring(0, 1))
                                        .statementDate(closingAvaliableBalance.substring(1, 6))
                                        .currency(closingAvaliableBalance.substring(7, 9))
                                        .amount(closingAvaliableBalance.substring(9, closingAvaliableBalance.length() - 1))
                                        .build()
                                );
                            });
                        }
                    }
                    break;
                case SWIFT_2:
                    for (String str : strings) {
                        if (str.startsWith("{1:")) {
                            data.add(Bank.Builder.builder()
                                    .bank(str.substring(6).replace("}", "").trim())
                                    .build()
                            );
                        } else if (str.startsWith("{2:")) {
                            data.add(Corp.Builder.builder()
                                    .corp(str.substring(7).replace("}", "").trim())
                                    .build()
                            );
                        } else if (str.startsWith("{4:")) {
                            Mt940Utils.parseFields(com.ardikars.jxpacket.mt940.swift.standard2.TransactionReferenceNumber.TAG, str).forEach(transactionReference -> {
                                data.add(com.ardikars.jxpacket.mt940.swift.standard2.TransactionReferenceNumber.Builder.builder()
                                        .statementDate(transactionReference.substring(0, 6))
                                        .referenceNumber(transactionReference.substring(8, transactionReference.length() - 1))
                                        .build()
                                );
                            });
                            Mt940Utils.parseFields(com.ardikars.jxpacket.mt940.swift.standard2.AccountIdentification.TAG, str).forEach(accountIdentification -> {
                                data.add(com.ardikars.jxpacket.mt940.swift.standard2.AccountIdentification.Builder.builder()
                                        .accountIdentification(accountIdentification)
                                        .build()
                                );
                            });
                            Mt940Utils.parseFields(com.ardikars.jxpacket.mt940.swift.standard2.StatementNumber.TAG, str).forEach(statementNumber -> {
                                data.add(com.ardikars.jxpacket.mt940.swift.standard2.StatementNumber.Builder.builder()
                                        .statementNumber(statementNumber)
                                        .build()
                                );
                            });
                            Mt940Utils.parseFields(com.ardikars.jxpacket.mt940.swift.standard2.OpeningBalance.TAG, str).forEach(openingBalance -> {
                                data.add(com.ardikars.jxpacket.mt940.swift.standard2.OpeningBalance.Builder.builder()
                                        .creditOrDebitMark(openingBalance.substring(0, 1))
                                        .statementDate(openingBalance.substring(1, 7))
                                        .currency(openingBalance.substring(7, 10))
                                        .amount(openingBalance.substring(10, openingBalance.length() - 1))
                                        .build()
                                );
                            });
                            Mt940Utils.parseFields(com.ardikars.jxpacket.mt940.swift.standard2.StatementLine.TAG, str).forEach(statementLine -> {
                                int li = 0;
                                char[] amount = statementLine.substring(7).toCharArray();
                                for (int i = 0; i < amount.length; i++) {
                                    if (!Character.isDigit(amount[i]) && amount[i] != ',') {
                                        li = i;
                                        break;
                                    }
                                }
                                li += 7;
                                data.add(com.ardikars.jxpacket.mt940.swift.standard2.StatementLine.Builder.builder()
                                        .valueDate(statementLine.substring(0, 6))
                                        .transactionDate(statementLine.substring(6, 10))
                                        .creditOrDebit(statementLine.substring(10, 11))
                                        .amount(statementLine.substring(11, li))
                                        .transactionNumber("0")
                                        .build()
                                );
                            });
                            Mt940Utils.parseFields(com.ardikars.jxpacket.mt940.swift.standard2.InformationToAccountOwner.TAG, str).forEach(informationToAccountOwner -> {
                                data.add(com.ardikars.jxpacket.mt940.swift.standard2.InformationToAccountOwner.Builder.builder()
                                        .informationToAccountOwner(informationToAccountOwner)
                                        .build()
                                );
                            });
                            Mt940Utils.parseFields(com.ardikars.jxpacket.mt940.swift.standard2.ClosingBalance.TAG, str).forEach(closingBalance -> {
                                data.add(com.ardikars.jxpacket.mt940.swift.standard2.ClosingBalance.Builder.builder()
                                        .creditOrDebitMark(closingBalance.substring(0, 1))
                                        .statementDate(closingBalance.substring(1, 7))
                                        .currency(closingBalance.substring(7, 10))
                                        .amount(closingBalance.substring(10, closingBalance.length() - 1))
                                        .build()
                                );
                            });
                            Mt940Utils.parseFields(com.ardikars.jxpacket.mt940.swift.standard2.ClosingAvailableBalance.TAG, str).forEach(closingAvaliableBalance -> {
                                data.add(com.ardikars.jxpacket.mt940.swift.standard2.ClosingAvailableBalance.Builder.builder()
                                        .creditOrDebitMark(closingAvaliableBalance.substring(0, 1))
                                        .statementDate(closingAvaliableBalance.substring(1, 6))
                                        .currency(closingAvaliableBalance.substring(7, 9))
                                        .amount(closingAvaliableBalance.substring(9, closingAvaliableBalance.length() - 1))
                                        .build()
                                );
                            });
                        }
                    }
                    break;
            }

            return new Mt940(data);
        }

    }

}
