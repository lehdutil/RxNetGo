package com.fungo.baseuilib.recycler.multitype;

import androidx.annotation.NonNull;

public interface ClassLinker<T> {
    @NonNull
    Class<? extends MultiTypeViewHolder<T, ?>> index(int position, @NonNull T t);
}
