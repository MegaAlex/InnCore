/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.utils;

import java.util.ArrayList;

public class CmdUtils {

    public static String[] mergeArgs(String[] args) {
        ArrayList<String> merged = new ArrayList<>();
        boolean open = false;
        StringBuilder builder = new StringBuilder();
        for(String arg : args) {
            if(open) {
                if(builder.length() != 0) {
                    builder.append(' ');
                }
                if(arg.endsWith("\"")) {
                    open = false;
                    builder.append(arg.substring(0, arg.length() - 1));
                    merged.add(builder.toString());
                    builder = new StringBuilder();
                } else {
                    builder.append(arg);
                }
            } else if(arg.startsWith("\"")) {
                open = true;
                builder.append(arg.substring(1));
            } else  {
                merged.add(arg);
            }
        }
        if(builder.length() != 0) {
            merged.add(builder.toString());
        }
        return merged.toArray(new String[merged.size()]);
    }

    public static String mergeArgsSimple(String[] args) {
        StringBuilder builder = new StringBuilder();
        for(String arg : args) {
            if(builder.length() != 0) {
                builder.append(' ');
            }
            builder.append(arg);
        }
        return builder.toString();
    }
}
