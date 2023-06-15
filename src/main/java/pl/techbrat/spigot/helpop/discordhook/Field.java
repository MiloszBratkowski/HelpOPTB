package pl.techbrat.spigot.helpop.discordhook;

class Field {
    private String name;
    private String value;
    private boolean inline;

    Field(String name, String value, boolean inline) {
        this.name = name;
        this.value = value;
        this.inline = inline;
    }

    String getName() {
        return name;
    }

    String getValue() {
        return value;
    }

    boolean isInline() {
        return inline;
    }
}