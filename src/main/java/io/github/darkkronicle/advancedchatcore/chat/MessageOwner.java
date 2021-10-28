/*
 * Mozilla Public License v2.0
 *
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;

/** Stores data about a message owner */
@Data
@Value
@AllArgsConstructor
@Environment(EnvType.CLIENT)
public class MessageOwner {

    /** Player name */
    String name;

    /** Entry that has player data */
    PlayerListEntry entry;

    /**
     * The texture of the player's skin
     *
     * @return Identifier with texture data
     */
    public Identifier getTexture() {
        return entry.getSkinTexture();
    }
}
