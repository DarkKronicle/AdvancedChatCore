/*
 * Copyright (C) 2021-2022 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.chat;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.tree.CommandNode;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.util.KeyCodes;
import io.github.darkkronicle.advancedchatcore.AdvancedChatCore;
import io.github.darkkronicle.advancedchatcore.config.ConfigStorage;
import io.github.darkkronicle.advancedchatcore.config.gui.GuiConfigHandler;
import io.github.darkkronicle.advancedchatcore.gui.IconButton;
import io.github.darkkronicle.advancedchatcore.interfaces.AdvancedChatScreenSection;
import io.github.darkkronicle.advancedchatcore.util.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import io.github.darkkronicle.advancedchatcore.util.RowList;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.network.ChatPreviewer;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.ServerList;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.DecoratableArgumentType;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class AdvancedChatScreen extends GuiBase {

    public static boolean PERMANENT_FOCUS = false;

    private String finalHistory = "";
    private int messageHistorySize = -1;
    private int startHistory = -1;
    private boolean passEvents = false;

    /** Chat field at the bottom of the screen */
    @Getter protected AdvancedTextField chatField;

    /** What the chat box started out with */
    @Getter private String originalChatText = "";

    private static String last = "";
    private final List<AdvancedChatScreenSection> sections = new ArrayList<>();

    @Getter
    private final RowList<ButtonBase> rightSideButtons = new RowList<>();

    @Getter
    private final RowList<ButtonBase> leftSideButtons = new RowList<>();

    // TODO chat preview somewhere else
    private static final Text CHAT_PREVIEW_WARNING_TOAST_TITLE = Text.translatable("chatPreview.warning.toast.title");
    private static final Text CHAT_PREVIEW_WARNING_TOAST_TEXT = Text.translatable("chatPreview.warning.toast");
    private static final Text CHAT_PREVIEW_PLACEHOLDER_TEXT = Text.translatable("chat.preview").formatted(Formatting.DARK_GRAY);

    @Getter
    private ChatPreviewer chatPreviewer;

    @Override
    protected void closeGui(boolean showParent) {
        if (ConfigStorage.ChatScreen.PERSISTENT_TEXT.config.getBooleanValue()) {
            last = chatField.getText();
        }
        super.closeGui(showParent);
    }

    public AdvancedChatScreen(boolean passEvents) {
        this.passEvents = passEvents;
    }

    public AdvancedChatScreen(int indexOfLast) {
        super();
        this.originalChatText = "";
        setupSections();
        startHistory = indexOfLast;
    }

    public AdvancedChatScreen(String originalChatText) {
        super();
        this.originalChatText = originalChatText;
        setupSections();
    }

    private void setupSections() {
        for (Function<AdvancedChatScreen, AdvancedChatScreenSection> supplier : ChatScreenSectionHolder.getInstance().getSectionSuppliers()) {
            AdvancedChatScreenSection section = supplier.apply(this);
            if (section != null) {
                sections.add(section);
            }
        }
    }

    private Color getColor() {
        return ConfigStorage.ChatScreen.COLOR.config.get();
    }

    public void resetCurrentMessage() {
        this.messageHistorySize = this.client.inGameHud.getChatHud().getMessageHistory().size();
    }

    public void initGui() {
        super.initGui();
        this.chatPreviewer = new ChatPreviewer(this.client);
        this.rightSideButtons.clear();
        this.leftSideButtons.clear();
        this.client.keyboard.setRepeatEvents(true);
        resetCurrentMessage();
        this.chatField =
                new AdvancedTextField(
                        this.textRenderer,
                        4,
                        this.height - 12,
                        this.width - 4,
                        12,
                        Text.translatable("chat.editBox")) {
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
        } else if (ConfigStorage.ChatScreen.PERSISTENT_TEXT.config.getBooleanValue()
                && !last.equals("")) {
            this.chatField.setText(last);
        }
        this.chatField.setChangedListener(this::onChatFieldUpdate);

        // Add settings button
        rightSideButtons.add("settings", new IconButton(0, 0, 14, 64, new Identifier(AdvancedChatCore.MOD_ID, "textures/gui/settings.png"), (button) -> GuiBase.openGui(GuiConfigHandler.getInstance().getDefaultScreen())));

        this.addSelectableChild(this.chatField);

        this.setInitialFocus(this.chatField);

        for (AdvancedChatScreenSection section : sections) {
            section.initGui();
        }

        int originalX = client.getWindow().getScaledWidth() - 1;
        int y = client.getWindow().getScaledHeight() - 30;
        for (int i = 0; i < rightSideButtons.rowSize(); i++) {
            List<ButtonBase> buttonList = rightSideButtons.get(i);
            int maxHeight = 0;
            int x = originalX;
            for (ButtonBase button : buttonList) {
                maxHeight = Math.max(maxHeight, button.getHeight());
                x -= button.getWidth() + 1;
                button.setPosition(x, y);
                addButton(button, null);
            }
            y -= maxHeight + 1;
        }
        originalX = 1;
        y = client.getWindow().getScaledHeight() - 30;
        for (int i = 0; i < leftSideButtons.rowSize(); i++) {
            List<ButtonBase> buttonList = leftSideButtons.get(i);
            int maxHeight = 0;
            int x = originalX;
            for (ButtonBase button : buttonList) {
                maxHeight = Math.max(maxHeight, button.getHeight());
                button.setPosition(x, y);
                addButton(button, null);
                x += button.getWidth() + 1;
            }
            y -= maxHeight + 1;
        }
        if (startHistory >= 0) {
            setChatFromHistory(-startHistory - 1);
        }
        ServerInfo serverInfo = this.client.getCurrentServerEntry();
        if (serverInfo != null && this.client.options.getChatPreview().getValue()) {
            ServerInfo.ChatPreview chatPreview = serverInfo.getChatPreview();
            if (chatPreview != null && serverInfo.shouldPreviewChat() && chatPreview.showToast()) {
                ServerList.updateServerListEntry(serverInfo);
            }
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
        this.chatPreviewer.tryRequestPending();
    }

    private void updatePreviewer(String string) {
        String text = string.trim();
        if (this.shouldPreviewChat()) {
            this.tryRequestPreview(text);
        } else {
            this.chatPreviewer.disablePreview();
        }
    }

    private void tryRequestPreview(String string) {
        if (string.startsWith("/")) {
            this.tryRequestCommandPreview(string);
        } else {
            this.tryRequestChatPreview(string);
        }
    }

    private void tryRequestChatPreview(String string) {
        this.chatPreviewer.tryRequest(string);
    }

    private void tryRequestCommandPreview(String chatText) {
        // TODO fix this
        this.chatPreviewer.disablePreview();
    }

    private boolean shouldPreviewChat() {
        if (this.client.player == null) {
            return false;
        } else if (!this.client.options.getChatPreview().getValue()) {
            return false;
        } else {
            ServerInfo serverInfo = this.client.getCurrentServerEntry();
            return serverInfo != null && serverInfo.shouldPreviewChat();
        }
    }

    private void onChatFieldUpdate(String chatText) {
        String string = this.chatField.getText();
        for (AdvancedChatScreenSection section : sections) {
            section.onChatFieldUpdate(chatText, string);
        }
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (passEvents) {
            InputUtil.Key key = InputUtil.fromKeyCode(keyCode, scanCode);
            KeyBinding.setKeyPressed(key, false);
        }
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!passEvents) {
            for (AdvancedChatScreenSection section : sections) {
                if (section.keyPressed(keyCode, scanCode, modifiers)) {
                    return true;
                }
            }
            if (super.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        if (keyCode == KeyCodes.KEY_ESCAPE) {
            // Exit out
            GuiBase.openGui(null);
            return true;
        }
        if (keyCode == KeyCodes.KEY_ENTER || keyCode == KeyCodes.KEY_KP_ENTER) {
            String string = this.chatField.getText().trim();
            // Strip message and send
            Text text = this.chatPreviewer.tryConsumeResponse(this.chatField.getText());
            MessageSender.getInstance().sendMessage(string, text);
            this.chatField.setText("");
            last = "";
            // Exit
            GuiBase.openGui(null);
            return true;
        }
        if (keyCode == KeyCodes.KEY_UP) {
            // Go through previous history
            this.setChatFromHistory(-1);
            return true;
        }
        if (keyCode == KeyCodes.KEY_DOWN) {
            // Go through previous history
            this.setChatFromHistory(1);
            return true;
        }
        if (keyCode == KeyCodes.KEY_PAGE_UP) {
            // Scroll
            client.inGameHud
                    .getChatHud()
                    .scroll(this.client.inGameHud.getChatHud().getVisibleLineCount() - 1);
            return true;
        }
        if (keyCode == KeyCodes.KEY_PAGE_DOWN) {
            // Scroll
            client.inGameHud
                    .getChatHud()
                    .scroll(-this.client.inGameHud.getChatHud().getVisibleLineCount() + 1);
            return true;
        }
        if (passEvents) {
            this.chatField.setText("");
            InputUtil.Key key = InputUtil.fromKeyCode(keyCode, scanCode);
            KeyBinding.setKeyPressed(key, true);
            KeyBinding.onKeyPressed(key);
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

        // Send to hud to scroll
        client.inGameHud.getChatHud().scroll((int) amount);
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (AdvancedChatScreenSection section : sections) {
            if (section.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        ChatHud hud = client.inGameHud.getChatHud();
        if (hud.mouseClicked(mouseX, mouseY)) {
            return true;
        }
        Style style = hud.getTextStyleAt(mouseX, mouseY);
        if (style != null && style.getClickEvent() != null) {
            if (this.handleTextClick(style)) {
                return true;
            }
        }
        return (this.chatField.mouseClicked(mouseX, mouseY, button)
                || super.mouseClicked(mouseX, mouseY, button));
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
    public boolean mouseDragged(
            double mouseX, double mouseY, int button, double deltaX, double deltaY) {
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
        int targetIndex = this.messageHistorySize + i;
        int maxIndex = this.client.inGameHud.getChatHud().getMessageHistory().size();
        targetIndex = MathHelper.clamp(targetIndex, 0, maxIndex);
        if (targetIndex != this.messageHistorySize) {
            if (targetIndex == maxIndex) {
                this.messageHistorySize = maxIndex;
                this.chatField.setText(this.finalHistory);
            } else {
                if (this.messageHistorySize == maxIndex) {
                    this.finalHistory = this.chatField.getText();
                }

                String hist = this.client.inGameHud.getChatHud().getMessageHistory().get(targetIndex);
                this.chatField.setText(hist);
                for (AdvancedChatScreenSection section : sections) {
                    section.setChatFromHistory(hist);
                }
                this.messageHistorySize = targetIndex;
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
        Style style = hud.getTextStyleAt(mouseX, mouseY);
        if (style != null && style.getHoverEvent() != null) {
            this.renderTextHoverEffect(matrixStack, style, mouseX, mouseY);
        }
        if (this.chatPreviewer.shouldRenderPreview()) {
            this.renderChatPreview(matrixStack);
        }
    }

    public void renderChatPreview(MatrixStack matrices) {
        int i = (int)(255.0 * (this.client.options.getChtOpacity().getValue() * 0.9F + 0.1F));
        int j = (int)(255.0 * this.client.options.getTextBackgroundOpacity().getValue());
        int k = this.getPreviewWidth();
        List<OrderedText> list = this.getPreviewText();
        int l = this.getPreviewHeight(list);
        RenderSystem.enableBlend();
        matrices.push();
        matrices.translate((double)this.getPreviewLeft(), (double)this.getPreviewTop(l), 0.0);
        fill(matrices, 0, 0, k, l, j << 24);
        matrices.translate(2.0, 2.0, 0.0);

        for(int m = 0; m < list.size(); ++m) {
            OrderedText orderedText = list.get(m);
            this.client.textRenderer.drawWithShadow(matrices, orderedText, 0.0F, (float)(m * 9), i << 24 | 16777215);
        }

        matrices.pop();
        RenderSystem.disableBlend();
    }

    private List<OrderedText> getPreviewText() {
        Text text = this.chatPreviewer.getPreviewText();
        return text != null ? this.textRenderer.wrapLines(text, this.getPreviewWidth()) : List.of(CHAT_PREVIEW_PLACEHOLDER_TEXT.asOrderedText());
    }

    private int getPreviewWidth() {
        return this.client.currentScreen.width - 4;
    }

    private int getPreviewHeight(List<OrderedText> lines) {
        return Math.max(lines.size(), 1) * 9 + 4;
    }

    private int getPreviewBottom() {
        return this.client.currentScreen.height - 15;
    }

    private int getPreviewTop(int previewHeight) {
        return this.getPreviewBottom() - previewHeight;
    }

    private int getPreviewLeft() {
        return 2;
    }

    private int getPreviewRight() {
        return this.client.currentScreen.width - 2;
    }

    @Override
    protected void drawScreenBackground(int mouseX, int mouseY) {}

    public boolean isPauseScreen() {
        return false;
    }

    private void setText(String text) {
        this.chatField.setText(text);
    }
}
