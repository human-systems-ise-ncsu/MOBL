package com.edvard.MoBiomechLab

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import java.lang.Double.NaN
import java.util.ArrayList


class DrawView_jump : View {

    private var mRatioWidth = 0
    private var mRatioHeight = 0


    private val mDrawPoint = ArrayList<PointF>()
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mRatioX: Float = 0.toFloat()
    private var mRatioY: Float = 0.toFloat()
    private var mImgWidth: Int = 0
    private var mImgHeight: Int = 0
    private var dataset_headY = mutableListOf<Double>()
    //private var dataset_RankleY = mutableListOf<Double>()
    private var dataset_ankleY = mutableListOf<Double>()
    //private var dataset_LankleY = mutableListOf<Double>()
    private var dataset_Height = mutableListOf<Double>()
    private var parameter_height:Double = 1.0
    private var mark: Int = 1
    private var dataset_jump:Float = 0.toFloat()
    private var jumpratio:Double = 0.0
    private var temp = 3000.0


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
            textSize = sp(18).toFloat()
            color = 0xffff0000.toInt()//red
        }
    }

    private val mPaint3: Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            style = Paint.Style.FILL
            strokeWidth = dip(2).toFloat()
            textSize = sp(18).toFloat()
            color = 0xFF0000FF.toInt()//blue
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

        /*var height = -(mDrawPoint[0].y - 0.5*(mDrawPoint[10].y + mDrawPoint[13].y))
        dataset_Height.add(height)
        canvas.drawText(
            dataset_Height.toString(),
            100F,
            250F,
            mPaint2
        )
        dataset_Height.forEachIndexed { index, currentDataPoint ->
            if (dataset_Height.size >=11) {
                //val parameter_height = dataset_Height[10]
                jumpratio =
                    1 - (500 * dataset_Height[index]) /
                            ((500 * dataset_Height[10]) + 1)
                canvas.drawText(
                    jumpratio.toString(),
                    100F,
                    250F,
                    mPaint2
                )
            }
        }*/
        dataset_headY.add((mDrawPoint[0].y).toDouble())
        //dataset_LankleY.add(mDrawPoint[10].y.toDouble())
        //dataset_RankleY.add(mDrawPoint[13].y.toDouble())
        dataset_ankleY.add(0.5*(mDrawPoint[10].y+mDrawPoint[13].y).toDouble())

        if(dataset_headY.size >= 500){
            dataset_headY.clear()
            //dataset_LankleY.clear()
            //dataset_RankleY.clear()
            dataset_ankleY.clear()
            dataset_Height.clear()
        }

        //var parameter_height=1.0
        //var mark = 1
        val height = -(mDrawPoint[0].y - 0.5*(mDrawPoint[10].y + mDrawPoint[13].y))
        dataset_Height.add(height)
        if (dataset_Height.size > 11) {
            for (i in 10..dataset_Height.size-1) {
                if (dataset_Height[i] != 0.0) {
                    parameter_height = dataset_Height[i]
                    mark = i
                   /*canvas.drawText(
                        "height = "+ parameter_height.toString(),
                        100F,
                        250F,
                        mPaint2
                    )*/
                    break
                }
            }
                for (i in 10..dataset_Height.size-1) {
                    if (dataset_ankleY[i] != 0.0) {
                        temp = Math.min(dataset_ankleY[i], temp)
                        //temp = dataset_ankleY[i]
                        jumpratio =
                            (dataset_ankleY[mark]  - temp) / (parameter_height/(1-0.039))

                       /* canvas.drawText(
                            "temp = "+temp.toString(),
                            300F,
                            250F,
                            mPaint2
                        )*/
                    }

                //parameter_height = dataset_Height[10].toDouble()
                //mark =10


                }

        }



        canvas.drawText(
            "Jump height = "+String.format("%.4f",jumpratio)+" of your height",
            720F,
            100F,
            mPaint2
        )
        /*canvas.drawText(
            "Take-off velocity = sqrt(2g * Jump Height)", 720.toFloat(),
            140.toFloat(),
            mPaint3
        )

        canvas.drawText(
            "Take-off velocity =sqrt(" +String.format("%.4f",2*9.81*jumpratio)+" * your height)", 720.toFloat(),
            180.toFloat(),
            mPaint3
        )*/




            for ((index, pointF) in mDrawPoint.withIndex()) {
                mPaint.color = mColorArray[index]
                if (index == 0||index == 10||index == 13){
                    canvas.drawCircle(pointF.x, pointF.y, circleRadius, mPaint)
                }
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

