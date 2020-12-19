package me.anuar2k.engine.util;

import java.util.Objects;

public final class Pair<Left, Right> {
    public final Left left;
    public final Right right;

    //type erasure bullshit
    //in Java you just can't do instanceof SomeType<Parameter1, Parameter2...>
    //so you use getClass(). Of course, it renders our class useless for nulls
    private final Class leftClass;
    private final Class rightClass;

    public Pair(Left left, Right right) {
        this.left = left;
        this.right = right;
        this.leftClass = left.getClass();
        this.rightClass = right.getClass();
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

        if (this.leftClass != other.leftClass || this.rightClass != other.rightClass) {
            return false;
        }

        return this.left.equals(other.left) && this.right.equals(other.right);
    }

    @Override
    public String toString() {
        return "(" + this.left.toString() + ", " + this.right.toString() + ")";
    }
}
