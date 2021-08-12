package io.github.darkkronicle.advancedchatcore;

import lombok.Value;
import net.fabricmc.loader.api.metadata.Person;

import java.util.Collection;

@Value
public class Module {

    String modId;
    Collection<Person> authors;

}
