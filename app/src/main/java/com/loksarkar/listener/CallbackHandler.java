package com.loksarkar.listener;

import java.util.List;

public interface CallbackHandler<T> {
    void onNewPage(List<T> page);
}
