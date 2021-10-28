/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
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
