/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.mixin;

import fi.dy.masa.malilib.gui.GuiBase;
import io.github.darkkronicle.advancedchatcore.chat.AdvancedSleepingChatScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.SleepingChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(SleepingChatScreen.class)
public class MixinSleepingChatScreen {

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    public void sleepInit(CallbackInfo ci) {
        // Open the Advanced sleeping screen
        GuiBase.openGui(new AdvancedSleepingChatScreen());
        ci.cancel();
    }
}
