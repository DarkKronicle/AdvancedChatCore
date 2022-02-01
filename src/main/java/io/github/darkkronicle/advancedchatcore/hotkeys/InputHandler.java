package io.github.darkkronicle.advancedchatcore.hotkeys;

import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.hotkeys.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputHandler implements IKeybindProvider, IKeyboardInputHandler, IMouseInputHandler {

    private final static InputHandler INSTANCE = new InputHandler();
    @Getter
    private final Map<String, List<ConfigHotkey>> hotkeys = new HashMap<>();
    @Getter
    private final Map<String, String> translation = new HashMap<>();

    public static InputHandler getInstance() {
        return INSTANCE;
    }

    private InputHandler() {}

    public void clear(String modId) {
        hotkeys.remove(modId);
    }

    public void addDisplayName(String modId, String displayName) {
        translation.put(modId, displayName);
    }

    public void add(String modId, ConfigHotkey hotkey) {
        if (!hotkeys.containsKey(modId)) {
            hotkeys.put(modId, new ArrayList<>());
        }
        hotkeys.get(modId).add(hotkey);
    }

    public void add(String modId, ConfigHotkey hotkey, IHotkeyCallback callback) {
        hotkey.getKeybind().setCallback(callback);
        add(modId, hotkey);
    }

    @Override
    public void addKeysToMap(IKeybindManager manager) {
        for (List<ConfigHotkey> hots : hotkeys.values()) {
            for (IHotkey hotkey : hots) {
                manager.addKeybindToMap(hotkey.getKeybind());
            }
        }
    }

    @Override
    public void addHotkeys(IKeybindManager manager) {
        for (Map.Entry<String, List<ConfigHotkey>> hots : hotkeys.entrySet()) {
            manager.addHotkeysForCategory(hots.getKey(), "hotkeys", hots.getValue());
        }
    }

    public void reload() {
        InputEventHandler.getKeybindManager().updateUsedKeys();
    }

    public String getDisplayName(String key) {
        return translation.getOrDefault(key, key);
    }
}