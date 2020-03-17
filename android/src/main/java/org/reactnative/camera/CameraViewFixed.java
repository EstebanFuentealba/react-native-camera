package org.reactnative.camera;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.cameraview.CameraView;

public class CameraViewFixed extends CameraView {
    public CameraViewFixed(Context context, boolean fallbackToOldApi) {
        super(context, fallbackToOldApi);
    }

    public CameraViewFixed(Context context, AttributeSet attrs, boolean fallbackToOldApi) {
        super(context, attrs, fallbackToOldApi);
    }

    public CameraViewFixed(Context context, AttributeSet attrs, int defStyleAttr, boolean fallbackToOldApi) {
        super(context, attrs, defStyleAttr, fallbackToOldApi);
    }
}
