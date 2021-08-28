package io.github.darkkronicle.advancedchatcore.chat;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.KeyCodes;
import fi.dy.masa.malilib.util.StringUtils;
import io.github.darkkronicle.advancedchatcore.config.ConfigStorage;
import io.github.darkkronicle.advancedchatcore.config.gui.GuiConfigHandler;
import io.github.darkkronicle.advancedchatcore.gui.CleanButton;
import io.github.darkkronicle.advancedchatcore.interfaces.AdvancedChatScreenSection;
import io.github.darkkronicle.advancedchatcore.util.ColorUtil;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class AdvancedChatScreen extends GuiBase {
    private String finalHistory = "";
    private int messageHistorySize = -1;
    @Getter
    protected TextFieldWidget chatField;

    @Getter
    private String originalChatText = "";

    private static String last = "";
    private final List<AdvancedChatScreenSection> sections = new ArrayList<>();

    @Override
    protected void closeGui(boolean showParent) {
        if (ConfigStorage.ChatScreen.PERSISTENT_TEXT.config.getBooleanValue()) {
            last = chatField.getText();
        }
        super.closeGui(showParent);
    }

    public AdvancedChatScreen(String originalChatText) {
        super();
        this.originalChatText = originalChatText;
        for (Function<AdvancedChatScreen, AdvancedChatScreenSection> supplier : ChatScreenSectionHolder.getInstance().getSectionSuppliers()) {
            sections.add(supplier.apply(this));
        }
    }

    private ColorUtil.SimpleColor getColor() {
        return ConfigStorage.ChatScreen.COLOR.config.getSimpleColor();
    }

    public void initGui() {
        super.initGui();
        this.client.keyboard.setRepeatEvents(true);
        this.messageHistorySize = this.client.inGameHud.getChatHud().getMessageHistory().size();
        this.chatField = new TextFieldWidget(this.textRenderer, 4, this.height - 12, this.width - 4, 12, new TranslatableText("chat.editBox")) {
            protected MutableText getNarrationMessage() {
                return null;
            }
        };
        if (ConfigStorage.ChatScreen.MORE_TEXT.config.getBooleanValue()) {
            this.chatField.setMaxLength(64000);
        } else {
            this.chatField.setMaxLength(256);
        }
        this.chatField.setDrawsBackground(false);
        if (!this.originalChatText.equals("")) {
            this.chatField.setText(this.originalChatText);
        } else if (ConfigStorage.ChatScreen.PERSISTENT_TEXT.config.getBooleanValue() && !last.equals("")) {
            this.chatField.setText(last);
        }
        this.chatField.setChangedListener(this::onChatFieldUpdate);


        ColorUtil.SimpleColor baseColor = getColor();
        int x = client.getWindow().getScaledWidth() - 1;
        String settings = StringUtils.translate("advancedchat.gui.button.settings");
        int settingsWidth = StringUtils.getStringWidth(settings) + 5;
        x -= settingsWidth + 5;
        CleanButton settingsButton = new CleanButton(x, client.getWindow().getScaledHeight() - 27, settingsWidth, 11, baseColor, settings);
        this.addButton(settingsButton, (button, mouseButton) -> GuiBase.openGui(GuiConfigHandler.getInstance().getDefaultScreen()));

        this.addSelectableChild(this.chatField);

        this.setInitialFocus(this.chatField);

        for (AdvancedChatScreenSection section : sections) {
            section.initGui();
        }

    }

    public void resize(MinecraftClient client, int width, int height) {
        String string = this.chatField.getText();
        this.init(client, width, height);
        this.setText(string);
        for (AdvancedChatScreenSection section : sections) {
            section.resize(width, height);
        }
    }

    @Override
    public void removed() {
        this.client.keyboard.setRepeatEvents(false);
        for (AdvancedChatScreenSection section : sections) {
            section.removed();
        }
    }

    public void tick() {
        this.chatField.tick();
    }

    private void onChatFieldUpdate(String chatText) {
        String string = this.chatField.getText();
        for (AdvancedChatScreenSection section : sections) {
            section.onChatFieldUpdate(chatText, string);
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (AdvancedChatScreenSection section : sections) {
            if (section.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if (keyCode == KeyCodes.KEY_ESCAPE) {
            GuiBase.openGui(null);
            return true;
        }
        if (keyCode == KeyCodes.KEY_ENTER || keyCode == KeyCodes.KEY_KP_ENTER) {
            String string = this.chatField.getText().trim();
            if (!string.isEmpty()) {
                if (string.length() > 256) {
                    string = string.substring(0, 256);
                }
                this.sendMessage(string);
            }
            this.chatField.setText("");
            last = "";
            GuiBase.openGui(null);
            return true;
        }
        if (keyCode == KeyCodes.KEY_UP) {
            this.setChatFromHistory(-1);
            return true;
        }
        if (keyCode == KeyCodes.KEY_DOWN) {
            this.setChatFromHistory(1);
            return true;
        }
        if (keyCode == KeyCodes.KEY_PAGE_UP) {
            client.inGameHud.getChatHud().scroll(this.client.inGameHud.getChatHud().getVisibleLineCount() - 1);
            return true;
        }
        if (keyCode == KeyCodes.KEY_PAGE_DOWN) {
            client.inGameHud.getChatHud().scroll(-this.client.inGameHud.getChatHud().getVisibleLineCount() + 1);
            return true;
        }
        return false;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (amount > 1.0D) {
            amount = 1.0D;
        }

        if (amount < -1.0D) {
            amount = -1.0D;
        }

        for (AdvancedChatScreenSection section : sections) {
            if (section.mouseScrolled(mouseX, mouseY, amount)) {
                return true;
            }
        }
        if (!hasShiftDown()) {
            amount *= 7.0D;
        }

        client.inGameHud.getChatHud().scroll(amount);
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (AdvancedChatScreenSection section : sections) {
            if (section.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        if (client.inGameHud.getChatHud().mouseClicked(mouseX, mouseY)) {
            return true;
        }
        return this.chatField.mouseClicked(mouseX, mouseY, button) || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        for (AdvancedChatScreenSection section : sections) {
            if (section.mouseReleased(mouseX, mouseY, mouseButton)) {
                return true;
            }
        }
        return super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (AdvancedChatScreenSection section : sections) {
            if (section.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                return true;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    protected void insertText(String text, boolean override) {
        if (override) {
            this.chatField.setText(text);
        } else {
            this.chatField.write(text);
        }
    }

    public void setChatFromHistory(int i) {
        int j = this.messageHistorySize + i;
        int k = this.client.inGameHud.getChatHud().getMessageHistory().size();
        j = MathHelper.clamp(j, 0, k);
        if (j != this.messageHistorySize) {
            if (j == k) {
                this.messageHistorySize = k;
                this.chatField.setText(this.finalHistory);
            } else {
                if (this.messageHistorySize == k) {
                    this.finalHistory = this.chatField.getText();
                }

                String hist = this.client.inGameHud.getChatHud().getMessageHistory().get(j);
                this.chatField.setText(hist);
                for (AdvancedChatScreenSection section : sections) {
                    section.setChatFromHistory(hist);
                }
                this.messageHistorySize = j;
            }
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        ChatHud hud = client.inGameHud.getChatHud();
        this.setFocused(this.chatField);
        this.chatField.setTextFieldFocused(true);
        fill(matrixStack, 2, this.height - 14, this.width - 2, this.height - 2, getColor().color());
        this.chatField.render(matrixStack, mouseX, mouseY, partialTicks);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        for (AdvancedChatScreenSection section : sections) {
            section.render(matrixStack, mouseX, mouseY, partialTicks);
        }
        Style style = hud.getText(mouseX, mouseY);
        if (style != null && style.getHoverEvent() != null) {
            this.renderTextHoverEffect(matrixStack, style, mouseX, mouseY);
        }
    }

    @Override
    protected void drawScreenBackground(int mouseX, int mouseY) {

    }

    public boolean isPauseScreen() {
        return false;
    }


    private void setText(String text) {
        this.chatField.setText(text);
    }

}
