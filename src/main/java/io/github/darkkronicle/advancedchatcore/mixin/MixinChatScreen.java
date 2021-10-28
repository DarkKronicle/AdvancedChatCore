package io.github.darkkronicle.advancedchatcore.mixin;

import fi.dy.masa.malilib.gui.GuiBase;
import io.github.darkkronicle.advancedchatcore.chat.AdvancedChatScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ChatScreen.class)
public class MixinChatScreen {

    @Final @Shadow private String originalChatText;

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    public void chatInit(CallbackInfo ci) {
        // Open the AdvancedChatScreen instead
        GuiBase.openGui(new AdvancedChatScreen(this.originalChatText));
        ci.cancel();
    }

    @Inject(method = "addScreenNarrations", at = @At("HEAD"), cancellable = true)
    public void screenNarrations(NarrationMessageBuilder builder, CallbackInfo ci) {
        // Don't cause random narrations to happen/crashes
        ci.cancel();
    }
}
