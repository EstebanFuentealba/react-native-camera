package cl.json.mrzdetector.tasks;


import com.facebook.react.bridge.WritableMap;
import org.reactnative.barcodedetector.RNBarcodeDetector;

public interface MRZDetectorAsyncTaskDelegate {
    void onMRZDetected(WritableMap payload);
}
