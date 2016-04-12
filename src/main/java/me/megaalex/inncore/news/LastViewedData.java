/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.news;

public class LastViewedData {

    private int bookId;
    private long time;

    public LastViewedData(int bookId, long time) {
        this.bookId = bookId;
        this.time = time;
    }

    public LastViewedData(int bookId) {
        this.bookId = bookId;
        this.time = System.currentTimeMillis() / 1000L;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
