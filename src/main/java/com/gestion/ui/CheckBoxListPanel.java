package com.gestion.ui;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CheckBoxListPanel<T> extends JPanel {
    private List<JCheckBox> checkBoxes;
    private List<T> items;

    public CheckBoxListPanel(List<T> items) {
        this.items = items;
        this.checkBoxes = new ArrayList<>();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        for (T item : items) {
            JCheckBox checkBox = new JCheckBox(item.toString());
            checkBoxes.add(checkBox);
            add(checkBox);
        }
    }

    public List<T> getSelectedItems() {
        List<T> selected = new ArrayList<>();
        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isSelected()) {
                selected.add(items.get(i));
            }
        }
        return selected;
    }
}
