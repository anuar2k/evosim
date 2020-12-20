package me.anuar2k.engine.util;

import java.util.Objects;

public final class Pair<Left, Right> {
    public final Left left;
    public final Right right;

    public Pair(Left left, Right right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.left, this.right);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Pair)) {
            return false;
        }

        Pair other = (Pair)obj;

        return Objects.equals(this.left, other.left) && Objects.equals(this.right, other.right);
    }

    @Override
    public String toString() {
        return "(" + this.left + ", " + this.right + ")";
    }
}
