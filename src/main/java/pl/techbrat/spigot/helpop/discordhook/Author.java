package pl.techbrat.spigot.helpop.discordhook;

class Author {
    private String name;
    private String url;
    private String iconUrl;

    Author(String name, String url, String iconUrl) {
        this.name = name;
        this.url = url;
        this.iconUrl = iconUrl;
    }

    String getName() {
        return name;
    }

    String getUrl() {
        return url;
    }

    String getIconUrl() {
        return iconUrl;
    }
}