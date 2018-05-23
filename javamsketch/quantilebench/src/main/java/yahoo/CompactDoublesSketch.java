/*
 * Copyright 2017, Yahoo! Inc. Licensed under the terms of the
 * Apache License 2.0. See LICENSE file at the project root for terms.
 */

package yahoo;

import com.yahoo.memory.Memory;
import com.yahoo.memory.WritableMemory;

/**
 * @author Jon Malkin
 */
public abstract class CompactDoublesSketch extends DoublesSketch {
    CompactDoublesSketch(final int k) {
        super(k);
    }

    public static CompactDoublesSketch heapify(final Memory srcMem) {
        return HeapCompactDoublesSketch.heapifyInstance(srcMem);
    }

    @Override
    boolean isCompact() {
        return true;
    }

    /**
     * Gets the Memory if it exists, otherwise returns null.
     * @return the Memory if it exists, otherwise returns null.
     */
    @Override
    abstract WritableMemory getMemory();
}
