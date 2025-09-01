package com.example.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DrawingViewLetter extends View {
    public static class Stroke{
        Path path;
        Paint paint;
        Stroke(Path p, Paint pt){path=p;paint=pt;}
    }

    private final List<Stroke> strokes = new ArrayList<>();
    private Path currentPath;
    private Paint currentPaint;

    private int strokeColor = Color.BLACK;


    private float dpToPx(float dp) {
       return dp * getResources().getDisplayMetrics().density;
    };

    private float strokeWidth = dpToPx(23); // normal stroke width
    private static final float DOT_RADIUS_DP = 3f; // dot radius in dp
    private float dotRadius = dpToPx(DOT_RADIUS_DP);
    private boolean isDrawingEnabled = false;
    public void setDrawingEnabled(boolean enabled) {
        this.isDrawingEnabled = enabled;
    }
    public DrawingViewLetter(Context context) {
        super(context);
        init();
    }
    public DrawingViewLetter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawingViewLetter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        currentPaint = createPaint(strokeColor, strokeWidth);
    }

    private Paint createPaint(int color, float width) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(color);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeJoin(Paint.Join.ROUND);
        p.setStrokeWidth(width);
        return p;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Stroke s : strokes) {
            canvas.drawPath(s.path, s.paint);
        }
        if (currentPath != null) {
            canvas.drawPath(currentPath, currentPaint);
        }
    }
    private float startX, startY;
    private static final float TAP_TOLERANCE = 10f; // small movement threshold

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isDrawingEnabled) return false;
        float x = event.getX();
        float y = event.getY();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                startX = x;
                startY = y;
                currentPath = new Path();
                currentPath.moveTo(x, y);
                currentPaint = createPaint(strokeColor, strokeWidth);
                invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                if (currentPath != null) {
                    currentPath.lineTo(x, y);
                    invalidate();
                }
                return true;

            case MotionEvent.ACTION_UP:
                if (currentPath != null) {
                    float dx = Math.abs(x - startX);
                    float dy = Math.abs(y - startY);

                    if (dx < TAP_TOLERANCE && dy < TAP_TOLERANCE) {
                        // 👇 This is a tap → draw a dot
                        Path dotPath = new Path();
                        dotPath.addCircle(x, y,  dotRadius, Path.Direction.CW);

                        // Always create a fresh Paint for the stroke
                        Paint dotPaint = createPaint(strokeColor, strokeWidth);
                        strokes.add(new Stroke(dotPath, dotPaint));
                    } else {
                        // 👇 This is a real stroke
                        Paint strokePaint = createPaint(strokeColor, strokeWidth);
                        strokes.add(new Stroke(currentPath, strokePaint));
                    }

                    currentPath = null;
                    invalidate();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void setStrokeColor(int color) {
        this.strokeColor = color;
    }

    public void setStrokeWidth(float widthPx) {
        this.strokeWidth = widthPx;
    }

    public void clearCanvas() {
        strokes.clear();
        currentPath = null;
        invalidate();
    }


    public void getPNGBytesResized(int width, int height, Consumer<byte[]> callback) {
        ;
        Executors.newSingleThreadExecutor().execute(() -> {
            Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            draw(canvas);
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, width, height, true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] pngBytes = stream.toByteArray();
            // 3. Return result on UI thread if needed
            new Handler(Looper.getMainLooper()).post(() -> callback.accept(pngBytes));
        });

    }
    public void sendDrawingToServer(Consumer<Integer> onResult) {
        Executors.newSingleThreadExecutor().execute(() -> {
            // 1. Create bitmap & resize
            Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            draw(canvas);
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 32, 32, true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] pngBytes = stream.toByteArray();

            // 2. Send HTTP async
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "drawing.png", RequestBody.create(MediaType.parse("image/png"), pngBytes))
                    .build();
            Request request = new Request.Builder()
                    .url("https://app-deploy-letter.onrender.com/predict")
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show()
                    );
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String jsonString = response.body().string();
                        response.close();
                        JSONObject json = new JSONObject(jsonString);
                        int predicted = json.getInt("predicted_class");
                        int mappedPredicted = predicted + 10;

                        // Return the int via Consumer on main thread
                        new Handler(Looper.getMainLooper()).post(() -> onResult.accept(mappedPredicted));

                    } catch (Exception e) {
                        new Handler(Looper.getMainLooper()).post(() ->
                                Toast.makeText(getContext(), "Invalid response", Toast.LENGTH_LONG).show()
                        );
                    }
                }
            });
        });
    }

}