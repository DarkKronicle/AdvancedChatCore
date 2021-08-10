package io.github.darkkronicle.advancedchatcore.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;

/**
 * Stores data about a message owner
 */
@Data
@Value
@AllArgsConstructor
@Environment(EnvType.CLIENT)
public class MessageOwner {
    String name;
    PlayerListEntry entry;

    public Identifier getTexture() {
        return entry.getSkinTexture();
    }
}
