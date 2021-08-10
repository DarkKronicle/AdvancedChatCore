package io.github.darkkronicle.advancedchatcore.interfaces;

import net.minecraft.client.gui.screen.Screen;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * An interface to supply a screen.
 */
public interface IScreenSupplier {

    /**
     * Get's a supplier of a screen based off of a parent.
     *
     * @param parent Parent screen (nullable)
     * @return Supplier of a screen
     */
    Supplier<Screen> getScreen(@Nullable Screen parent);

}
