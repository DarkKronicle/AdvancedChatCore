package io.github.darkkronicle.advancedchatcore.chat;

import io.github.darkkronicle.advancedchatcore.interfaces.IMessageFilter;
import io.github.darkkronicle.advancedchatcore.interfaces.IMessageProcessor;
import io.github.darkkronicle.advancedchatcore.util.FindType;
import io.github.darkkronicle.advancedchatcore.util.FluidText;
import io.github.darkkronicle.advancedchatcore.util.SearchResult;
import io.github.darkkronicle.advancedchatcore.util.SearchUtils;
import io.github.darkkronicle.advancedchatcore.util.StringMatch;
import io.github.darkkronicle.advancedchatcore.util.StyleFormatter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A class to handle chat events.
 *
 * Different events and hooks can be registered in here.
 */
@Environment(EnvType.CLIENT)
public class MessageDispatcher {

    private final static MessageDispatcher INSTANCE = new MessageDispatcher();
    private ArrayList<IMessageProcessor> processors = new ArrayList<>();
    private ArrayList<IMessageFilter> preFilters = new ArrayList<>();

    public static MessageDispatcher getInstance() {
        return INSTANCE;
    }

    private MessageDispatcher() {
        // We don't really want this to be reconstructed or changed because it will lead to problems
        // of not having everything registered
        registerPreFilter((text) -> Optional.of(StyleFormatter.formatText(text)), -1);

        registerPreFilter((text) -> {
            String string = text.getString();
            if (string.isEmpty()) {
                return Optional.empty();
            }
            SearchResult search = SearchResult.searchOf(string, "(http(s)?:\\/\\/.)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&\\/=]*)", FindType.REGEX);
            if (search.size() == 0) {
                return Optional.empty();
            }
            Map<StringMatch, FluidText.StringInsert> insert = new HashMap<>();
            for (StringMatch match : search.getMatches()) {
                insert.put(match, (current, match1) -> {
                    String url = match1.match;
                    if (!SearchUtils.isMatch(match1.match, "(http(s)?:\\/\\/.)", FindType.REGEX)) {
                        url = "https://" + url;
                    }
                    if (current.getStyle().getClickEvent() == null) {
                        return new FluidText(current.withStyle(current.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url))));
                    }
                    return new FluidText(current);
                });
            }
            text.replaceStrings(insert);
            return Optional.of(text);
        }, -1);
        registerPreFilter((IMessageProcessor) (text, orig) -> {
            LogManager.getLogger().info("[CHAT] {}",text.getString().replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n"));
            return true;
        }, -1);

    }

    /**
     * This is ONLY used for new messages in chat
     *
     * Note: It is not recommended to call this method to force add new text. Typically, grabbing the {@link net.minecraft.client.gui.hud.ChatHud} from
     * {@link net.minecraft.client.MinecraftClient} and calling addText is a safer way.
     *
     * @param text Text that is received
     */
    public void handleText(Text text) {
        FluidText fluidText = new FluidText(text);
        fluidText = preFilter(fluidText);
        if (fluidText.getString().length() == 0) {
            // No more
            return;
        }
        process(fluidText);
    }

    private FluidText preFilter(FluidText text) {
        for (IMessageFilter f : preFilters) {
            Optional<FluidText> t = f.filter(text);
            if (t.isPresent()) {
                text = t.get();
            }
        }
        return text;
    }

    private void process(FluidText text) {
        for (IMessageFilter f : processors) {
            f.filter(text);
        }
    }

    /**
     * Registers a {@link IMessageFilter} to be called to modify the text. This is to keep formatting consistent, or to
     * stop a message from being sent.
     *
     * If text of zero length is returned by the MessageFilter the text won't be sent to the processors.
     *
     * Note: It's discouraged to add a IMessageFilter that doesn't modify text. For that use registerProcess
     *
     * @param processor IMessageFilter to modify text
     * @param index Index to add it. Supplying a negative value will put it at the end.
     */
    public void registerPreFilter(IMessageFilter processor, int index) {
        if (index < 0) {
            index = preFilters.size();
        }
        if (!preFilters.contains(processor)) {
            preFilters.add(index, processor);
        }
    }

    /**
     * Register's a {@link IMessageProcessor} to handle a chat event after the message has been preprocessed.
     *
     * @param processor IMessageProcessor to get called back
     * @param index Index that it will be added to. Supplying a negative value will put it at the end.
     */
    public void register(IMessageProcessor processor, int index) {
        if (index < 0) {
            index = processors.size();
        }
        if (!processors.contains(processor)) {
            processors.add(index, processor);
        }
    }

}
