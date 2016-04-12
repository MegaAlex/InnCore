/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.command;

public class CmdResultData {

    private CmdResult result;
    private String message;

    public CmdResultData(CmdResult result, String message) {
        this.result = result;
        this.message = message;
    }

    public CmdResult getResult() {
        return result;
    }

    public void setResult(CmdResult result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
