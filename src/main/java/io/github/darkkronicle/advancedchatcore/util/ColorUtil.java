package io.github.darkkronicle.advancedchatcore.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import lombok.With;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;
import net.minecraft.util.Formatting;

/**
 * A static utility class that helps when dealing with colors that use bit shifting, like Minecraft.
 * In here there is a color storage class that makes getting int and creating new colors simple.
 */
@UtilityClass
public class ColorUtil {

    // Probably my best hope for color...
    // https://github.com/parzivail/StarWarsMod/blob/master/src/main/java/com/parzivail/util/ui/GLPalette.java
    // I don't like color ints :(
    // intToColor and colorToInt from parzivail https://github.com/parzivail (slightly modified to
    // account for Alpha)

    /**
     * Turns a packed RGB color into a Color
     *
     * @param rgb The color to unpack
     * @return The new Color
     */
    public SimpleColor intToColor(int rgb) {
        int alpha = rgb >> 24 & 0xFF;
        int red = rgb >> 16 & 0xFF;
        int green = rgb >> 8 & 0xFF;
        int blue = rgb & 0xFF;
        return new SimpleColor(red, green, blue, alpha);
    }

    /**
     * Turns a Color into a packed RGB int
     *
     * @param c The color to pack
     * @return The packed int
     */
    public int colorToInt(SimpleColor c) {
        int rgb = c.alpha();
        rgb = (rgb << 8) + c.red();
        rgb = (rgb << 8) + c.green();
        rgb = (rgb << 8) + c.blue();
        return rgb;
    }

    public SimpleColor fade(SimpleColor color, float percent) {
        float alpha = (float) color.alpha();
        return color.withAlpha((int) Math.floor((alpha * percent)));
    }

    public SimpleColor fromFormatting(Formatting formatting) {
        return new SimpleColor(formatting.getColorValue());
    }

    // Standard quick reference colors
    public final SimpleColor WHITE = new SimpleColor(255, 255, 255, 255);
    public final SimpleColor BLACK = new SimpleColor(0, 0, 0, 255);
    public final SimpleColor GRAY = new SimpleColor(128, 128, 128, 255);

    /**
     * Simple class that uses Lombok's many features to simplify. You can convert at anytime from
     * the color, to an int.
     */
    @Value
    @Accessors(fluent = true)
    public static class SimpleColor {

        @Getter int red;

        @Getter int green;

        @Getter int blue;

        @Getter
        @With(AccessLevel.PUBLIC)
        int alpha;

        @Getter int color;

        public SimpleColor(int color) {
            this.color = color;
            SimpleColor completeColor = intToColor(color);
            this.red = completeColor.red();
            this.green = completeColor.green();
            this.blue = completeColor.blue();
            this.alpha = completeColor.alpha();
        }

        public SimpleColor(int red, int green, int blue, int alpha) {
            if (red > 255) {
                red = 255;
            }
            if (green > 255) {
                green = 255;
            }
            if (blue > 255) {
                blue = 255;
            }
            if (alpha > 255) {
                alpha = 255;
            }
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
            this.color = colorToInt(this);
        }

        /** Generated for use of Lombok's @With */
        public SimpleColor(int red, int green, int blue, int alpha, int color) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
            this.color = colorToInt(this);
        }

        public String getString() {
            return String.format("#%08X", color);
        }
    }
}
