package io.github.darkkronicle.advancedchatcore.konstruct;

import io.github.darkkronicle.Konstruct.functions.ObjectFunction;
import io.github.darkkronicle.Konstruct.nodes.Node;
import io.github.darkkronicle.Konstruct.parser.IntRange;
import io.github.darkkronicle.Konstruct.parser.ParseContext;
import io.github.darkkronicle.Konstruct.parser.Result;
import io.github.darkkronicle.Konstruct.type.IntegerObject;
import io.github.darkkronicle.Konstruct.type.KonstructObject;
import io.github.darkkronicle.Konstruct.type.StringObject;
import io.github.darkkronicle.advancedchatcore.util.SearchResult;
import io.github.darkkronicle.advancedchatcore.util.StringMatch;

import java.util.List;

public class StringMatchObject extends KonstructObject<StringMatchObject> {

    private final StringMatch result;

    private final static List<ObjectFunction<StringMatchObject>> FUNCTIONS = List.of(
            new ObjectFunction<>() {
                @Override
                public Result parse(ParseContext context, StringMatchObject self, List<Node> input) {
                    return Result.success(new IntegerObject(self.result.start));
                }

                @Override
                public String getName() {
                    return "getStart";
                }

                @Override
                public IntRange getArgumentCount() {
                    return IntRange.of(0);
                }
            },
            new ObjectFunction<>() {
                @Override
                public Result parse(ParseContext context, StringMatchObject self, List<Node> input) {
                    return Result.success(new IntegerObject(self.result.end));
                }

                @Override
                public String getName() {
                    return "getEnd";
                }

                @Override
                public IntRange getArgumentCount() {
                    return IntRange.of(0);
                }
            },
            new ObjectFunction<>() {
                @Override
                public Result parse(ParseContext context, StringMatchObject self, List<Node> input) {
                    return Result.success(new StringObject(self.result.match));
                }

                @Override
                public String getName() {
                    return "getMessage";
                }

                @Override
                public IntRange getArgumentCount() {
                    return IntRange.of(0);
                }
            }
    );

    public StringMatchObject(StringMatch result) {
        super(FUNCTIONS);
        this.result = result;
    }

    @Override
    public String getString() {
        return result.toString();
    }

    @Override
    public String getTypeName() {
        return "string_match";
    }
}
