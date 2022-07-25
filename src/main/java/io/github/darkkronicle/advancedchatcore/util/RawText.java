package io.github.darkkronicle.advancedchatcore.util;

import net.minecraft.text.*;
import net.minecraft.util.Language;

import java.util.ArrayList;
import java.util.List;

public record RawText(String content, Style style) implements Text {

    @Override
    public Style getStyle() {
        return style;
    }

    @Override
    public TextContent getContent() {
        return new LiteralTextContent(content);
    }

    @Override
    public String getString() {
        return content;
    }

    @Override
    public List<Text> getSiblings() {
        return new ArrayList<>();
    }

    @Override
    public OrderedText asOrderedText() {
        Language language = Language.getInstance();
        return language.reorder(this);
    }

    public static RawText of(String string) {
        return RawText.of(string, Style.EMPTY);
    }

    public static RawText of(String string, Style style) {
        return new RawText(string, style);
    }
}