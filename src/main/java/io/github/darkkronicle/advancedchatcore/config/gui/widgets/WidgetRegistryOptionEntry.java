package io.github.darkkronicle.advancedchatcore.config.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.ButtonOnOff;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.StringUtils;
import io.github.darkkronicle.advancedchatcore.interfaces.ConfigRegistryOption;
import io.github.darkkronicle.advancedchatcore.util.ColorUtil;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class WidgetRegistryOptionEntry<T extends ConfigRegistryOption<?>>
        extends WidgetListEntryBase<T> {

    private final WidgetListRegistryOption<T> parent;
    private final boolean isOdd;
    private final List<String> hoverLines;
    private final int buttonStartX;
    private final T option;

    public WidgetRegistryOptionEntry(
            int x,
            int y,
            int width,
            int height,
            boolean isOdd,
            T registryOption,
            int listIndex,
            WidgetListRegistryOption<T> parent) {
        super(x, y, width, height, registryOption, listIndex);
        this.parent = parent;
        this.isOdd = isOdd;
        this.hoverLines = registryOption.getHoverLines();
        this.option = registryOption;

        y += 1;

        int pos = x + width - 2;
        pos -= addOnOffButton(pos, y, ButtonListener.Type.ACTIVE, option.isActive());
        if (this.option.getScreen(parent) != null) {
            pos -= addButton(pos, y, ButtonListener.Type.CONFIGURE);
        }

        buttonStartX = pos;
    }

    protected int addButton(int x, int y, ButtonListener.Type type) {
        ButtonGeneric button = new ButtonGeneric(x, y, -1, true, type.getDisplayName());
        this.addButton(button, new ButtonListener<>(type, this));

        return button.getWidth() + 1;
    }

    private int addOnOffButton(int xRight, int y, ButtonListener.Type type, boolean isCurrentlyOn) {
        ButtonOnOff button = new ButtonOnOff(xRight, y, -1, true, type.translate, isCurrentlyOn);
        this.addButton(button, new ButtonListener<>(type, this));

        return button.getWidth() + 1;
    }

    @Override
    public void render(int mouseX, int mouseY, boolean selected, MatrixStack matrixStack) {
        RenderUtils.color(1f, 1f, 1f, 1f);

        // Draw a lighter background for the hovered and the selected entry
        if (selected || this.isMouseOver(mouseX, mouseY)) {
            RenderUtils.drawRect(
                    this.x,
                    this.y,
                    this.width,
                    this.height,
                    ColorUtil.WHITE.withAlpha(150).color());
        } else if (this.isOdd) {
            RenderUtils.drawRect(
                    this.x, this.y, this.width, this.height, ColorUtil.WHITE.withAlpha(70).color());
        } else {
            RenderUtils.drawRect(
                    this.x, this.y, this.width, this.height, ColorUtil.WHITE.withAlpha(50).color());
        }
        String name = this.option.getDisplayName();
        this.drawString(this.x + 4, this.y + 7, ColorUtil.WHITE.color(), name, matrixStack);

        RenderUtils.color(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();

        super.render(mouseX, mouseY, selected, matrixStack);

        RenderUtils.disableDiffuseLighting();
    }

    @Override
    public void postRenderHovered(
            int mouseX, int mouseY, boolean selected, MatrixStack matrixStack) {
        super.postRenderHovered(mouseX, mouseY, selected, matrixStack);

        if (hoverLines == null) {
            return;
        }
        if (mouseX >= this.x
                && mouseX < this.buttonStartX
                && mouseY >= this.y
                && mouseY <= this.y + this.height) {
            RenderUtils.drawHoverText(mouseX, mouseY, this.hoverLines, matrixStack);
        }
    }

    private static class ButtonListener<T extends ConfigRegistryOption<?>>
            implements IButtonActionListener {

        private final Type type;
        private final WidgetRegistryOptionEntry<T> parent;

        public ButtonListener(Type type, WidgetRegistryOptionEntry<T> parent) {
            this.parent = parent;
            this.type = type;
        }

        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            if (type == Type.ACTIVE) {
                this.parent
                        .option
                        .getActive()
                        .config
                        .setBooleanValue(!this.parent.option.isActive());
                parent.parent.refreshEntries();
            } else if (type == Type.CONFIGURE) {
                Screen screen = parent.option.getScreen(parent.parent.getParent()).get();
                if (screen != null) {
                    GuiBase.openGui(screen);
                }
            }
        }

        public enum Type {
            CONFIGURE("configure"),
            ACTIVE("active");

            private final String translate;

            Type(String name) {
                this.translate = translate(name);
            }

            private static String translate(String key) {
                return "advancedchat.config.button." + key;
            }

            public String getDisplayName() {
                return StringUtils.translate(translate);
            }
        }
    }
}
