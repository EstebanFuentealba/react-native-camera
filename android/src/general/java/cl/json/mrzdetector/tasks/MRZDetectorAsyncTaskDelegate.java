package cl.json.mrzdetector.tasks;


import com.facebook.react.bridge.WritableMap;
import org.reactnative.barcodedetector.RNBarcodeDetector;

import cl.json.mrzdetector.MRZRect;

public interface MRZDetectorAsyncTaskDelegate {
    void onMRZDetected(MRZRect payload);
}
