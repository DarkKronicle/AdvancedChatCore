package io.github.darkkronicle.advancedchatcore.config.gui.widgets;

import fi.dy.masa.malilib.gui.button.ButtonOnOff;
import lombok.Getter;

public class WidgetToggle extends ButtonOnOff {
    @Getter
    private boolean currentlyOn;

    public WidgetToggle(int x, int y, int width, boolean rightAlign, String translationKey, boolean isCurrentlyOn, String... hoverStrings) {
        super(x, y, width, rightAlign, translationKey, isCurrentlyOn, hoverStrings);
        this.currentlyOn = isCurrentlyOn;
    }

    @Override
    protected boolean onMouseClickedImpl(int mouseX, int mouseY, int mouseButton) {
        this.currentlyOn = !this.currentlyOn;
        this.updateDisplayString(this.currentlyOn);
        return super.onMouseClickedImpl(mouseX, mouseY, mouseButton);
    }

    public void setOn(boolean on) {
        this.currentlyOn = on;
        this.updateDisplayString(on);
    }
}
