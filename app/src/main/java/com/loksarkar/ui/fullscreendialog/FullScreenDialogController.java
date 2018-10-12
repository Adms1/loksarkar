
package com.loksarkar.ui.fullscreendialog;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Interface to control the dialog
 */
public interface FullScreenDialogController {

    /**
     * Enable or disable the confirm button.
     *
     * @param enabled true to enable the button, false to disable it
     */
    void setConfirmButtonEnabled(boolean enabled);


    void confirm(@Nullable Bundle result);


    void discard();


    void discardFromExtraAction(int actionId, @Nullable Bundle result);


}
