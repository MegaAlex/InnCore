/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.news;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import me.megaalex.inncore.database.SqlConnection;
import me.megaalex.inncore.database.SqlModule;

public class NewsSqlModule extends SqlModule {

    @Override
    public void setupTables() {
        SqlConnection con = getConnection();
        String bookMeta = "CREATE TABLE IF NOT EXISTS `" + con.prefix + "news_book_meta` (\n" +
                "  `id` int(10) NOT NULL AUTO_INCREMENT,\n" +
                "  `author` varchar(64) NOT NULL,\n" +
                "  `title` varchar(256) NOT NULL,\n" +
                "  `deleted` tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  `time` int(13) NOT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;";
        String bookPages = "CREATE TABLE IF NOT EXISTS `" + con.prefix + "news_book_pages` (\n" +
                "  `id` int(10) NOT NULL AUTO_INCREMENT,\n" +
                "  `bookId` int(10) NOT NULL,\n" +
                "  `page` int(10) NOT NULL,\n" +
                "  `text` varchar(512) NOT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE KEY `bookId` (`bookId`,`page`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;";
        String playerHistory = "CREATE TABLE IF NOT EXISTS `" + con.prefix + "news_playerHistory` (\n" +
                "  `id` int(10) NOT NULL AUTO_INCREMENT,\n" +
                "  `username` varchar(32) NOT NULL,\n" +
                "  `lastViewed` int(10) NOT NULL,\n" +
                "  `time` int(13) NOT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE KEY `username` (`username`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;";
        Statement stmt = null;
        try {
            stmt = con.con.createStatement();
            stmt.addBatch(bookMeta);
            stmt.addBatch(bookPages);
            stmt.addBatch(playerHistory);
            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        closeConnection(con);
    }

    public int insertBook(BookData data) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        int bookId = 0;
        try {
            String query = "INSERT INTO `" + con.prefix + "news_book_meta`(`author`, `title`, `time`, `deleted`) VALUES(?, ?, ?, ?)";
            stmt = con.con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, data.getAuthor());
            stmt.setString(2, data.getTitle());
            stmt.setLong(3, data.getTime());
            stmt.setBoolean(4, data.isDeleted());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if(generatedKeys.next()) {
                bookId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        closeConnection(con);

        // Set Pages
        if(bookId != 0) {
            setPages(new BookData(bookId, data));
        }
        return bookId;
    }

    public void updateBook(BookData data) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        try {
            String query = "UPDATE `" + con.prefix + "news_book_meta` SET `author` = ?,  `title` = ?, `time` = ?, `deleted` = ? " +
                    "WHERE `id` = ?";
            stmt = con.con.prepareStatement(query);
            stmt.setString(1, data.getAuthor());
            stmt.setString(2, data.getTitle());
            stmt.setLong(3, data.getTime());
            stmt.setBoolean(4, data.isDeleted());
            stmt.setInt(5, data.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        closeConnection(con);
        setPages(data);
    }

    public boolean bookExists(int bookId) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        ResultSet result = null;
        boolean exists = false;
        try {
            String query = "SELECT `id` FROM `" + con.prefix + "news_book_meta` " +
                    "WHERE `id` = ?";
            stmt = con.con.prepareStatement(query);
            stmt.setInt(1, bookId);
            result = stmt.executeQuery();
            if(result.next()) {
                exists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        closeConnection(con);
        return exists;
    }

    public void setPages(BookData data) {
        PreparedStatement stmt = null;
        SqlConnection con = getConnection();
        try {
            con.con.setAutoCommit(false);
            String pageQuery = "INSERT INTO `" + con.prefix + "news_book_pages`(`bookId`, `page`, `text`) VALUES(?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE `text` = ?;";
            List<String> pages = data.getPages();
            stmt = con.con.prepareStatement(pageQuery);
            for(int i = 0; i < pages.size(); i++) {
                stmt.setInt(1, data.getId());
                stmt.setInt(2, i);
                stmt.setString(3, pages.get(i));
                stmt.setString(4, pages.get(i));
                stmt.executeUpdate();
                con.con.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        closeConnection(con);
    }

    public BookData getBook(int bookId) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        ResultSet result = null;
        BookData data = null;
        try {
            String query = "SELECT `author`, `time`, `title`, `deleted` FROM `" + con.prefix + "news_book_meta` " +
                    "WHERE `id` = ?";
            stmt = con.con.prepareStatement(query);
            stmt.setInt(1, bookId);
            result = stmt.executeQuery();
            if(result.next()) {
                data = getBookDataResultSet(result);
                data.setId(bookId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        closeConnection(con);

        // Get pages
        if(data != null) {
            List<String> bookPages = getBookPages(bookId);
            data.setPages(bookPages);
        }
        return data;
    }

    public List<BookData> getNotDeletedBooksAfter(int bookId) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        ResultSet result = null;
        List<BookData> books = new ArrayList<>();
        try {
            String query = "SELECT `id`,`author`, `time`, `title`, `deleted` FROM `" + con.prefix + "news_book_meta` " +
                    "WHERE `id` > ? AND `deleted` = 0  ORDER BY `id` DESC LIMIT 0, 7";
            stmt = con.con.prepareStatement(query);
            stmt.setInt(1, bookId);
            result = stmt.executeQuery();
            while(result.next()) {
                int curBookId = result.getInt("id");
                BookData book = getBookDataResultSet(result);
                book.setId(curBookId);
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        closeConnection(con);

        // Get pages
        for(BookData book : books) {
            book.setPages(getBookPages(book.getId()));
        }
        return books;
    }

    public List<BookData> getBookListForPage(int page, boolean loadPages) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        ResultSet result = null;
        List<BookData> books = new ArrayList<>();
        try {
            if(page < 1) {
                return books;
            }
            int from = (page - 1) * 7;
            int to = from + 7;
            String query = "SELECT `id`,`author`, `time`, `title`, `deleted` FROM `" + con.prefix + "news_book_meta` " +
                    "LIMIT " + from + ", " + to;
            stmt = con.con.prepareStatement(query);
            result = stmt.executeQuery();
            while(result.next()) {
                int curBookId = result.getInt("id");
                BookData book = getBookDataResultSet(result);
                book.setId(curBookId);
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        closeConnection(con);

        // Get pages
        if(loadPages) {
            for (BookData book : books) {
                book.setPages(getBookPages(book.getId()));
            }
        }
        return books;
    }

    public List<String> getBookPages(int bookId) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        ResultSet result = null;
        List<String> pages = new ArrayList<>();
        BookData data = null;
        try {
            String query = "SELECT `text` FROM `" + con.prefix + "news_book_pages` " +
                    "WHERE `bookId` = ? ORDER BY `page` ASC";
            stmt = con.con.prepareStatement(query);
            stmt.setInt(1, bookId);
            result = stmt.executeQuery();
            while(result.next()) {
                String pageText = result.getString(1);
                pages.add(pageText);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        closeConnection(con);
        return pages;
    }

    public LastViewedData getLastViewed(String name) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        ResultSet result = null;
        LastViewedData data = null;
        try {
            String query = "SELECT `lastViewed`, `time` FROM `" + con.prefix + "news_playerHistory` " +
                    "WHERE `username` = ?";
            stmt = con.con.prepareStatement(query);
            stmt.setString(1, name);
            result = stmt.executeQuery();
            if(result.next()) {
                data = new LastViewedData(result.getInt(1), result.getLong(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        closeConnection(con);
        return data;
    }

    public void setLastViewed(String name, LastViewedData data) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        try {
            String query = "INSERT INTO `" + con.prefix + "news_playerHistory`(`username`, `lastViewed`, `time`) VALUES(?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE  `lastViewed` = ?, `time` = ?";
            stmt = con.con.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setInt(2, data.getBookId());
            stmt.setLong(3, data.getTime());
            stmt.setInt(4, data.getBookId());
            stmt.setLong(5, data.getTime());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        closeConnection(con);
    }

    public void setLastViewed(String name, int bookId) {
        long time = System.currentTimeMillis() / 1000L;
        LastViewedData data = new LastViewedData(bookId, time);
        setLastViewed(name, data);
    }

    public Integer getLastBookId() {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        ResultSet result = null;
        Integer lastBookId = 0;
        try {
            String query = "SELECT `id` FROM `" + con.prefix + "news_book_meta` " +
                    "ORDER BY `id` DESC LIMIT 0, 1";
            stmt = con.con.prepareStatement(query);
            result = stmt.executeQuery();
            if(result.next()) {
                lastBookId = result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        closeConnection(con);
        return lastBookId;
    }

    public Integer getLastBookIdQuery(boolean deleted) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        ResultSet result = null;
        Integer lastBookId = null;
        try {
            String query = "SELECT `id`, FROM `" + con.prefix + "news_book_meta` " +
                    "WHERE `deleted` = ? ORDER BY `id` DESC LIMIT 0, 1";
            stmt = con.con.prepareStatement(query);
            stmt.setBoolean(1, deleted);
            result = stmt.executeQuery();
            if(result.next()) {
                lastBookId = result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        closeConnection(con);
        return lastBookId;
    }

    public void setDeleted(int bookId, boolean deleted) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        try {
            String query = "UPDATE `" + con.prefix + "news_book_meta` SET `deleted` = ? " +
                    "WHERE `id` = ?";
            stmt = con.con.prepareStatement(query);
            stmt.setBoolean(1, deleted);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        closeConnection(con);
    }

    private BookData getBookDataResultSet(ResultSet result) throws SQLException {
        String author = result.getString("author");
        Long time = result.getLong("time");
        String title = result.getString("title");
        Boolean deleted = result.getBoolean("deleted");
        return new BookData(author, title, time, deleted);
    }
}
