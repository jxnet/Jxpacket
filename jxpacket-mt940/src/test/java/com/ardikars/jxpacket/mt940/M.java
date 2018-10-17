package com.ardikars.jxpacket.mt940;

import com.ardikars.jxpacket.mt940.domain.Standart;
import com.ardikars.jxpacket.mt940.swift.standard2.StatementLine;

import java.io.*;
import java.util.List;

/**
 * @author jxpacket 2018/10/16
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
public class M {

    public static void main(String[] args) {

        String payload = getPayload("/Users/macintosh/Downloads/BUKOPIN.txt");

        Bootstrap bootstrap = Bootstrap.Builder.builder()
                .buffer(payload)
                .standart(Standart.SWIFT_2)
                .build();

        List<StatementLine> statementLines = bootstrap.getField(StatementLine.class);
        statementLines.forEach(System.out::println);
    }

    public static String getPayload(String path) {
        File file = new File(path);
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null) {
                sb.append(st);
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

}
