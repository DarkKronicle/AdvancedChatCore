package io.github.darkkronicle.advancedchatcore.mixin;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChatHud.class)
public interface MixinChatHudInvoker {
    @Invoker
    void invokeAddMessage(Text message, int messageId, int timestamp, boolean refresh);
}
