package io.github.darkkronicle.advancedchatcore.interfaces;

import io.github.darkkronicle.advancedchatcore.chat.AdvancedChatScreen;
import lombok.Getter;
import net.minecraft.client.util.math.MatrixStack;

public abstract class AdvancedChatScreenSection {

    @Getter
    private final AdvancedChatScreen screen;

    public AdvancedChatScreenSection(AdvancedChatScreen screen) {
        this.screen = screen;
    }

    public void initGui() {

    }

    public void resize(int width, int height) {

    }

    public void removed() {

    }

    public void onChatFieldUpdate(String chatText, String text) {

    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return false;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return false;
    }

    public void setChatFromHistory(String hist) {

    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

    }
}
