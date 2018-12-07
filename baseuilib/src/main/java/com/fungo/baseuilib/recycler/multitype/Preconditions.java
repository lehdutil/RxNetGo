package com.fungo.baseuilib.recycler.multitype;

import androidx.annotation.NonNull;

public final class Preconditions {

    @SuppressWarnings("ConstantConditions")
    public static @NonNull
    <T> T checkNotNull(@NonNull final T object) {
        if (object == null) {
            throw new NullPointerException();
        }
        return object;
    }


    private Preconditions() {
    }
}