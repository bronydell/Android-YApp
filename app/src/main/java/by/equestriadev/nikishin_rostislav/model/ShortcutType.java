package by.equestriadev.nikishin_rostislav.model;

/**
 * Created by Rostislav on 19.02.2018.
 */

public enum ShortcutType {
    APPLICATION(0),
    CONTACT(1),
    URL(2);

    private Integer hierarchy;

    private ShortcutType(final Integer hierarchy) {
        this.hierarchy = hierarchy;
    }

    public Integer getNumber() {
        return hierarchy;
    }
}
