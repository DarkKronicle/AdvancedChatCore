package io.github.darkkronicle.advancedchatcore.util;

import net.minecraft.text.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class TextBuilder {

    private List<RawText> siblings = new ArrayList<>();

    public TextBuilder append(String string) {
        siblings.add(RawText.of(string));
        return this;
    }

    public TextBuilder append(String string, Style style) {
        siblings.add(RawText.of(string, style));
        return this;
    }

    public List<RawText> getTexts() {
        return siblings;
    }

    public TextBuilder append(Text text) {
        AtomicReference<Style> last = new AtomicReference<>(null);
        AtomicReference<StringBuilder> builder = new AtomicReference<>(new StringBuilder());
        text.visit((style, asString) -> {
            if (last.get() == null) {
                last.set(style);
                builder.get().append(asString);
                return Optional.empty();
            } else if (last.get().equals(style)) {
                builder.get().append(asString);
                return Optional.empty();
            }
            append(builder.get().toString(), last.get());
            last.set(style);
            builder.set(new StringBuilder(asString));
            return Optional.empty();
        }, Style.EMPTY);
        if (!builder.get().isEmpty()) {
            append(builder.get().toString(), last.get());
        }
        return this;
    }

    public MutableText build() {
        MutableText newText = Text.empty();
        for (RawText sib : siblings) {
            newText.append(Text.literal(sib.content()).fillStyle(sib.style()));
        }
        return newText;
    }

}
