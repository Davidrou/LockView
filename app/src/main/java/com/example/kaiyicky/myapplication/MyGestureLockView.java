package com.example.kaiyicky.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class MyGestureLockView extends View {

    /**
     * 不同状态的画笔
     */
    private Paint paintNormalPoint;
    private Paint paintSelectedPoint;
    private Paint paintLines;

    private Point[] cycles;
    private Path linePath = new Path();
    private float lineStartX;
    private float lineStartY;
    private float lineFirstEndX;
    private float lineFirstEndY;
    private float lineSecondEndX;
    private float lineSecondEndY;
    private boolean startDrawSecondLine;
    private boolean isOther=true;


    public MyGestureLockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyGestureLockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyGestureLockView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paintNormalPoint = new Paint();
        paintNormalPoint.setAntiAlias(true);
        paintNormalPoint.setStyle(Paint.Style.FILL);
        paintNormalPoint.setColor(getResources().getColor(R.color.circle_bg));

        paintSelectedPoint =new Paint();
        paintSelectedPoint.setAntiAlias(true);
        paintSelectedPoint.setStyle(Paint.Style.FILL);
        if(isOther) {
            paintSelectedPoint.setColor(getResources().getColor(R.color.line_bg_red));
        }else{
            paintSelectedPoint.setColor(getResources().getColor(R.color.line_bg_blue));
        }

        paintLines = new Paint();
        paintLines.setAntiAlias(true);
        paintLines.setStyle(Paint.Style.STROKE);
        paintLines.setStrokeWidth(3);
        paintLines.setColor(getResources().getColor(R.color.line_bg_blue));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        // TODO Auto-generated method stub
        super.onLayout(changed, left, top, right, bottom);
        int perSize = 0;
        if (cycles == null && (perSize = getWidth() / 6) > 0) {
            cycles = new Point[9];
            for (int i = 0;i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    Point cycle = new Point();
                    cycle.setOx(perSize * (j * 2 + 1));
                    cycle.setOy(perSize * (i * 2 + 1));
                    cycle.setR(perSize * 0.1f);
                    cycles[i * 3 + j] = cycle;
                }
            }

            // 设置为整条路径的起点
            lineStartX=cycles[0].getOx();
            lineStartY=cycles[0].getOy();
            lineFirstEndX=lineStartX;
            lineFirstEndY=lineStartY;
            lineSecondEndX=cycles[6].getOx();
            lineSecondEndY=cycles[6].getOy();
            setLinePath(cycles[6].getOx(), cycles[6].getOy(),cycles[8].getOx(),cycles[8].getOy());
            }
        }

    /**
     * 绘制所需要绘制的内容
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        for (int i = 0; i < cycles.length; i++) {
            drawPonit(cycles[i], canvas);
        }
        drawLine(canvas);
    }

    /**
     * 绘制大圆里的小圆
     *
     * @param canvas
     */
    private void drawPonit(Point cycle, Canvas canvas) {
        if(cycle.getSelectedStatus()){
            canvas.drawCircle(cycle.getOx(), cycle.getOy(), cycle.getR(), paintSelectedPoint);
        }else {
            canvas.drawCircle(cycle.getOx(), cycle.getOy(), cycle.getR(), paintNormalPoint);
        }
    }

    private void drawLine(Canvas canvas) {
        linePath.reset();
        linePath.moveTo(lineStartX,lineStartY);
        linePath.lineTo(lineFirstEndX, lineFirstEndY);
      if(startDrawSecondLine){
            linePath.lineTo(lineSecondEndX, lineSecondEndY);
       }
        canvas.drawPath(linePath, paintLines);
    }

    private void setLinePath(final float ox, final float oy,final float ox2, final float oy2){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                 cycles[0].setSelectedStatus(true);
               while(lineFirstEndY<=oy||lineFirstEndX<=ox2) {
                   if(lineFirstEndY<oy) {
                       lineFirstEndY =lineFirstEndY+ 5<=oy?lineFirstEndY+ 5:oy;
                       if(lineFirstEndY>=cycles[3].getOy()){
                           cycles[3].setSelectedStatus(true);
                       }
                       if(lineFirstEndY>=cycles[6].getOy()){
                           cycles[6].setSelectedStatus(true);
                       }
                   }else if(lineSecondEndX<ox2){
                       lineSecondEndX+=5;
                       if(lineSecondEndX>=cycles[7].getOx()){
                           cycles[7].setSelectedStatus(true);
                       }
                       if(lineSecondEndX>=cycles[8].getOx()){
                           cycles[8].setSelectedStatus(true);
                       }
                       startDrawSecondLine=true;
                   }else {
                       try {
                           Thread.sleep(1000);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                       lineStartX=cycles[0].getOx();
                       lineStartY=cycles[0].getOy();
                       lineFirstEndX=lineStartX;
                       lineFirstEndY=lineStartY;
                       lineSecondEndX=cycles[6].getOx();
                       lineSecondEndY=cycles[6].getOy();
                       startDrawSecondLine=false;
                       cycles[0].setSelectedStatus(false);
                       cycles[3].setSelectedStatus(false);
                       cycles[6].setSelectedStatus(false);
                       cycles[7].setSelectedStatus(false);
                       cycles[8].setSelectedStatus(false);
                       setLinePath(cycles[6].getOx(), cycles[6].getOy(), cycles[8].getOx(), cycles[8].getOy());
                       return;
                   }
                   MyGestureLockView.this.postInvalidate();
                   try {
                       Thread.sleep(10);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
            }
        }).start();
    }
}