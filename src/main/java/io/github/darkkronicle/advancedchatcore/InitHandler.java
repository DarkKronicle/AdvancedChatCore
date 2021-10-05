package io.github.darkkronicle.advancedchatcore;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import fi.dy.masa.malilib.util.StringUtils;
import io.github.darkkronicle.advancedchatcore.chat.ChatHistoryProcessor;
import io.github.darkkronicle.advancedchatcore.chat.ChatScreenSectionHolder;
import io.github.darkkronicle.advancedchatcore.chat.DefaultChatSuggestor;
import io.github.darkkronicle.advancedchatcore.chat.MessageDispatcher;
import io.github.darkkronicle.advancedchatcore.config.ConfigStorage;
import io.github.darkkronicle.advancedchatcore.config.gui.GuiConfigHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class InitHandler implements IInitializationHandler {

    @Override
    public void registerModHandlers() {
        // Setup modules
        ModuleHandler.getInstance().registerModules();
        ConfigManager.getInstance().registerConfigHandler(AdvancedChatCore.MOD_ID, new ConfigStorage());
        // Setup chat history
        MessageDispatcher.getInstance().register(new ChatHistoryProcessor(), -1);
        List< IConfigBase> configBases = new ArrayList<>();
        for (ConfigStorage.SaveableConfig<? extends IConfigBase> saveable : ConfigStorage.General.OPTIONS) {
            configBases.add(saveable.config);
        }
        GuiConfigHandler.getInstance().addGuiSection(new GuiConfigHandler.GuiConfigSection() {
            @Override
            public List<IConfigBase> getOptions() {
                return configBases;
            }

            @Override
            public String getName() {
                return StringUtils.translate("advancedchat.config.tab.general");
            }
        });

        // This constructs the default chat suggestor
        ChatScreenSectionHolder.getInstance().addSectionSupplier((advancedChatScreen -> {
            if (AdvancedChatCore.CREATE_SUGGESTOR) {
                return new DefaultChatSuggestor(advancedChatScreen);
            }
            return null;
        }));
    }

}
