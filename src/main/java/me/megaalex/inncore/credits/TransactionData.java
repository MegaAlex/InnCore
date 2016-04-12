/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.credits;

import java.math.BigDecimal;

public class TransactionData {

    private final String sender;
    private final String receiver;
    private final BigDecimal amount;
    private final long time;
    private final int type;

    public TransactionData(String sender, String receiver, BigDecimal amount, long time, int type) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.time = time;
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public long getTime() {
        return time;
    }

    public int getType() {
        return type;
    }
}
