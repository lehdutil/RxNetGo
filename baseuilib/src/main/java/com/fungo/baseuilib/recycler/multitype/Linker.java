package com.fungo.baseuilib.recycler.multitype;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

public interface Linker<T> {
    @IntRange(from = 0)
    int index(int position, @NonNull T t);
}