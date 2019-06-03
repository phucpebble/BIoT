package it.hp.biot.ImageClassify;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.SystemClock;
import android.util.Size;
import android.util.TypedValue;
import java.io.IOException;
import java.util.List;

import it.hp.biot.R;
import it.hp.biot.env.BorderedText;
import it.hp.biot.env.ImageUtils;
import it.hp.biot.env.Logger;
import it.hp.biot.tflite.Classifier;
import it.hp.biot.tflite.ClassifierQuantizedMobileNet;

public  class ClassifierActivity extends CameraActivity implements OnImageAvailableListener {
    private static final Logger LOGGER = new Logger();
    private static final boolean MAINTAIN_ASPECT = true;
    private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);
    private static final float TEXT_SIZE_DIP = 10;
    private Bitmap rgbFrameBitmap = null;
    private Bitmap croppedBitmap = null;
    private Bitmap cropCopyBitmap = null;
    private long lastProcessingTimeMs;
    private Integer sensorOrientation;
    private Classifier classifier;
    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;
    private BorderedText borderedText;

    @Override
    protected int getLayoutId() {
        return R.layout.camera_connection_fragment;
    }

    @Override
    protected Size getDesiredPreviewFrameSize() {
        return DESIRED_PREVIEW_SIZE;
    }

    @Override
    public void onPreviewSizeChosen(final Size size, final int rotation) {
        final float textSizePx =
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
        borderedText = new BorderedText(textSizePx);
        borderedText.setTypeface(Typeface.MONOSPACE);

        try {
            classifier = new ClassifierQuantizedMobileNet(this);
        } catch (IOException e) {
            LOGGER.e(e, "Failed to load classifier.");
            return;
        }

        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        sensorOrientation = rotation - getScreenOrientation();
        LOGGER.i("Camera orientation relative to screen canvas: %d", sensorOrientation);

        LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight);
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);
        croppedBitmap =
                Bitmap.createBitmap(
                        classifier.getImageSizeX(), classifier.getImageSizeY(), Config.ARGB_8888);

        frameToCropTransform =
                ImageUtils.getTransformationMatrix(
                        previewWidth,
                        previewHeight,
                        classifier.getImageSizeX(),
                        classifier.getImageSizeY(),
                        sensorOrientation,
                        MAINTAIN_ASPECT);

        cropToFrameTransform = new Matrix();
        frameToCropTransform.invert(cropToFrameTransform);
    }

    @Override
    protected void processImage() {
        if (classifier == null) {
            return;
        }
        rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);
        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);

        runInBackground(
                new Runnable() {
                    @Override
                    public void run() {
                        final long startTime = SystemClock.uptimeMillis();
                        final List<Classifier.Recognition> results = classifier.recognizeImage(croppedBitmap);
                        lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;
                        LOGGER.i("Detect: %s", results);
                        cropCopyBitmap = Bitmap.createBitmap(croppedBitmap);

                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        showResultsInBottomSheet(results);
                                        showFrameInfo(previewWidth + "x" + previewHeight);
                                        showCropInfo(cropCopyBitmap.getWidth() + "x" + cropCopyBitmap.getHeight());
                                        showCameraResolution(canvas.getWidth() + "x" + canvas.getHeight());
                                        showRotationInfo(String.valueOf(sensorOrientation));
                                        showInference(lastProcessingTimeMs + "ms");
                                    }
                                });

                        readyForNextImage();
                    }
                });
    }

    @Override
    protected void setUseNNAPI(final boolean isChecked) {
        if (classifier == null) {
            return;
        }
        runInBackground(
                () -> {
                    if (isChecked) {
                        classifier.useNNAPI();
                    } else {
                        classifier.useCPU();
                    }
                });
    }

    @Override
    protected void setNumThreads(final int numThreads) {
        if (classifier == null) {
            return;
        }
        runInBackground(() -> classifier.setNumThreads(numThreads));
    }
}
