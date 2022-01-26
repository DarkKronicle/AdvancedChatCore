package io.github.darkkronicle.advancedchatcore.config.gui;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.screen.Screen;

import java.util.ArrayList;
import java.util.List;

public class TabSupplier {

    @Getter
    private final String name;

    private final String translationKey;

    @Getter
    @Setter
    private TabSupplier nestedSelection = null;

    @Getter
    private List<TabSupplier> children = new ArrayList<>();

    public TabSupplier(String name, String translationKey) {
        this.name = name;
        this.translationKey = translationKey;
    }

    public String getDisplayName() {
        return StringUtils.translate(translationKey);
    }

    public List<IConfigBase> getOptions() {
        return null;
    }

    public Screen getScreen(Screen parent) {
        return null;
    }

    public boolean isSelectable() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TabSupplier)) {
            return false;
        }
        return (((TabSupplier) obj).getName().equals(getName()));
    }

    public void addChild(TabSupplier supplier) {
        if (nestedSelection == null) {
            if (supplier.isSelectable()) {
                nestedSelection = supplier;
            }
        }
        this.children.add(supplier);
    }

    public TabSupplier get(String name) {
        for (TabSupplier child : children) {
            if (name.equals(child.getName())) {
                return child;
            }
        }
        return null;
    }

}
