package cl.json.mrzdetector.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.YuvImage;
import android.util.SparseArray;

import androidx.exifinterface.media.ExifInterface;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.face.Face;

import org.reactnative.barcodedetector.BarcodeFormatUtils;
import org.reactnative.camera.RNCameraView;
import org.reactnative.camera.RNCameraViewHelper;
import org.reactnative.camera.utils.ImageDimensions;
import org.reactnative.frame.RNFrame;
import org.reactnative.frame.RNFrameFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import cl.json.mrzdetector.MRZRect;
import cl.json.mrzdetector.RNMRZDetector;

public class MRZDetectorAsyncTask extends android.os.AsyncTask<Void, Void, MRZRect> {

    private ThemedReactContext mThemedReactContext;
    private byte[] mImageData;

    private Bitmap mBitmap;
    private int mWidth;
    private int mHeight;
    private int mRotation;
    private RNMRZDetector mMRZDetector;
    private MRZDetectorAsyncTaskDelegate mDelegate;
    private double mScaleX;
    private double mScaleY;
    private ImageDimensions mImageDimensions;
    private int mPaddingLeft;
    private int mPaddingTop;

    public MRZDetectorAsyncTask(
            MRZDetectorAsyncTaskDelegate delegate,
            ThemedReactContext themedReactContext,
            RNMRZDetector mrzDetector,
            byte[] imageData,
            int width,
            int height,
            int rotation,
            float density,
            int facing,
            int viewWidth,
            int viewHeight,
            int viewPaddingLeft,
            int viewPaddingTop
    ) {
        mImageData = imageData;
        mThemedReactContext = themedReactContext;
        mWidth = width;
        mHeight = height;
        mRotation = rotation;
        mDelegate = delegate;
        mMRZDetector = mrzDetector;
        mImageDimensions = new ImageDimensions(width, height, rotation, facing);
        mScaleX = (double) (viewWidth) / (mImageDimensions.getWidth() * density);
        mScaleY = (double) (viewHeight) / (mImageDimensions.getHeight() * density);
        mPaddingLeft = viewPaddingLeft;
        mPaddingTop = viewPaddingTop;

    }
    @Override
    protected MRZRect doInBackground(Void... voids) {
        if (isCancelled() || mDelegate == null || mMRZDetector == null) {
            return null;
        }

        RNFrame frame = RNFrameFactory.buildFrame(mImageData, mWidth, mHeight, mRotation);


        ByteArrayOutputStream outstr = new ByteArrayOutputStream();
        android.graphics.Rect rect = new android.graphics.Rect(0, 0, mWidth, mHeight);
        YuvImage yuvimage=new YuvImage(mImageData, ImageFormat.NV21,mWidth,mHeight,null);
        yuvimage.compressToJpeg(rect, 100, outstr);
        Bitmap bmp = BitmapFactory.decodeByteArray(outstr.toByteArray(), 0, outstr.size());
        return mMRZDetector.detect(frame, bmp);
    }
    @Override
    protected void onPostExecute(MRZRect mrzRect) {
        super.onPostExecute(mrzRect);
        mDelegate.onMRZDetected(mrzRect);
    }
    private WritableMap serializeEventData(WritableMap event) {

        return event;
    }
}