package io.github.darkkronicle.advancedchatcore.config.gui.widgets;

import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.StringUtils;
import io.github.darkkronicle.advancedchatcore.util.ColorUtil;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class WidgetColor extends GuiTextFieldGeneric {

    private int colorX;
    private ColorUtil.SimpleColor currentColor;

    public WidgetColor(int x, int y, int width, int height, ColorUtil.SimpleColor color, TextRenderer textRenderer) {
        super(x, y, width - 22, height, textRenderer);
        this.colorX = x + width - 20;
        this.currentColor = color;
        setText(String.format("#%08X", this.currentColor.color()));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        int y = this.y;
        RenderUtils.drawRect(this.colorX    , y, 19, 19, 0xFFFFFFFF);
        RenderUtils.drawRect(this.colorX + 1, y + 1, 17, 17, 0xFF000000);
        RenderUtils.drawRect(this.colorX + 2, y + 2, 15, 15, this.currentColor.color());
    }

    @Override
    public void write(String text) {
        super.write(text);
        getAndRefreshColor();
    }

    @Override
    public int getWidth() {
        return super.getWidth() + 22;
    }

    public ColorUtil.SimpleColor getAndRefreshColor() {
        this.currentColor = new ColorUtil.SimpleColor(StringUtils.getColor(getText(), 0));
        return this.currentColor;
    }
}
