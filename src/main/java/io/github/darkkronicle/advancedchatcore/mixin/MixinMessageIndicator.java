package io.github.darkkronicle.advancedchatcore.mixin;

import io.github.darkkronicle.advancedchatcore.config.ConfigStorage;
import net.minecraft.client.gui.hud.MessageIndicator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MessageIndicator.class)
public class MixinMessageIndicator {

    @Inject(method = "indicatorColor", at = @At("HEAD"), cancellable = true)
    private void getColor(CallbackInfoReturnable<Integer> ci) {
        MessageIndicator indicator = ((MessageIndicator) (Object) this);
        String name = indicator.loggedName();
        ci.setReturnValue(switch (name) {
            case "Modified" -> ConfigStorage.ChatScreen.MODIFIED.config.getColor().intValue;
            case "Filtered" -> ConfigStorage.ChatScreen.FILTERED.config.getColor().intValue;
            case "Not Secure" -> ConfigStorage.ChatScreen.NOT_SECURE.config.getColor().intValue;
            default -> // And "System"
                    ConfigStorage.ChatScreen.SYSTEM.config.getColor().intValue;
        });

    }

    @Inject(method = "icon", at = @At("HEAD"), cancellable = true)
    private void getIcon(CallbackInfoReturnable<MessageIndicator.Icon> ci) {
        if (!ConfigStorage.ChatScreen.SHOW_CHAT_ICONS.config.getBooleanValue()) {
            ci.setReturnValue(null);
        }
    }

}
