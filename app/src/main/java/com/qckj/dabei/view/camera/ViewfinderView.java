package com.qckj.dabei.view.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.qckj.dabei.R;

import java.util.Collection;
import java.util.HashSet;

/**
 * 二维码扫描界面的扫描框框
 */
public final class ViewfinderView extends View {
    /**
     * 刷新界面的时间
     */
    private static final long ANIMATION_DELAY = 10L;
    /**
     * 中间那条线每次刷新移动的距离
     */
    private static final int SPEEN_DISTANCE = 2;
    /**
     * 字体大小
     */
    private static final int TEXT_SIZE = 12;
    /**
     * 字体距离扫描框下面的距离
     */
    private static final int TEXT_PADDING_BOTTOM = 50;
    /**
     * 手机的屏幕密度
     */
    private static float density;
    private final int offest = 3;
    private final int maskColor;
    boolean isFirst;
    private int width = 0;
    private int height = 0;
    /**
     * 画笔对象的引用
     */
    private Paint paint;
    private TextPaint textPaint;
    /**
     * 中间滑动线的最顶端位置
     */
    private int slideTop;
    /**
     * 将扫描的二维码拍下来，这里没有这个功能，暂时不考虑
     */
    private Bitmap resultBitmap;
    private Collection<ResultPoint> possibleResultPoints;

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        textPaint = new TextPaint();
        Resources resources = getResources();
        possibleResultPoints = new HashSet<ResultPoint>(5);
        maskColor = resources.getColor(R.color.viewfinder_mask);
        density = context.getResources().getDisplayMetrics().density;
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        if (isInEditMode()) {
            return;
        }
        Rect frame = CameraManager.get().getFramingRect();
        if (frame == null) {
            return;
        }
        if (resultBitmap == null) {
            resultBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.sweep_border);
        }
        if (!isFirst) {
            isFirst = true;
            slideTop = frame.top + 2;
            width = canvas.getWidth();
            height = canvas.getHeight();
        }
        drawOutShadowRect(canvas, frame);
        drawScanLine(canvas, frame);
//		drawTipText(canvas, frame);
        postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
    }

    /**
     * 画扫描框下面的字
     *
     * @param canvas
     * @param frame
     */
    private void drawTipText(Canvas canvas, Rect frame) {
        textPaint.setColor(Color.parseColor("#929292"));
        textPaint.setTextSize(TEXT_SIZE * density);
        textPaint.setTypeface(Typeface.create("System", Typeface.BOLD));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        StaticLayout layout = new StaticLayout(getResources().getString(R.string.set_richscan_box_tip), textPaint, width, Alignment.ALIGN_NORMAL, 1.0F, 0.0F,
                true);
        canvas.save();
        canvas.translate(frame.centerX(), frame.top - TEXT_PADDING_BOTTOM * density);
        layout.draw(canvas);
        canvas.restore();
    }

    /**
     * 画扫描线
     *
     * @param canvas
     * @param frame
     */
    private void drawScanLine(Canvas canvas, Rect frame) {
        slideTop += SPEEN_DISTANCE;
        if (slideTop >= frame.bottom - offest - 2) {
            slideTop = frame.top + 2;
        }
        Rect lineRect = new Rect();
        lineRect.left = frame.left + offest;
        lineRect.right = frame.right - offest;
        lineRect.top = slideTop;
        lineRect.bottom = slideTop + 2;
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.sweep_line), null, lineRect, paint);
    }

    /**
     * 画出扫描框外面的阴影部分和扫描区域
     *
     * @param canvas
     * @param frame
     */
    private void drawOutShadowRect(Canvas canvas, Rect frame) {
        paint.setColor(maskColor);
        canvas.drawRect(0, 0, width, frame.top + offest, paint);
        canvas.drawRect(0, frame.top + offest, frame.left + offest, frame.bottom - offest, paint);
        canvas.drawRect(frame.right - offest, frame.top + offest, width, frame.bottom - offest, paint);
        canvas.drawRect(0, frame.bottom - offest, width, height, paint);
        paint.setColor(Color.WHITE);
        canvas.drawBitmap(resultBitmap, null, frame, paint);
    }

    public void drawViewfinder() {
        resultBitmap = null;
        invalidate();
    }

    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
    }

}
