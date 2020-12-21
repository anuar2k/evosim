package me.anuar2k.engine.util;

import java.util.Arrays;

public final class ByteArrayWrapper {
    public final byte[] array;

    public ByteArrayWrapper(byte[] array) {
        this.array = array;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.array);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof ByteArrayWrapper)) {
            return false;
        }

        ByteArrayWrapper other = (ByteArrayWrapper)obj;

        return Arrays.equals(this.array, other.array);
    }
}
