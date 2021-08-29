package io.github.darkkronicle.advancedchatcore;

import lombok.Getter;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModuleHandler {

    private static final ModuleHandler INSTANCE = new ModuleHandler();

    @Getter
    private final List<Module> modules = new ArrayList<>();

    private ModuleHandler() {

    }

    public static ModuleHandler getInstance() {
        return INSTANCE;
    }

    public void registerModules() {
        modules.clear();
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            // Check if in "custom" it has "acmodule": true
            CustomValue acData = mod.getMetadata().getCustomValue("acmodule");
            if (acData == null) {
                continue;
            }
            if (acData.getType() == CustomValue.CvType.BOOLEAN && acData.getAsBoolean()) {
                // Add the module
                modules.add(new Module(mod.getMetadata().getId(), mod.getMetadata().getAuthors()));
            }
        }
    }

    /**
     * Retrieves a {@link Module} based off of a mod ID.
     *
     * This is useful for incompatible features or enabling others.
     * @param modID Mod id of the mod
     * @return An optional containing the module if found.
     */
    public Optional<Module> fromId(String modID) {
        for (Module m : modules) {
            if (m.getModId().equals(modID)) {
                return Optional.of(m);
            }
        }
        return Optional.empty();
    }

}
