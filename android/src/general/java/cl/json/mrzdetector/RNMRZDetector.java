package cl.json.mrzdetector;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.reactnative.camera.utils.ImageDimensions;
import org.reactnative.frame.RNFrame;

import java.util.ArrayList;
import java.util.List;

public class RNMRZDetector {


    private static final String TAG = RNMRZDetector.class.getSimpleName();


    public RNMRZDetector(Context context) {
    }

    // Public API
    public boolean detect(RNFrame frame) {
        int IMG_HEIGHT = 600;
        double RECTX = IMG_HEIGHT / 46.154;
        double RECTY = IMG_HEIGHT / 120.0;
        double SQLXY = IMG_HEIGHT / 39.0;

        Bitmap bmp32 = frame.getFrame().getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        Mat in = new Mat();
        Utils.bitmapToMat(bmp32, in);

        Mat dst = in.clone();
        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_RGBA2GRAY, 0);
        Size ksize =  new Size(3, 3);
        Imgproc.GaussianBlur(dst, dst, ksize, 0, 0);

        Mat rectKernel = Imgproc.getStructuringElement(
                Imgproc.MORPH_RECT,
                new Size(RECTX, RECTY)
        );


        Imgproc.morphologyEx(dst, dst, Imgproc.MORPH_BLACKHAT, rectKernel);

        Imgproc.Sobel(dst, dst, CvType.CV_8UC1, 1, 0,-1, 1, 0, Core.BORDER_DEFAULT);


        org.opencv.core.Core.convertScaleAbs( dst, dst, 1, 0);


        Imgproc.morphologyEx(dst, dst, Imgproc.MORPH_CLOSE, rectKernel);
        Imgproc.threshold(dst, dst, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

        Mat sqKernel = Imgproc.getStructuringElement(
                Imgproc.MORPH_RECT,
                new Size(SQLXY, SQLXY)
        );


        Imgproc.morphologyEx(dst, dst, Imgproc.MORPH_CLOSE, sqKernel);
        Mat M = Mat.ones(3, 3, CvType.CV_8U);
        Imgproc.erode(dst, dst, M, new Point(-1, -1), 4);

        Mat hierarchy = new Mat();

        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(
                dst,
                contours,
                hierarchy,
                Imgproc.RETR_EXTERNAL,
                Imgproc.CHAIN_APPROX_SIMPLE
        );

        boolean mrzAreaFound = false;

        for (int i = 0; i < contours.size(); ++i) {
            Rect rect = Imgproc.boundingRect(contours.get(i));

            int x = rect.x;
            int y = rect.y;
            int w = rect.width;
            int h = rect.height;

            int ar = rect.width / rect.height;

            if (ar > 5.0) {
                int pX = (int)Math.floor((x + w) * 0.03);
                int pY = (int)Math.floor((y + h) * 0.03);
                x = x - pX;
                y = y - pY;
                w = w + pX * 2;
                h = h + pY * 2;

                if (x < 0) {
                    x = 0;
                }
                if (y < 0) {
                    y = 0;
                }
                if (w < 0) {
                    w = 0;
                }
                if (h < 0) {
                    h = 0;
                }


                    /*
                    if (ar > 5.0 && crWidth > 0.75) {

                    }*/
                if(w > 340 && h > 60){


                    try {
                        Rect roi = new Rect(x, y, w, h);
                        Mat cropped = new Mat(in, roi);

                        Imgproc.rectangle(in,
                                new Point(x, y),
                                new Point(x + w, y+h),
                                new Scalar(0,255,0),
                                1);

                        /*
                        //  #   <CheckBlurry>
                        Mat destination =  new Mat();
                        Mat matGray = new Mat();

                        Imgproc.cvtColor(cropped, matGray, Imgproc.COLOR_BGR2GRAY);
                        Imgproc.Laplacian(matGray, destination, 3);
                        MatOfDouble median = new MatOfDouble();
                        MatOfDouble std = new MatOfDouble();
                        Core.meanStdDev(destination, median, std);

                        double variance = Math.pow(std.get(0, 0)[0], 2.0);
                        Log.d(TAG, "variance: " + variance);
                        double threshold = 7500;

                        if (variance <= threshold) {
                            // Blurry
                            Log.d(TAG, "Blurry: " + variance + " <= " +threshold);
                        } else {
                            // Not blurry
                            Log.d(TAG, "Not Blurry: " + variance + " > " +threshold);

                            Bitmap bitmap = Bitmap.createBitmap(cropped.cols(), cropped.rows(), Bitmap.Config.ARGB_8888);
                            Utils.matToBitmap(cropped, bitmap);

                        }
                        */
                        //  #   </CheckBlurry>
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }



                    mrzAreaFound = true;
                }



                break;
            }
        }
        return mrzAreaFound;
    }

}
