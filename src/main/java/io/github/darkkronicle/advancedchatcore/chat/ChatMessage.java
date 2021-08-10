package io.github.darkkronicle.advancedchatcore.chat;

import io.github.darkkronicle.advancedchatcore.util.ColorUtil;
import io.github.darkkronicle.advancedchatcore.util.StyleFormatter;
import lombok.Builder;
import lombok.Data;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import javax.annotation.Nullable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A message from chat with data stored within it.
 */
@Environment(EnvType.CLIENT)
@Data
public class ChatMessage {

    /**
     * Tick the message was created.
     */
    protected int creationTick;

    /**
     * The text that will be displayed on render.
     */
    protected Text displayText;

    /**
     * The unmodified original text. Used to keep time stamp off of.
     */
    protected Text originalText;

    /**
     * ID of the message.
     */
    protected int id;

    /**
     * The time the message was created.
     */
    protected LocalTime time;

    /**
     * The background color of the message.
     */
    protected ColorUtil.SimpleColor background;

    /**
     * The amount of times the message has been stacked.
     */
    protected int stacks;

    /**
     * Unique ID of the message.
     */
    protected UUID uuid;

    /**
     * The owner of the message.
     */
    @Nullable
    protected MessageOwner owner;

    /**
     * Split up lines for line breaks.
     */
    protected List<AdvancedChatLine> lines;

    /**
     * Set's the display text of the message and formats the line breaks.
     * @param text Text to set to
     * @param width The width that a line break should be enforced
     */
    public void setDisplayText(Text text, int width) {
        this.displayText = text;
        formatChildren(width);
    }

    /**
     * Clones the object
     * @param width Width for the line breaks to be enforced
     * @return Cloned object
     */
    public ChatMessage shallowClone(int width) {
        ChatMessage message = new ChatMessage(creationTick, displayText, originalText, id, time, background, width, owner);
        message.setStacks(getStacks());
        return message;
    }

    /**
     * A sub section of {@link ChatMessage} which contains a renderable line.
     */
    @Data
    public static class AdvancedChatLine {
        /**
         * Render text
         */
        private Text text;
        private final ChatMessage parent;
        private int width;

        private AdvancedChatLine(ChatMessage parent, Text text) {
            this.parent = parent;
            this.text = text;
            this.width = MinecraftClient.getInstance().textRenderer.getWidth(text);
        }

        @Override
        public String toString() {
            return "AdvancedChatLine{" +
                    "text=" + text +
                    ", width=" + width +
                    '}';
        }

    }

    @Builder
    protected ChatMessage(int creationTick, Text displayText, Text originalText, int id, LocalTime time, ColorUtil.SimpleColor background, int width, MessageOwner owner) {
        this.creationTick = creationTick;
        this.displayText = displayText;
        this.id = id;
        this.time = time;
        this.background = background;
        this.stacks = 0;
        this.uuid = UUID.randomUUID();
        this.owner = owner;
        this.originalText = originalText == null ? displayText : originalText;
        formatChildren(width);
    }

    /**
     * Reformat's the line breaks
     * @param width Width that the line breaks should be enforced
     */
    public void formatChildren(int width) {
        this.lines = new ArrayList<>();
        if (width == 0) {
            this.lines.add(new AdvancedChatLine(this, displayText));
        } else {
            for (Text t : StyleFormatter.wrapText(MinecraftClient.getInstance().textRenderer, width, displayText)) {
                this.lines.add(new AdvancedChatLine(this, t));
            }
        }
    }

    /**
     * Check if the original text is similar to another's original text
     * @param message Message to compare to
     * @return If it's similar
     */
    public boolean isSimilar(ChatMessage message) {
        return message.getOriginalText().getString().equals(this.getOriginalText().getString());
    }

    /**
     * Get's the total amount of lines
     * @return Line count
     */
    public int getLineCount() {
        return this.lines.size();
    }

}
