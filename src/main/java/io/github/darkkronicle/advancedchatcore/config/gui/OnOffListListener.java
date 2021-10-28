package io.github.darkkronicle.advancedchatcore.config.gui;

import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import io.github.darkkronicle.advancedchatcore.config.gui.widgets.WidgetToggle;
import io.github.darkkronicle.advancedchatcore.interfaces.Translatable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnOffListListener<T extends Translatable> implements IButtonActionListener {

    private final ButtonGeneric button;
    private final WidgetToggle toggle;
    private T current;
    private HashMap<T, Boolean> onOff;
    private final List<T> order;

    public OnOffListListener(ButtonGeneric button, WidgetToggle toggle, HashMap<T, Boolean> onOff) {
        this.button = button;
        this.toggle = toggle;
        this.onOff = new HashMap<>(onOff);
        this.order = new ArrayList<>(onOff.keySet());
        next();
    }

    public IButtonActionListener getButtonListener() {
        return (button1, mouseButton) -> {
            onToggled();
        };
    }

    private void onToggled() {
        onOff.put(current, toggle.isCurrentlyOn());
    }

    private void next() {
        int i = order.indexOf(current) + 1;
        if (i >= order.size()) {
            i = 0;
        }
        current = order.get(i);
        button.setDisplayString(current.translate());
        toggle.setOn(onOff.get(current));
    }

    public List<T> getOn() {
        ArrayList<T> list = new ArrayList<>();
        for (Map.Entry<T, Boolean> entry : onOff.entrySet()) {
            if (entry.getValue()) {
                list.add(entry.getKey());
            }
        }
        return list;
    }

    public static <T extends Translatable> HashMap<T, Boolean> getOnOff(
            List<T> all, List<T> active) {
        HashMap<T, Boolean> map = new HashMap<>();
        for (T a : all) {
            map.put(a, active.contains(a));
        }
        return map;
    }

    @Override
    public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
        next();
    }
}
