package io.github.darkkronicle.advancedchatcore.config.gui.widgets;

import fi.dy.masa.malilib.gui.interfaces.ISelectionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetListBase;
import io.github.darkkronicle.advancedchatcore.interfaces.ConfigRegistryOption;
import io.github.darkkronicle.advancedchatcore.util.AbstractRegistry;
import java.util.Collection;
import java.util.stream.Collectors;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;

public class WidgetListRegistryOption<T extends ConfigRegistryOption<?>>
        extends WidgetListBase<T, WidgetRegistryOptionEntry<T>> {

    private final AbstractRegistry<?, T> registry;

    public WidgetListRegistryOption(
            int x,
            int y,
            int width,
            int height,
            @Nullable ISelectionListener<T> selectionListener,
            AbstractRegistry<?, T> registry,
            Screen parent) {
        super(x, y, width, height, selectionListener);
        this.browserEntryHeight = 22;
        this.setParent(parent);
        this.registry = registry;
    }

    @Override
    protected WidgetRegistryOptionEntry<T> createListEntryWidget(
            int x, int y, int listIndex, boolean isOdd, T entry) {
        return new WidgetRegistryOptionEntry<T>(
                x,
                y,
                this.browserEntryWidth,
                this.getBrowserEntryHeightFor(entry),
                isOdd,
                entry,
                listIndex,
                this);
    }

    @Override
    protected Collection<T> getAllEntries() {
        return registry.getAll().stream()
                .filter(option -> !option.isHidden())
                .collect(Collectors.toList());
    }
}
