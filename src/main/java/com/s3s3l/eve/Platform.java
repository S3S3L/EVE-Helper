/** 
 * Project Name:eve-helper 
 * File Name:Platform.java 
 * Package Name:com.s3s3l.eve 
 * Date:May 11, 20176:18:20 PM 
 * Copyright (c) 2017, kehewei@hellobike.com All Rights Reserved. 
 * 
*/

package com.s3s3l.eve;

/**
 * <p>
 * </p>
 * ClassName:Platform <br>
 * Date: May 11, 2017 6:18:20 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public enum Platform {
    Any("any"),
    Linux("Linux"),
    Mac_OS("Mac OS"),
    Mac_OS_X("Mac OS X"),
    Windows("Windows"),
    OS2("OS/2"),
    Solaris("Solaris"),
    SunOS("SunOS"),
    MPEiX("MPE/iX"),
    HP_UX("HP-UX"),
    AIX("AIX"),
    OS390("OS/390"),
    FreeBSD("FreeBSD"),
    Irix("Irix"),
    Digital_Unix("Digital Unix"),
    NetWare_411("NetWare"),
    OSF1("OSF1"),
    OpenVMS("OpenVMS"),
    Others("Others");

    private String info;

    private Platform(String info) {
        this.info = info;
    }

    public String info() {
        return this.info;
    }

    public static Platform parse(String info) {
        Platform[] enums = values();

        for (Platform currentEnum : enums) {
            if (currentEnum.info.equals(info)) {
                return currentEnum;
            }
        }
        throw new IllegalArgumentException(String.format("No matching constant for [%s]", info));
    }
}
