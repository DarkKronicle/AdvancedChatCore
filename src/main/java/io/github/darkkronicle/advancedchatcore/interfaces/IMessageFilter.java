/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.interfaces;

import io.github.darkkronicle.advancedchatcore.util.FluidText;
import java.util.Optional;

/** An interface to modify text. */
public interface IMessageFilter {
    /**
     * Modifies text
     *
     * @param text Text to modify
     * @return Modified text. If empty, the text won't be changed.
     */
    Optional<FluidText> filter(FluidText text);
}
