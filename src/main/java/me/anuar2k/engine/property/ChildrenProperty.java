package me.anuar2k.engine.property;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChildrenProperty implements Property {
    private final List<ChildrenProperty> children = new ArrayList<>();

    public ChildrenProperty() {

    }

    public void addChild(ChildrenProperty child) {
        this.children.add(child);
    }

    public List<ChildrenProperty> getChildren() {
        return this.children;
    }

    public int walk(Set<ChildrenProperty> visited) {
        int result = 1;

        visited.add(this);

        for (ChildrenProperty child : this.children) {
            if (!visited.contains(child)) {
                result += child.walk(visited);
            }
        }
        return result;
    }

    public int walkDescendants() {
        Set<ChildrenProperty> visited = new HashSet<>();
        int result = 1;

        for (ChildrenProperty child : this.children) {
            if (!visited.contains(child)) {
                result += child.walk(visited);
            }
        }

        return result;
    }
}
