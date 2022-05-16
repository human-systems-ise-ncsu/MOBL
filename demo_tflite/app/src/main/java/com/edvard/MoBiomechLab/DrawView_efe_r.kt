package com.edvard.MoBiomechLab

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.os.SystemClock
import android.util.AttributeSet
import android.view.View
import java.util.ArrayList


class DrawView_efe_r : View {

    private var mRatioWidth = 0
    private var mRatioHeight = 0

    private val XPoint = 60
    private val YPoint = 260
    private val XScale = 40 // 刻度长度
    private val YScale = 40
    private val XLength = 240
    private val YLength = 240
    private val mDrawPoint = ArrayList<PointF>()
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mRatioX: Float = 0.toFloat()
    private var mRatioY: Float = 0.toFloat()
    private var mImgWidth: Int = 0
    private var mImgHeight: Int = 0
    private var temp = mutableListOf<Double>()
    private var dataset_relbow = mutableListOf<Double>()
    private var dataset_relbow_av= mutableListOf<Double>()
    private var time= mutableListOf<Long>()

    private val dataPointPaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 1f
        style = Paint.Style.STROKE
    }

    private val dataPointFillPaint = Paint().apply {
        color = Color.WHITE
    }

    private val dataPointLinePaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 1.5f
        isAntiAlias = true
    }

    private val mColorArray = intArrayOf(
        resources.getColor(R.color.color_top, null),
        resources.getColor(R.color.color_neck, null),
        resources.getColor(R.color.color_l_shoulder, null),
        resources.getColor(R.color.color_l_elbow, null),
        resources.getColor(R.color.color_l_wrist, null),
        resources.getColor(R.color.color_r_shoulder, null),
        resources.getColor(R.color.color_r_elbow, null),
        resources.getColor(R.color.color_r_wrist, null),
        resources.getColor(R.color.color_l_hip, null),
        resources.getColor(R.color.color_l_knee, null),
        resources.getColor(R.color.color_l_ankle, null),
        resources.getColor(R.color.color_r_hip, null),
        resources.getColor(R.color.color_r_knee, null),
        resources.getColor(R.color.color_r_ankle, null),
        resources.getColor(R.color.color_background, null)
    )

    private val circleRadius: Float by lazy {
        dip(5).toFloat()
    }

    private val mPaint: Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            style = Paint.Style.FILL
            strokeWidth = dip(4).toFloat()
            textSize = sp(15).toFloat()
        }
    }

    private val mPaint2: Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            style = Paint.Style.FILL
            strokeWidth = dip(2).toFloat()
            textSize = sp(15).toFloat()
            color = 0xffff0000.toInt()//red
        }
    }


    constructor(context: Context) : super(context)

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    fun setImgSize(
        width: Int,
        height: Int
    ) {
        mImgWidth = width
        mImgHeight = height
        requestLayout()
    }


    /**
     * Scale according to the device.
     * @param point 2*14
     */
    fun setDrawPoint(
        point: Array<FloatArray>,
        ratio: Float
    ) {
        mDrawPoint.clear()
        var tempX: Float
        var tempY: Float
        for (i in 0..13) {
            tempX = point[0][i] / ratio / mRatioX
            tempY = point[1][i] / ratio / mRatioY
            mDrawPoint.add(PointF(tempX, tempY))
        }
        val R_lowerarmX = (mDrawPoint[7].x - mDrawPoint[6].x).toDouble()
        val R_upperarmX = (mDrawPoint[5].x - mDrawPoint[6].x).toDouble()
        val R_lowerarmY = (mDrawPoint[7].y - mDrawPoint[6].y).toDouble()
        val R_upperarmY = (mDrawPoint[5].y - mDrawPoint[6].y).toDouble()
        val R_elbow_angle_cos =
            ((R_lowerarmX * R_upperarmX + R_lowerarmY * R_upperarmY) / (Math.sqrt(
                Math.pow(
                    R_lowerarmX,
                    2.0
                ) + Math.pow(R_lowerarmY, 2.0)
            ) * Math.sqrt(Math.pow(R_upperarmX, 2.0) + Math.pow(R_upperarmY, 2.0))))
        val R_elbow_angle = (180 * Math.acos(R_elbow_angle_cos) / Math.PI)
        temp.add(R_elbow_angle)
        if (temp.size < 3){
            dataset_relbow.add(R_elbow_angle)
        }
        if (temp.size >= 5)  {
            dataset_relbow.add((temp [temp.size - 1] + temp[temp.size - 2] + temp[temp.size - 3] + temp[temp.size - 4] + temp[temp.size - 5])/5)
        }
        time.add(SystemClock.uptimeMillis())
        if (dataset_relbow.size >= 2)  {
            val a = (dataset_relbow [dataset_relbow.size - 1] - dataset_relbow[dataset_relbow.size - 2]) / (time[time.size -1] - time[time.size - 2])
            dataset_relbow_av.add(a*1000)
        }
        if(temp.size >= 101){
            temp.clear()
            dataset_relbow.clear()
            dataset_relbow_av.clear()
            time.clear()
        }
    }


    /**
     * Sets the aspect ratio for this view. The size of the view will be measured based on the ratio
     * calculated from the parameters. Note that the actual sizes of parameters don't matter, that is,
     * calling setAspectRatio(2, 3) and setAspectRatio(4, 6) make the same result.
     *
     * @param width  Relative horizontal size
     * @param height Relative vertical size
     */
    fun setAspectRatio(
        width: Int,
        height: Int
    ) {
        if (width < 0 || height < 0) {
            throw IllegalArgumentException("Size cannot be negative.")
        }
        mRatioWidth = width
        mRatioHeight = height
        requestLayout()
    }

    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        if (mDrawPoint.isEmpty()) return
        var prePointF: PointF? = null
        mPaint.color = 0xff0090ff.toInt()
        val p1 = mDrawPoint[1]

        /*canvas.drawText(
            dataset_relbow.toString(), 500F, 500F,
            mPaint
        );// 文字
        canvas.drawText(
            //time.toString(), 520F, 600F,
            dataset_relbow_av.toString(), 520F, 600F,
            mPaint
        )*/



        // Right elbow//
        val R_lowerarmX = (mDrawPoint[7].x - mDrawPoint[6].x).toDouble()
        val R_upperarmX = (mDrawPoint[5].x - mDrawPoint[6].x).toDouble()
        val R_lowerarmY = (mDrawPoint[7].y - mDrawPoint[6].y).toDouble()
        val R_upperarmY = (mDrawPoint[5].y - mDrawPoint[6].y).toDouble()
        val R_elbow_angle =
            ((R_lowerarmX * R_upperarmX + R_lowerarmY * R_upperarmY) / (Math.sqrt(
                Math.pow(
                    R_lowerarmX,
                    2.0
                ) + Math.pow(R_lowerarmY, 2.0)
            ) * Math.sqrt(Math.pow(R_upperarmX, 2.0) + Math.pow(R_upperarmY, 2.0))))

        canvas.drawText(
            "Right elbow angle ="+String.format("%.2f",(180 * Math.acos(R_elbow_angle) / Math.PI))+" degs",
            300F,
            150F,
            mPaint2
        )
        if (dataset_relbow_av.size >=1) {
            canvas.drawText(
                "Right elbow angular velocity ="+String.format("%.2f",dataset_relbow_av[dataset_relbow_av.size-1]) + " degs/s", 280F, 450F,
                mPaint2
            )
        }
        val paint = Paint()
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true); // 去锯齿
        paint.setColor(Color.BLUE);

        canvas.drawLine(
            XPoint.toFloat(),
            (YPoint - YLength).toFloat(),
            XPoint.toFloat(),
            YPoint.toFloat(),
            paint
        )
        canvas.drawLine(
            XPoint.toFloat(),
            (YPoint+40).toFloat(),
            XPoint.toFloat(),
            (YPoint+40+2*YLength).toFloat(),
            paint
        )

        canvas.drawLine(
            XPoint.toFloat(), YPoint.toFloat(), (XPoint + XLength).toFloat(),
            YPoint.toFloat(), paint
        );

        canvas.drawLine(
            XPoint.toFloat(), (YPoint+40+YLength).toFloat(), (XPoint + XLength).toFloat(),
            (YPoint+40+YLength).toFloat(), paint
        );
        canvas.drawLine(
            XPoint.toFloat(),
            (YPoint - YLength).toFloat(),
            (XPoint - 3).toFloat(),
            (YPoint - YLength + 6).toFloat(),
            paint
        ) // 箭头
        canvas.drawLine(
            XPoint.toFloat(),
            (YPoint - YLength).toFloat(),
            (XPoint + 3).toFloat(),
            (YPoint - YLength + 6).toFloat(),
            paint
        );

        canvas.drawLine(
            XPoint.toFloat(),
            (YPoint + 40).toFloat(),
            (XPoint - 3).toFloat(),
            (YPoint + 40 + 6).toFloat(),
            paint
        ) // 箭头
        canvas.drawLine(
            XPoint.toFloat(),
            (YPoint + 40).toFloat(),
            (XPoint + 3).toFloat(),
            (YPoint + 40 + 6).toFloat(),
            paint
        );
        //canvas.drawLine(0f, 0f, 0f, height.toFloat(), axisLinePaint)
        //canvas.drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), axisLinePaint)

        for (i in 0..XLength / XScale) {
            canvas.drawLine(
                (XPoint + i*XScale).toFloat(), YPoint.toFloat(), (XPoint + i*XScale).toFloat(),
                (YPoint - 5).toFloat(), paint
            ); // 刻度

            canvas.drawLine(
                (XPoint + i*XScale).toFloat(), (YPoint+40+YLength).toFloat(), (XPoint + i*XScale).toFloat(),
                (YPoint+40+YLength - 5).toFloat(), paint
            ); // 刻度

            canvas.drawText(
                (0 + XScale * i).toString(), (XPoint + i * XScale).toFloat(),(YPoint + 20).toFloat(),
                paint
            );// 文字
            canvas.drawText(
                (0 + XScale * i).toString(), (XPoint + i * XScale).toFloat(),(YPoint+40+YLength + 20).toFloat(),
                paint
            );// 文字
        }

        for (i in 0..YLength / YScale) {
            canvas.drawLine(
                XPoint.toFloat(), (YPoint - i * YScale).toFloat(), (XPoint + 5).toFloat(),
                (YPoint - i * YScale).toFloat(), paint
            ); // 刻度
            canvas.drawLine(
                XPoint.toFloat(), (YPoint+40+YLength - i * YScale).toFloat(), (XPoint + 5).toFloat(),
                (YPoint+40+YLength - i * YScale).toFloat(), paint
            ); // 刻度
            canvas.drawLine(
                XPoint.toFloat(), (YPoint+40+YLength + i * YScale).toFloat(), (XPoint + 5).toFloat(),
                (YPoint+40+YLength + i * YScale).toFloat(), paint
            ); // 刻度

            canvas.drawText(
                (0 + YScale * i).toString(), (XPoint - 40).toFloat(), (YPoint - i * YScale).toFloat(),
                paint
            );// 文字
            canvas.drawText(
                (0 + YScale * i).toString(), (XPoint - 40).toFloat(), (YPoint+40+YLength - i * YScale).toFloat(),
                paint
            );// 文字
            canvas.drawText(
                (0 - YScale * i).toString(), (XPoint - 40).toFloat(), (YPoint+40+YLength + i * YScale).toFloat(),
                paint
            );// 文字
        }

        dataset_relbow.forEachIndexed { index, currentDataPoint ->

            if (index < dataset_relbow.size - 1) {
                val nextDataPoint = dataset_relbow[index + 1]
                val startX = index.toFloat() + XPoint
                val startY = YPoint - currentDataPoint.toFloat()
                val endX = (index + 1).toFloat() + XPoint
                val endY = YPoint - nextDataPoint.toFloat()

                canvas.drawLine(startX, startY, endX, endY, dataPointLinePaint)
                //canvas.drawCircle(endX, endY, 2f, dataPointFillPaint)
                //canvas.drawCircle(endX, endY, 2f, dataPointPaint)

            }

        }


        dataset_relbow_av.forEachIndexed { index, currentDataPoint ->

            if (index < dataset_relbow_av.size - 1) {
                    val nextDataPoint = dataset_relbow_av[index + 1]
                    val startX = index.toFloat()+ XPoint
                    val startY = YPoint+40+YLength - currentDataPoint.toFloat()
                    val endX = (index + 1).toFloat()+XPoint
                    val endY = YPoint+40+YLength - nextDataPoint.toFloat()

                    canvas.drawLine(startX, startY, endX, endY, dataPointLinePaint)
                    //canvas.drawCircle(endX, endY, 2f, dataPointFillPaint)
                    //canvas.drawCircle(endX, endY, 2f, dataPointPaint)

            }

        }



        for ((index, pointF) in mDrawPoint.withIndex()) {
                mPaint.color = 0xff6fa8dc.toInt()
                canvas.drawLine(
                    mDrawPoint[6].x,
                    mDrawPoint[6].y,
                    mDrawPoint[7].x,
                    mDrawPoint[7].y,
                    mPaint
                )
                canvas.drawLine(
                    mDrawPoint[6].x,
                    mDrawPoint[6].y,
                    mDrawPoint[5].x,
                    mDrawPoint[5].y,
                    mPaint
                )
                prePointF = pointF
        }

            for ((index, pointF) in mDrawPoint.withIndex()) {
                mPaint.color = mColorArray[index]
                if (index == 5||index ==6||index == 7){
                    canvas.drawCircle(pointF.x, pointF.y, circleRadius, mPaint)
                }
            //canvas.drawCircle(pointF.x, pointF.y, circleRadius, mPaint)
            }
    }
    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        val height = View.MeasureSpec.getSize(heightMeasureSpec)
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height)
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                mWidth = width
                mHeight = width * mRatioHeight / mRatioWidth
            } else {
                mWidth = height * mRatioWidth / mRatioHeight
                mHeight = height
            }
        }

        setMeasuredDimension(mWidth, mHeight)

        mRatioX = mImgWidth.toFloat() / mWidth
        mRatioY = mImgHeight.toFloat() / mHeight
    }
}
