package io.github.darkkronicle.advancedchatcore;

import java.util.Collection;
import lombok.Value;
import net.fabricmc.loader.api.metadata.Person;

@Value
public class Module {

    /** The Mod ID of the module */
    String modId;

    /** A {@link Collection} of {@link Person}'s */
    Collection<Person> authors;
}
