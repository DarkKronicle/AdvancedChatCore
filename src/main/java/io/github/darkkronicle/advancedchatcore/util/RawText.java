package io.github.darkkronicle.advancedchatcore.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.With;
import lombok.experimental.Accessors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Class that allows for easy mutable objects that are like minecraft Text.
 */

@Accessors(chain = true)
@AllArgsConstructor
@Environment(EnvType.CLIENT)
public class RawText implements MutableText {
    @Getter @Setter @With
    private String message;
    @Getter @Setter @With
    private Style style;

    private RawText(RawText text) {
        this.style = text.withStyle(text.getStyle()).getStyle();
        this.message = text.getMessage();
    }

    /**
     * Apply a color to the RawText
     * @param string Content
     * @param color Color that will get transfered over
     * @return New RawText
     */
    public static RawText withColor(String string, ColorUtil.SimpleColor color) {
        if (color == null) {
            return new RawText(string, Style.EMPTY);
        }
        Style style = Style.EMPTY;
        TextColor textColor = TextColor.fromRgb(color.color());
        return RawText.withStyle(string, style.withColor(textColor));
    }

    public static RawText withStyle(String string, Style base) {
        return new RawText(string, base);
    }

    @Override
    public String asString() {
        return null;
    }

    /**
     * Will ALWAYS RETURN NULL
     * DON'T USE
     */
    @Override
    public List<Text> getSiblings() {
        return new ArrayList<>(Collections.singleton(this));
    }

    @Override
    public RawText copy() {
        return new RawText(this);
    }

    @Override
    public RawText shallowCopy() {
        return copy();
    }

    @Override
    public OrderedText asOrderedText() {
        return OrderedText.styledForwardsVisitedString(message, style);
    }

    @Override
    public <T> Optional<T> visit(StyledVisitor<T> styledVisitor, Style style) {
        return this.visitSelf(styledVisitor, style);
    }

    @Override
    public <T> Optional<T> visit(Visitor<T> visitor) {
        return this.visitSelf(visitor);
    }

    @Override
    public <T> Optional<T> visitSelf(StyledVisitor<T> visitor, Style style) {
        return visitor.accept(this.style.withParent(style), message);
    }

    @Override
    public <T> Optional<T> visitSelf(Visitor<T> visitor) {
        return visitor.accept(message);
    }

    /**
     * Appends the message content to the message.
     * @param text Text message to add
     * @return MutableText that was added on
     */
    @Override
    public MutableText append(Text text) {
        this.setMessage(message + text.getString());
        return this;
    }
}
