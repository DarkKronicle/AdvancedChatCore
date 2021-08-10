package io.github.darkkronicle.advancedchatcore.config.gui.widgets;

import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import io.github.darkkronicle.advancedchatcore.util.FindType;
import io.github.darkkronicle.advancedchatcore.util.SearchUtils;
import io.github.darkkronicle.advancedchatcore.util.StringMatch;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.font.TextRenderer;

import java.util.List;
import java.util.Optional;

public class WidgetIntBox extends GuiTextFieldGeneric {

    @Setter
    @Getter
    private Runnable apply = null;

    public WidgetIntBox(int x, int y, int width, int height, TextRenderer textRenderer) {
        super(x, y, width, height, textRenderer);
        this.setTextPredicate((text) -> {
            if (text.equals("")) {
                return true;
            }
            try {
                // Only allow numbers!
                Integer.valueOf(text);
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        });
        this.setDrawsBackground(true);
    }

    public Integer getInt() {
        String text = this.getText();
        if (text == null || text.length() == 0) {
            return null;
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            // Extra catch
            Optional<List<StringMatch>> omatches = SearchUtils.findMatches(text, "[0-9]+", FindType.REGEX);
            if (!omatches.isPresent()) {
                return null;
            }
            for (StringMatch m : omatches.get()) {
                try {
                    return Integer.parseInt(m.match);
                } catch (NumberFormatException err) {
                    return null;
                }
            }
        }
        return null;
    }

}
