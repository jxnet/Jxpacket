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

        String payload = getPayload("/Users/macintosh/Downloads/1022473014.201810150308.txt");
//        String payload = "{4:" +
//                ":20:20181010031555" +
//                ":25:1000092017" +
//                ":28C:00283" +
//                ":60F:C181010IDR421934282106" +
//                ":61:181010D5357968NTRF" +
//                ":86:PB CH 1218076234 AP/PLN IMPREST U/SPPD" +
//                ":61:181010D235768000000NTRF" +
//                ":86:PB CH 1218075706 AP/PLN IMPREST U/PENY" +
//                ":61:181010D2690235291NTRF" +
//                ":86:PB CH 1218076233 AP/PLN IMPREST U/LAY" +
//                ":62F:C181010IDR183470688847" +
//                "-}";

        Bootstrap.Builder.builder()
                .buffer(payload)
                .standart(Standart.SWIFT_2)
                .build().getData().forEach(System.out::println);

//        List<StatementLine> statementLines = bootstrap.getField(StatementLine.class);
//        statementLines.forEach(System.out::println);
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
