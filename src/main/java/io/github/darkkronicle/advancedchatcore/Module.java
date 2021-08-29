package io.github.darkkronicle.advancedchatcore;

import lombok.Value;
import net.fabricmc.loader.api.metadata.Person;

import java.util.Collection;

@Value
public class Module {

    /**
     * The Mod ID of the module
     */
    String modId;

    /**
     * A {@link Collection} of {@link Person}'s
     */
    Collection<Person> authors;

}
