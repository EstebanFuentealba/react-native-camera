package cl.json.mrzdetector.events;

import androidx.core.util.Pools;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import org.reactnative.camera.CameraViewManager;
import org.reactnative.camera.events.FacesDetectedEvent;

import cl.json.mrzdetector.MRZRect;


public class MrzDetectedEvent extends Event<MrzDetectedEvent> {

    private static final Pools.SynchronizedPool<MrzDetectedEvent> EVENTS_POOL =
            new Pools.SynchronizedPool<>(3);


    private int mX;
    private int mY;
    private int mWidth;
    private int mHeight;



    public MrzDetectedEvent() {}
    private void init(int viewTag,  int x, int y, int height, int width) {
        super.init(viewTag);
        this.mX = x;
        this.mY = y;
        this.mWidth = width;
        this.mHeight = height;
    }
    public static MrzDetectedEvent obtain(int viewTag, int x, int y, int height, int width) {
        MrzDetectedEvent event = EVENTS_POOL.acquire();
        if (event == null) {
            event = new MrzDetectedEvent();
        }
        event.init(viewTag, x, y, width, height);
        return event;
    }

    @Override
    public short getCoalescingKey() {
        return 0;
    }

    @Override
    public String getEventName() {
        return CameraViewManager.Events.EVENT_ON_MRZ_DETECTED.toString();
    }

    @Override
    public void dispatch(RCTEventEmitter rctEventEmitter) {
        rctEventEmitter.receiveEvent(getViewTag(), getEventName(), serializeEventData());
    }

    private WritableMap serializeEventData() {
        WritableMap event = Arguments.createMap();
            WritableMap bounds = Arguments.createMap();
            bounds.putInt("x", this.mX);
            bounds.putInt("y", this.mY);
            bounds.putInt("width", this.mWidth);
            bounds.putInt("height", this.mHeight);
        event.putString("type", "mrz");
        event.putMap("bounds", bounds);
        event.putInt("target", getViewTag());
        return event;
    }
}
