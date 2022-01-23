package io.github.darkkronicle.advancedchatcore.util;

import io.github.darkkronicle.Konstruct.functions.Function;
import io.github.darkkronicle.Konstruct.functions.NamedFunction;
import io.github.darkkronicle.Konstruct.functions.Variable;
import io.github.darkkronicle.Konstruct.nodes.Node;
import io.github.darkkronicle.Konstruct.parser.*;
import io.github.darkkronicle.Konstruct.reader.builder.NodeBuilder;
import io.github.darkkronicle.Konstruct.type.IntegerObject;
import io.github.darkkronicle.Konstruct.type.StringObject;
import io.github.darkkronicle.addons.*;
import io.github.darkkronicle.advancedchatcore.AdvancedChatCore;
import lombok.Getter;
import net.minecraft.util.Util;

import java.util.List;

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
        addFunction(new TimeFunction());
        addVariable("server", () -> new StringObject(AdvancedChatCore.getServer()));
        addFunction("randomString", new Function() {
            @Override
            public Result parse(ParseContext context, List<Node> input) {
                return Result.success(AdvancedChatCore.getRandomString());
            }

            @Override
            public IntRange getArgumentCount() {
                return IntRange.none();
            }
        });
        addFunction("getColor", new Function() {
            @Override
            public Result parse(ParseContext context, List<Node> input) {
                Result res = Function.parseArgument(context, input, 0);
                if (Function.shouldReturn(res)) return res;
                Color color = Colors.getInstance().getColorOrWhite(res.getContent().getString());
                return Result.success(color.getString());
            }

            @Override
            public IntRange getArgumentCount() {
                return IntRange.of(1);
            }
        });
        addFunction("superscript", new Function() {
            @Override
            public Result parse(ParseContext context, List<Node> input) {
                int number;
                try {
                    Result res = Function.parseArgument(context, input, 0);
                    if (Function.shouldReturn(res)) return res;
                    number = Integer.parseInt(res.getContent().getString().strip());
                } catch (NumberFormatException e) {
                    return Result.success("NaN");
                }
                return Result.success(TextUtil.toSuperscript(number));
            }

            @Override
            public IntRange getArgumentCount() {
                return IntRange.of(1);
            }
        });
        addVariable("ms", () -> new IntegerObject((int) Util.getMeasuringTimeMs()));
    }

    public ParseResult parse(Node node) {
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
