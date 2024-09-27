package io.github.darkkronicle.advancedchatcore.mixin;

import io.github.darkkronicle.advancedchatcore.chat.AdvancedChatScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends Entity {

    @Shadow @Final protected MinecraftClient client;

    @Shadow public float nauseaIntensity;

    public MixinClientPlayerEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
            method="updateNausea",
            at = @At(value="INVOKE", target="Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"),
            cancellable = true
    )
    public void updateNauseaHook(CallbackInfo ci) {
        if (client.currentScreen instanceof AdvancedChatScreen) {
            ci.cancel();
            nauseaIntensity += 0.0125f;
            if (this.nauseaIntensity >= 1.0f) {
                this.nauseaIntensity = 1.0f;
            }
            inNetherPortal = false;
        }
    }


}
