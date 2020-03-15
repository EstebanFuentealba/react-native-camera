package cl.json.mrzdetector.tasks;

import org.reactnative.camera.utils.ImageDimensions;
import org.reactnative.frame.RNFrame;
import org.reactnative.frame.RNFrameFactory;

import cl.json.mrzdetector.RNMRZDetector;

public class MRZDetectorAsyncTask extends android.os.AsyncTask<Void, Void, Boolean> {

    private byte[] mImageData;
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
    protected Boolean doInBackground(Void... voids) {
        if (isCancelled() || mDelegate == null || mMRZDetector == null) {
            return null;
        }

        RNFrame frame = RNFrameFactory.buildFrame(mImageData, mWidth, mHeight, mRotation);

        return mMRZDetector.detect(frame);
    }
}