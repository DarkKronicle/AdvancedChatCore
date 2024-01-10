package io.github.darkkronicle.advancedchatcore.gui;

import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import io.github.darkkronicle.advancedchatcore.util.Color;
import io.github.darkkronicle.advancedchatcore.util.TextUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.LinkedHashMap;

public class ContextMenu extends WidgetBase {

    private final LinkedHashMap<Text, ContextConsumer> options;
    private Text hoveredEntry = null;

    @Getter
    private final int contextX;

    @Getter
    private final int contextY;

    @Getter
    @Setter
    private Runnable close;

    @Setter
    @Getter
    private Color background;

    @Setter
    @Getter
    private Color hover;

    public ContextMenu(int x, int y, LinkedHashMap<Text, ContextConsumer> options, Runnable close) {
        this(x, y, options, close, new Color(0, 0, 0, 200), new Color(255, 255, 255, 100));
    }

    public ContextMenu(int x, int y, LinkedHashMap<Text, ContextConsumer> options, Runnable close, Color background, Color hover) {
        super(x, y, 10, 10);
        this.contextX = x;
        this.contextY = y;
        this.options = options;
        updateDimensions();
        this.close = close;
        this.background = background;
        this.hover = hover;
    }

    public void updateDimensions() {
        setWidth(TextUtil.getMaxLengthString(options.keySet().stream().map(Text::getString).toList()) + 4);
        setHeight(options.size() * (textRenderer.fontHeight + 2));
        int windowWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
        int windowHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
        if (x + width > windowWidth) {
            x = windowWidth - width;
        }
        if (y + height > windowHeight) {
            y = windowHeight - height;
        }
    }

    @Override
    public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        boolean success = super.onMouseClicked(mouseX, mouseY, mouseButton);
        if (success) {
            return true;
        }
        // Didn't click on this
        close.run();
        return false;
    }

    @Override
    protected boolean onMouseClickedImpl(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton != 0) {
            return false;
        }
        if (hoveredEntry == null) {
            return false;
        }
        options.get(hoveredEntry).takeAction(contextX, contextY);
        close.run();
        return true;
    }

    @Override
    public void render(int mouseX, int mouseY, boolean selected, DrawContext context) {
        drawRect(context, x, y, width, height, background.color());
        int rX = x + 2;
        int rY = y + 2;
        hoveredEntry = null;
        for (Text option : options.keySet()) {
            if (mouseX >= x && mouseX <= x + width && mouseY >= rY - 2 && mouseY < rY + fontHeight + 1) {
                hoveredEntry = option;
                drawRect(context, rX - 2, rY - 2, width, textRenderer.fontHeight + 2, hover.color());
            }
            context.drawTextWithShadow(textRenderer, option, rX, rY, -1);
            rY += textRenderer.fontHeight + 2;
        }
    }

    private static void drawRect(DrawContext context, int x, int y, int width, int height, int color) {
        context.fill(x, y, x + width, y + height, color);
    }

    public interface ContextConsumer  {
        void takeAction(int x, int y);
    }
}
