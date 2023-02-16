package com.example.ns;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Options {
    private static final String defaultTheme = "Dark";
    public StringProperty theme;

    private static Options instance;

    public static Options getInstance() {
        if (instance == null)
            instance = new Options();
        return instance;
    }

    public Options() {
        theme = new SimpleStringProperty();
        theme.setValue(Database.getInstance().getConfig("theme").orElse(defaultTheme));
        theme.addListener((observableValue, oldValue, newValue) ->
                Database.getInstance().setConfig("theme", newValue));
    }
}
