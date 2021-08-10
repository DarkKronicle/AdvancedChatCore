package io.github.darkkronicle.advancedchatcore.util;

import io.github.darkkronicle.advancedchatcore.interfaces.RegistryOption;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Create a registry with options that can be added from anywhere.
 *
 * @param <TYPE> Class to be wrapped
 * @param <OPTION> Wrapper option for the class
 */
public abstract class AbstractRegistry<TYPE, OPTION extends RegistryOption<TYPE>> {

    private List<OPTION> options = new ArrayList<>();

    public List<OPTION> getAll() {
        return options;
    }

    @Getter
    private OPTION defaultOption;

    /**
     * Add's an option directly. Recommended to use register
     * @param option Option to add
     */
    protected void add(OPTION option) {
        if (defaultOption == null) {
            defaultOption = option;
        }
        options.add(option);
    }

    public void register(Supplier<TYPE> replace, String saveString, String translation, String infoTranslation) {
        register(replace, saveString, translation, infoTranslation, true, false);
    }

    public void register(Supplier<TYPE> replace, String saveString, String translation, String infoTranslation, boolean active, boolean setDefault) {
        register(replace, saveString, translation, infoTranslation, active, setDefault, false);
    }

    public void register(Supplier<TYPE> replace, String saveString, String translation, String infoTranslation, boolean active, boolean setDefault, boolean hidden) {
        OPTION option = constructOption(replace, saveString, translation, infoTranslation, active, setDefault, hidden);
        options.add(option);
        if (setDefault || defaultOption == null) {
            defaultOption = option;
        }
    }

    public abstract AbstractRegistry<TYPE, OPTION> clone();

    public abstract OPTION constructOption(Supplier<TYPE> type, String saveString, String translation, String infoTranslation, boolean active, boolean setDefault, boolean hidden);

    public void setDefaultOption(@NonNull OPTION newDefault) {
        defaultOption = newDefault;
    }

    public OPTION fromString(String string) {
        for (OPTION m : options) {
            if (m.getSaveString().equals(string)) {
                return m;
            }
        }
        return defaultOption;
    }

    public OPTION getNext(OPTION option, boolean forward) {
        if (options.size() == 0) {
            return null;
        }
        int i = options.indexOf(option);
        if (i < 0) {
            return options.get(0);
        }
        if (forward) {
            i = i + 1;
            if (i >= options.size()) {
                return options.get(0);
            }
        } else {
            i = i - 1;
            if (i < 0) {
                return options.get(options.size() - 1);
            }
        }
        return options.get(i);
    }

}
