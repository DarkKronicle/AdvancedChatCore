package io.github.darkkronicle.advancedchatcore;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import fi.dy.masa.malilib.util.StringUtils;
import io.github.darkkronicle.advancedchatcore.chat.ChatHistoryProcessor;
import io.github.darkkronicle.advancedchatcore.chat.MessageDispatcher;
import io.github.darkkronicle.advancedchatcore.config.ConfigStorage;
import io.github.darkkronicle.advancedchatcore.config.gui.GuiConfigHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class InitHandler implements IInitializationHandler {

    @Override
    public void registerModHandlers() {
        ConfigManager.getInstance().registerConfigHandler(AdvancedChatCore.MOD_ID, new ConfigStorage());
        MessageDispatcher.getInstance().register(new ChatHistoryProcessor(), -1);
        GuiConfigHandler.getInstance().addGuiSection(GuiConfigHandler.createGuiConfigSection(StringUtils.translate("advancedchat.config.tab.general"), ConfigStorage.General.OPTIONS));
    }

}
