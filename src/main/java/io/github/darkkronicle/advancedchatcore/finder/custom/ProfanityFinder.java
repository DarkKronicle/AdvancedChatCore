/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.finder.custom;

import io.github.darkkronicle.advancedchatcore.config.ConfigStorage;
import io.github.darkkronicle.advancedchatcore.interfaces.IFinder;
import io.github.darkkronicle.advancedchatcore.util.ProfanityUtil;
import io.github.darkkronicle.advancedchatcore.util.StringMatch;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ProfanityFinder implements IFinder {
    @Override
    public boolean isMatch(String input, String toMatch) {
        return getMatches(input, toMatch).size() != 0;
    }

    @Override
    public List<StringMatch> getMatches(String input, String toMatch) {
        return ProfanityUtil.getInstance().getBadWords(input, (float) ConfigStorage.General.PROFANITY_ABOVE.config.getDoubleValue(), ConfigStorage.General.PROFANITY_ON_WORD_BOUNDARIES.config.getBooleanValue());
    }
}
