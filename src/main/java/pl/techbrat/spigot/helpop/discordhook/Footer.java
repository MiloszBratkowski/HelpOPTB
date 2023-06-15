package pl.techbrat.spigot.helpop.discordhook;

public class Footer {
    private String text;
    private String iconUrl;

    Footer(String text, String iconUrl) {
        this.text = text;
        this.iconUrl = iconUrl;
    }

    String getText() {
        return text;
    }

    String getIconUrl() {
        return iconUrl;
    }
}
