package com.ardikars.jxpacket.mt940;

import com.ardikars.jxpacket.mt940.swift.standard2.StatementLine;
import com.ardikars.jxpacket.mt940.util.Mt940Utils;

/**
 * @author jxpacket 2018/10/17
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
public class N {

    public static void main(String[] args) {
        Mt940Utils.parseFields(StatementLine.TAG, "{1:F05BANK BUKOPIN}{2:I940PLN}{4::20:181010031555:25:1000092017:28C:00283:60F:C181010IDR421934282106:61:181010D5357968NTRF:86:PB CH 1218076234 AP/PLN IMPREST U/SPPD:61:181010D235768000000NTRF:86:PB CH 1218075706 AP/PLN IMPREST U/PENY:61:181010D2690235291NTRF:86:PB CH 1218076233 AP/PLN IMPREST U/LAY:62F:C181010IDR183470688847-}")
                .forEach(System.out::println);
    }
}
