package com.mebene.ACHud;

import java.io.File;

/**
 * Created by Martin on 06/07/2017.
 */

public interface AsyncProcesCompleteListener<File> {
    public void onTaskComplete(java.io.File result);
}
