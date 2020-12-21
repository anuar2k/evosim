package me.anuar2k.engine.property;

import java.util.ArrayList;
import java.util.List;

public class ChildrenProperty implements Property {
    private final List<ChildrenProperty> children = new ArrayList<>();

    public ChildrenProperty() {

    }

    public void addChild(ChildrenProperty child) {

    }

    public List<ChildrenProperty> getChildren() {
        return this.children;
    }
}
