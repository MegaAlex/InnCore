package me.megaalex.inncore.messages;

public enum Message {

    NOPERM("&cYou don't have permissions to use this command!"),
    MORE_HELP("&6For more commands see &5/credits help"),
    ERROR("&cAn error occurred! Please try again later."),
    ERRORCUSTOM("&cError: {1}"),
    SUCCESSCUSTOM("&a{1}"),
    NEGATIVE_ARG("&cPlease use a positive number for argument [amount]!"),
    YOU_HAVE("&6You have &5{1}&6 credits."),
    USER_HAS("&5{1}&6 has &5{2}&6 credits."),
    CANT_SEND("&6You don't have &5{1}&6 credits!"),
    SENT("&6You sent &5{2}&6 credits to &5{1}&6."),
    GIVEN("&6You gave &5{2}&6 credits to &5{1}&6."),
    RECEIVED("&6You received &5{2}&6 credits from &5{1}&6."),
    TAKEN("&6You took &5{2}&6 credits from &5{1}&6."),
    TOOK("&5{1}&6 took from you &5{2}&6 credits."),
    SET("&6CYou changed the credits of &5{1}&6 to &5{2}&6."),
    HELP_HEADER("&6Commands:"),
    HELP_COMMNAD("&6- &5{1} &6 - {2}"),
    VILLAGER_DISABLE("&cVillager trading is disabled!"),
    BOOK_LIST_HEADER("&6Book list:"),
    BOOK_LIST_ENTRY("&a {1} - &f{2}&a(&f{3}&a)"),
    BOOK_LIST_ENTRY_DELETED("&c {1} - &f{2}&c(&f{3}&c)"),
    CREDITS_TOP_HEADER("&6Players with most credits:"),
    CREDITS_TOP_NOINFO("&cNo players have credits!"),
    CREDITS_TOP_ENTRY("&6 {1}: &5{2}&6 - &5{3}");

    String text;
    Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
