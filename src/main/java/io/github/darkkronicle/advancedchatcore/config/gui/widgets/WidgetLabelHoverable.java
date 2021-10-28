package io.github.darkkronicle.advancedchatcore.config.gui.widgets;

import fi.dy.masa.malilib.gui.widgets.WidgetLabel;
import fi.dy.masa.malilib.render.RenderUtils;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.util.math.MatrixStack;

public class WidgetLabelHoverable extends WidgetLabel {

    private List<String> hoverLines = null;

    public WidgetLabelHoverable(
            int x, int y, int width, int height, int textColor, List<String> lines) {
        super(x, y, width, height, textColor, lines);
    }

    public WidgetLabelHoverable(
            int x, int y, int width, int height, int textColor, String... text) {
        super(x, y, width, height, textColor, text);
    }

    public void setHoverLines(String... hoverLines) {
        this.hoverLines = Arrays.asList(hoverLines);
    }

    @Override
    public void postRenderHovered(
            int mouseX, int mouseY, boolean selected, MatrixStack matrixStack) {
        super.postRenderHovered(mouseX, mouseY, selected, matrixStack);

        if (hoverLines == null) {
            return;
        }

        if (mouseX >= this.x
                && mouseX < this.x + this.width
                && mouseY >= this.y
                && mouseY <= this.y + this.height) {
            RenderUtils.drawHoverText(mouseX, mouseY, this.hoverLines, matrixStack);
        }
    }
}
