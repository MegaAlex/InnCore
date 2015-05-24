package me.megaalex.inncore.messages;

public enum Message {

    NOPERM("&cTi nqmash permission da polzvash tazi komanda!"),
    MORE_HELP("&6Za poveche komandi napishi &5/credits help"),
    ERROR("&cVuznikna greshka pri obrabotka! Molq opitaite otnovo po-kusno!"),
    ERRORCUSTOM("&cError: {1}"),
    NEGATIVE_ARG("&cMolq izpolzvaite polojitelni chilsa za argument [broi]!"),
    YOU_HAVE("&6Ti imash &5{1}&6 kredita."),
    USER_HAS("&5{1}&6 ima &5{2}&6 kredita."),
    CANT_SEND("&6Ti nqmash &5{1}&6 kredita!"),
    SENT("&6Ti izprati &5{2}&6 kredita na &5{1}&6."),
    GIVEN("&6Ti dade &5{2}&6 kredita na &5{1}&6."),
    RECEIVED("&6Ti poluchi &5{2}&6 kredita ot &5{1}&6."),
    TAKEN("&6Ti vze &5{2}&6 kredita ot &5{1}&6."),
    TOOK("&5{1}&6 ti vze &5{2}&6 kredita."),
    SET("&6Ti setna kreditite na &5{1}&6 na &5{2}&6."),
    HELP_HEADER("&6Komandi:"),
    HELP_COMMNAD("&6- &5{1} &6 - {2}");

    String text;
    Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
