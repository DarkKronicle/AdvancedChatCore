package io.github.darkkronicle.advancedchatcore.util;

import io.github.darkkronicle.Konstruct.IntRange;
import io.github.darkkronicle.Konstruct.NodeProcessor;
import io.github.darkkronicle.Konstruct.ParseContext;
import io.github.darkkronicle.Konstruct.builder.NodeBuilder;
import io.github.darkkronicle.Konstruct.functions.Function;
import io.github.darkkronicle.Konstruct.functions.NamedFunction;
import io.github.darkkronicle.Konstruct.functions.Variable;
import io.github.darkkronicle.Konstruct.nodes.Node;
import io.github.darkkronicle.addons.*;
import io.github.darkkronicle.addons.conditions.BooleanFunction;
import io.github.darkkronicle.advancedchatcore.AdvancedChatCore;
import lombok.Getter;
import net.minecraft.util.Util;

import java.util.List;
import java.util.Optional;

public class AdvancedChatKonstruct {

    private static final AdvancedChatKonstruct INSTANCE = new AdvancedChatKonstruct();

    public static AdvancedChatKonstruct getInstance() {
        return INSTANCE;
    }

    @Getter
    private NodeProcessor processor;

    private AdvancedChatKonstruct() {
        reset();
        addFunction(new CalculatorFunction());
        addFunction(new GetFunction());
        addFunction(new RandomFunction());
        addFunction(new ReplaceFunction());
        addFunction(new RoundFunction());
        addFunction(new OwOFunction());
        addFunction(new RomanNumeralFunction());
        addFunction(new IsMatchFunction());
        BooleanFunction.addAllConditionalFunctions(processor);
        addFunction(new TimeFunction());
        addFunction(new NullFunction());
        addVariable("server", AdvancedChatCore::getServer);
        addFunction("randomString", new Function() {
            @Override
            public String parse(ParseContext context, List<Node> input) {
                return AdvancedChatCore.getRandomString();
            }

            @Override
            public IntRange getArgumentCount() {
                return IntRange.none();
            }
        });
        addFunction("getColor", new Function() {
            @Override
            public String parse(ParseContext context, List<Node> input) {
                Color color = Colors.getInstance().getColorOrWhite(Function.parseArgument(context, input, 0));
                return color.getString();
            }

            @Override
            public IntRange getArgumentCount() {
                return IntRange.of(1);
            }
        });
        addFunction("superscript", new Function() {
            @Override
            public String parse(ParseContext context, List<Node> input) {
                int number;
                try {
                    number = Integer.parseInt(Function.parseArgument(context, input, 0).strip());
                } catch (NumberFormatException e) {
                    return "NaN";
                }
                return TextUtil.toSuperscript(number);
            }

            @Override
            public IntRange getArgumentCount() {
                return IntRange.of(1);
            }
        });
        addVariable("ms", () -> String.valueOf(Util.getMeasuringTimeMs()));
    }

    public String parse(Node node) {
        return processor.parse(node);
    }

    public Node getNode(String string) {
        return new NodeBuilder(string).build();
    }

    public NodeProcessor copy() {
        return processor.copy();
    }

    /** Not really recommended to call... */
    public void reset() {
        this.processor = new NodeProcessor();
    }

    public void addVariable(String key, Variable variable) {
        processor.addVariable(key, variable);
    }

    public void addFunction(NamedFunction function) {
        processor.addFunction(function);
    }

    public void addFunction(String key, Function function) {
        processor.addFunction(key, function);
    }

}
