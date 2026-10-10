package com.mgth07.dailytodo;
import android.content.Context;
import android.graphics.*;
import android.view.View;
public class DogView extends View {
 private final Paint p=new Paint(3);
 public float phase; public boolean right=true;
 public DogView(Context c){super(c);setContentDescription("강아지: 누르면 점프, 드래그하면 이동");}
 private void oval(Canvas c,int color,float l,float t,float r,float b){p.setColor(color);c.drawOval(l,t,r,b,p);}
 protected void onDraw(Canvas c){
  super.onDraw(c);c.save();c.scale(getWidth()/140f,getHeight()/110f);
  if(!right){c.translate(140,0);c.scale(-1,1);}
  float stride=(float)Math.sin(phase)*10;int fur=0xffcf914e,dark=0xff6e4326;
  oval(c,0x22000000,24,95,120,102);c.translate(0,(float)Math.sin(phase*2)*2);
  p.setColor(fur);p.setStrokeWidth(10);p.setStrokeCap(Paint.Cap.ROUND);c.drawLine(34,63,18,42+stride/2,p);
  p.setColor(dark);p.setStrokeWidth(9);c.drawLine(49,74,49+stride,94,p);c.drawLine(88,74,88-stride,94,p);
  oval(c,fur,30,44,105,82);p.setColor(fur);c.drawLine(43,75,43-stride,94,p);c.drawLine(94,75,94+stride,94,p);
  oval(c,fur,83,21,123,68);oval(c,dark,82,21,97,57);oval(c,0xffffe6b9,104,44,136,65);
  oval(c,Color.BLACK,125,44,136,53);oval(c,Color.BLACK,111,33,117,40);oval(c,Color.WHITE,112,33,114,35);
  oval(c,0xfff48395,119,59,127,69);p.setColor(0xff3f9ca2);p.setStrokeWidth(5);c.drawLine(98,62,111,64,p);c.restore();
 }
 public boolean performClick(){super.performClick();return true;}
}
