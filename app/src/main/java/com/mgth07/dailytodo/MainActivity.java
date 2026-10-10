package com.mgth07.dailytodo;
import android.app.*;
import android.os.*;
import android.content.*;
import android.provider.Settings;
import android.net.Uri;
import android.widget.*;
public class MainActivity extends Activity {
 private TextView status;
 public void onCreate(Bundle b) {
  super.onCreate(b);
  LinearLayout root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); int p=Math.round(24*getResources().getDisplayMetrics().density);
  root.setPadding(p,p,p,p); root.setBackgroundColor(0xfffff8ea);
  root.setOnApplyWindowInsetsListener((v,i)->{v.setPadding(p+i.getSystemWindowInsetLeft(),p+i.getSystemWindowInsetTop(),p+i.getSystemWindowInsetRight(),p+i.getSystemWindowInsetBottom());return i;});
  TextView title=new TextView(this);title.setText("산책하는 강아지");title.setTextSize(30);root.addView(title);
  TextView help=new TextView(this);help.setText("홈 화면과 다른 앱 위에서 함께 뛰어다녀요.\n\n① 표시 권한을 켜 주세요\n② 강아지 시작을 누르세요\n③ 홈 화면으로 나가 보세요\n\n누르면 점프! 드래그하면 이동해요.\n앱이나 알림에서 언제든 멈출 수 있어요.");help.setTextSize(18);root.addView(help);
  status=new TextView(this);root.addView(status);
  button(root,"① 표시 권한 설정",()->startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:"+getPackageName()))));
  button(root,"② 강아지 시작",()->{
   if(!Settings.canDrawOverlays(this)){Toast.makeText(this,"먼저 표시 권한을 켜 주세요",Toast.LENGTH_LONG).show();return;}
   if(Build.VERSION.SDK_INT>=33 && checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)!=android.content.pm.PackageManager.PERMISSION_GRANTED)requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS},1);
   Intent i=new Intent(this,DogService.class);if(Build.VERSION.SDK_INT>=26)startForegroundService(i);else startService(i);
  });
  button(root,"강아지 멈추기",()->stopService(new Intent(this,DogService.class)));setContentView(root);
 }
 private void button(LinearLayout root,String text,Runnable r){Button b=new Button(this);b.setText(text);root.addView(b);b.setOnClickListener(v->r.run());}
 public void onResume(){super.onResume();if(status!=null)status.setText(Settings.canDrawOverlays(this)?"표시 권한: 켜짐":"표시 권한: 꺼짐");}
}
