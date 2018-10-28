package com.ardikars.jxpacket.mt940.util;

import com.ardikars.common.util.Validate;
import com.ardikars.common.util.ValidateNumber;
import com.ardikars.jxpacket.mt940.Bank;
import com.ardikars.jxpacket.mt940.domain.CreditOrDebit;
import com.ardikars.jxpacket.mt940.domain.Currency;
import com.ardikars.jxpacket.mt940.domain.TransactionCode;
import com.ardikars.jxpacket.mt940.swift.standard1.ClosingBalance;
import com.ardikars.jxpacket.mt940.swift.standard1.OpeningBalance;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author jxpacket 2018/10/16
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
public class Mt940Utils {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private static final DateFormat DATE_FORMAT_TX = new SimpleDateFormat("MMdd");
    private static final DateFormat DATE_FORMAT_DB = new SimpleDateFormat("dd/MM/yyyy");

    public static Date parseStatementDate(String statementDate) throws IllegalArgumentException {
        Validate.notIllegalArgument(statementDate != null, new IllegalArgumentException("Statement date should be not null."));
        Validate.notIllegalArgument(!statementDate.isEmpty(), new IllegalArgumentException("Statement date should be not empty."));
        ValidateNumber.notNumeric(statementDate, new IllegalArgumentException("Statement date can't containts non numeric character."));
        Validate.notIllegalArgument(statementDate.length() == 6, new IllegalArgumentException("Invalid statement date."));
        try {
            return DATE_FORMAT.parse("20" + statementDate);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static Date parseTransactionDate(String transactionDate) {
        Validate.notIllegalArgument(transactionDate != null, new IllegalArgumentException("Transaction date should be not null."));
        Validate.notIllegalArgument(!transactionDate.isEmpty(), new IllegalArgumentException("Transaction date should be not empty."));
        ValidateNumber.notNumeric(transactionDate, new IllegalArgumentException("Transaction date can't containts non numeric character."));
        Validate.notIllegalArgument(transactionDate.length() == 4, new IllegalArgumentException("Invalid statement date."));
        try {
            return DATE_FORMAT_TX.parse( transactionDate);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static String parseAccountIdentification(String accountIdentification) throws IllegalArgumentException {
        Validate.notIllegalArgument(accountIdentification != null,
                new IllegalArgumentException("Account identification should be not null."));
        Validate.notIllegalArgument(!accountIdentification.isEmpty(),
                new IllegalArgumentException("Account identification should be not empty."));
        Validate.notIllegalArgument(accountIdentification.length() <= 35,
                new IllegalArgumentException("Length of Account identification should be less then or equal to 35 characters."));
        return accountIdentification;
    }

    public static CreditOrDebit parseCreditOrDebit(String creditOrDebitMark) {
        Validate.notIllegalArgument(creditOrDebitMark != null || !creditOrDebitMark.isEmpty(), new IllegalArgumentException("Credit or debit mark should be not null or empty."));
        CreditOrDebit parsedCreditOrDebit = CreditOrDebit.parse(creditOrDebitMark);
        if (parsedCreditOrDebit == null) {
            throw new IllegalArgumentException("Invalid credit or debit mark.");
        }
        return parsedCreditOrDebit;
    }

    public static Currency parseCurrency(String currency) throws IllegalArgumentException {
        Validate.notIllegalArgument(currency != null || !currency.isEmpty(), new IllegalArgumentException("Currency should be not null or empty."));
        Currency curr = Currency.valueOf(currency);
        if (curr == null || curr == Currency.UNKNOWN) {
            throw new IllegalArgumentException("Invalid currency.");
        }
        return curr;
    }

    public static TransactionCode parseTransactionCode(String transactionCode) throws IllegalArgumentException {
        Validate.notIllegalArgument(transactionCode != null,
                new IllegalArgumentException("Transaction code should be not null."));
        Validate.notIllegalArgument(!transactionCode.isEmpty(),
                new IllegalArgumentException("Transaction code should be not empty."));
        TransactionCode parsedTransactionCode = TransactionCode.valueOf(transactionCode);
        if (parsedTransactionCode == null || parsedTransactionCode == TransactionCode.UNKNOWN) {
            throw new IllegalArgumentException("Invalid transaction code.");
        }
        return parsedTransactionCode;
    }

    public static BigDecimal parseAmount(String amount) {
        Validate.notIllegalArgument(amount != null, new IllegalArgumentException("Amount should be not null."));
        ValidateNumber.notNumericWithCommaSeparator(amount);
        amount = amount.replace(",", ".");
        try {
            BigDecimal value = new BigDecimal(amount);
            ValidateNumber.notGreaterThenOrEqualZero(value, new IllegalArgumentException("Amount should be greater then or equal to zero."));
            return value;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static BigInteger parseStatementNumber(String statementNumber) {
        Validate.notIllegalArgument(statementNumber != null,
                new IllegalArgumentException("Statement number should be not null."));
        Validate.notIllegalArgument(statementNumber.length() == 5,
                new IllegalArgumentException("Statement number length should be 5."));
        ValidateNumber.notNumeric(statementNumber, new IllegalArgumentException("Statement number can't contains non numeric characters."));
        try {
            BigInteger value = new BigInteger(statementNumber);
            ValidateNumber.notGreaterThenOrEqualZero(value, new IllegalArgumentException("Statement number should be greater then or equal to zero."));
            return value;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static BigInteger parseTransactionNumber(String transactionNumber) {
        Validate.notIllegalArgument(transactionNumber != null,
                new IllegalArgumentException("Transaction number should be not null."));
        Validate.notIllegalArgument(transactionNumber.length() <= 16,
                new IllegalArgumentException("Transaction number length should be less then or equal to 16."));
        ValidateNumber.notNumeric(transactionNumber, new IllegalArgumentException("Transaction number can't contains non numeric characters."));
        try {
            BigInteger value = new BigInteger(transactionNumber);
            ValidateNumber.notGreaterThenOrEqualZero(value, new IllegalArgumentException("Transaction number should be greater then or equal to zero."));
            return value;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static BigInteger parseBankReference(String bankReference) {
        Validate.notIllegalArgument(bankReference != null,
                new IllegalArgumentException("Bank reference should be not null."));
        Validate.notIllegalArgument(bankReference.length() <= 16,
                new IllegalArgumentException("Bank reference length should be less then or equal to 16."));
        ValidateNumber.notNumeric(bankReference, new IllegalArgumentException("Bank reference can't contains non numeric characters."));
        try {
            BigInteger value = new BigInteger(bankReference);
            ValidateNumber.notGreaterThenOrEqualZero(value, new IllegalArgumentException("Bank reference should be greater then or equal to zero."));
            return value;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static BigInteger parseReferenceNumber(String referenceNumber) {
        Validate.notIllegalArgument(referenceNumber != null,
                new IllegalArgumentException("Reference number should be not null."));
//        Validate.notIllegalArgument(referenceNumber.length() == 10,
//                new IllegalArgumentException("Reference number length should be 10."));
        ValidateNumber.notNumeric(referenceNumber, new IllegalArgumentException("Reference number can't contains non numeric characters."));
        try {
            BigInteger value = new BigInteger(referenceNumber);
            ValidateNumber.notGreaterThenOrEqualZero(value, new IllegalArgumentException("Reference number should be greater then or equal to zero."));
            return value;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static List<String> parseInformationToAccountOwner(String informationToAccountOwner) {
        Validate.notIllegalArgument(informationToAccountOwner != null,
                new IllegalArgumentException("Information to account owner should be not null."));
        Validate.notIllegalArgument(!informationToAccountOwner.isEmpty(),
                new IllegalArgumentException("Information to account owner should be not empty."));
        List<String> list = Arrays.asList(informationToAccountOwner.split("\n"));
        Validate.notIllegalArgument(list.size() <= 6,
                new IllegalArgumentException("Information to account may contain up to 6 rows of data."));
        return list;
    }

    public static List<String> parseSupplementaryDetails(String supplementaryDetails) {
        Validate.notIllegalArgument(supplementaryDetails != null,
                new IllegalArgumentException("Supplementary details should be not null."));
        Validate.notIllegalArgument(!supplementaryDetails.isEmpty(),
                new IllegalArgumentException("Supplementary details should be not empty."));
        List<String> list = Arrays.asList(supplementaryDetails.split("\n"));
        return list;
    }

    private static String parseField(String tag, String string) throws IllegalArgumentException {
        if (string.contains(tag)) {
            int tagLength = tag.length() + 1;
            char[] chars = string.toCharArray();
            int fi = string.indexOf(tag) + tagLength;
            if (tag.equals(OpeningBalance.TAG) || tag.equals(ClosingBalance.TAG)) {
                String subTag = string.substring(fi - tagLength, fi);
                if (subTag.equals(OpeningBalance.TAG + "F")
                        || subTag.equals(OpeningBalance.TAG + "M")
                        || subTag.equals(ClosingBalance.TAG + "F")
                        || subTag.equals(ClosingBalance.TAG + "M")) {
                    fi += 1;
                }
            }
            int li = 0;
            for (int index = fi; index < chars.length; index++) {
                if (chars[index + 1] == ':') {
                    li = index + 1;
                    break;
                }
                if (chars[index + 1] == '}') {
                    li = index + 1;
                    break;
                }
            }
            return string.substring(fi, li);
        } else {
            return null;
        }
    }

    public static List<String> parseFields(String tag, String string) {
        List<String> list = new ArrayList<>();
        String[] strings = string.split(tag);
        for (int i = 1; i < strings.length; i++) {
            if (strings[i] == null || strings[i].isEmpty()) {
                continue;
            }
            String parsed = parseField(tag, tag + "" + strings[i]);
            parsed = parsed.replaceAll("\n", "");
            if (parsed != null) {
                list.add(parsed);
            }
        }
        return list;
    }

    public static String parseDateToString(Date date) {
        return DATE_FORMAT_DB.format(date);
    }

}
