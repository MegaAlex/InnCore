/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.news;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class BookData {

    private Integer id = null;
    private String author;
    private String title;
    private List<String> pages;
    private Long time;
    private Boolean deleted = false;

    public BookData(Integer id, BookData data) {
        this.id = id;
        this.author = data.author;
        this.title = data.title;
        this.pages = data.pages;
        this.time = data.time;
        this.deleted = data.deleted;
    }

    public BookData(Integer id, String author, String title, List<String> pages, Long time, Boolean deleted) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.pages = pages;
        this.time = time;
        this.deleted = deleted;
    }

    public BookData(String author, String title, List<String> pages, Long time) {
        this.author = author;
        this.title = title;
        this.pages = pages;
        this.time = time;
    }

    public BookData(Integer id, String author, String title, Long time) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.time = time;
    }

    public BookData(String author, String title, Long time, Boolean deleted) {
        this.author = author;
        this.title = title;
        this.time = time;
        this.deleted = deleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getPages() {
        if(pages == null) {
            pages = new ArrayList<>();
        }
        return pages;
    }

    public void setPages(List<String> pages) {
        this.pages = pages;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void isDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public ItemStack toItemStack() {
        ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK, 1);
        BookMeta meta = (BookMeta) itemStack.getItemMeta();
        meta.setAuthor(author);
        meta.setTitle(title);
        meta.setPages(pages);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
