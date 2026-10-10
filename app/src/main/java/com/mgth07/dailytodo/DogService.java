package com.mgth07.dailytodo;
import android.app.*;
import android.content.*;
import android.content.pm.ServiceInfo;
import android.graphics.PixelFormat;
import android.os.*;
import android.provider.Settings;
import android.view.*;
public class DogService extends Service {
 private WindowManager wm; private WindowManager.LayoutParams lp; private DogView dog;
 private final Handler handler=new Handler(Looper.getMainLooper());
 private float x,y,vx=70,vy=22,downX,downY,startX,startY; private boolean dragging,moved;
 private long last,jumpStart;private int width,height;
 private int dp(int n){return Math.round(n*getResources().getDisplayMetrics().density);}
 private final Runnable tick=new Runnable(){public void run(){
  if(dog==null)return;if(!Settings.canDrawOverlays(DogService.this)){stopSelf();return;}
  long now=SystemClock.uptimeMillis();float dt=Math.min((now-last)/1000f,.05f);last=now;
  android.util.DisplayMetrics m=new android.util.DisplayMetrics();wm.getDefaultDisplay().getMetrics(m);
  float maxX=Math.max(0,m.widthPixels-width),maxY=Math.max(0,m.heightPixels-height-dp(32));
  if(!dragging){x+=vx*dt*getResources().getDisplayMetrics().density;y+=vy*dt*getResources().getDisplayMetrics().density;if(x<0||x>maxX)vx=-vx;if(y<dp(28)||y>maxY)vy=-vy;}
  x=Math.max(0,Math.min(maxX,x));y=Math.max(Math.min(dp(28),maxY),Math.min(maxY,y));
  float jump=jumpStart==0?0:(now-jumpStart)/650f;if(jump>=1){jumpStart=0;jump=0;}
  lp.x=Math.round(x);lp.y=Math.max(0,Math.round(y-(float)Math.sin(jump*Math.PI)*dp(55)));
  dog.phase+=dt*14;dog.right=vx>=0;dog.invalidate();
  try{wm.updateViewLayout(dog,lp);}catch(IllegalArgumentException|SecurityException e){stopSelf();return;}
  handler.postDelayed(this,33);
 }};
 public int onStartCommand(Intent intent,int flags,int id){
  if(intent!=null&&"STOP".equals(intent.getAction())){stopSelf();return START_NOT_STICKY;}
  if(!Settings.canDrawOverlays(this)){stopSelf();return START_NOT_STICKY;}
  NotificationManager nm=getSystemService(NotificationManager.class);
  if(Build.VERSION.SDK_INT>=26)nm.createNotificationChannel(new NotificationChannel("dog","강아지 산책",NotificationManager.IMPORTANCE_LOW));
  PendingIntent open=PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class),PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);
  PendingIntent stop=PendingIntent.getService(this,1,new Intent(this,DogService.class).setAction("STOP"),PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);
  Notification.Builder b=Build.VERSION.SDK_INT>=26?new Notification.Builder(this,"dog"):new Notification.Builder(this);
  Notification n=b.setSmallIcon(android.R.drawable.ic_menu_compass).setContentTitle("강아지가 산책 중이에요").setContentText("설정을 열거나 멈추기로 종료하세요").setContentIntent(open).setOngoing(true).addAction(new Notification.Action.Builder(android.R.drawable.ic_menu_close_clear_cancel,"멈추기",stop).build()).build();
  if(Build.VERSION.SDK_INT>=34)startForeground(1,n,ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);else startForeground(1,n);
  if(dog!=null)return START_NOT_STICKY;
  wm=(WindowManager)getSystemService(WINDOW_SERVICE);dog=new DogView(this);width=dp(112);height=dp(88);
  lp=new WindowManager.LayoutParams(width,height,Build.VERSION.SDK_INT>=26?WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY:WindowManager.LayoutParams.TYPE_PHONE,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,PixelFormat.TRANSLUCENT);
  lp.gravity=Gravity.TOP|Gravity.LEFT;x=dp(40);y=dp(240);
  dog.setOnTouchListener((v,e)->{switch(e.getActionMasked()){
   case MotionEvent.ACTION_DOWN:dragging=true;moved=false;downX=e.getRawX();downY=e.getRawY();startX=x;startY=y;break;
   case MotionEvent.ACTION_MOVE:float dx=e.getRawX()-downX,dy=e.getRawY()-downY;if(Math.hypot(dx,dy)>dp(8))moved=true;x=startX+dx;y=startY+dy;break;
   case MotionEvent.ACTION_UP:dragging=false;if(!moved){v.performClick();jumpStart=SystemClock.uptimeMillis();}break;
   case MotionEvent.ACTION_CANCEL:dragging=false;break;default:break;
  }return true;});
  try{wm.addView(dog,lp);last=SystemClock.uptimeMillis();handler.post(tick);}catch(SecurityException|WindowManager.BadTokenException e){dog=null;stopSelf();}
  return START_NOT_STICKY;
 }
 public void onDestroy(){handler.removeCallbacksAndMessages(null);if(dog!=null){try{wm.removeView(dog);}catch(IllegalArgumentException ignored){}dog=null;}stopForeground(true);super.onDestroy();}
 public IBinder onBind(Intent i){return null;}
}
