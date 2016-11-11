/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.utils;

import java.util.Collection;

public class MessageUtils {

    public static String getSeparatedString(Collection<String> el, String divider) {
        StringBuilder builder = new StringBuilder();
        for(String e : el) {
            if(builder.length() != 0) {
                builder.append(divider);
            }
            builder.append(e);

        }
        return builder.toString();
    }
}
