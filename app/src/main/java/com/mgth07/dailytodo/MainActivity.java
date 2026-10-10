package com.mgth07.dailytodo;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.view.WindowInsets;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import org.json.*;

public class MainActivity extends Activity {
    private JSONArray tasks;
    private LinearLayout list;
    private EditText input;
    private TextView summary;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        try { tasks = new JSONArray(getPreferences(MODE_PRIVATE).getString("tasks", "[]")); }
        catch (JSONException e) { tasks = new JSONArray(); }
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(246, 248, 252));
        int padding = dp(20);
        root.setPadding(padding, padding, padding, padding);
        root.setOnApplyWindowInsetsListener((view, insets) -> {
            view.setPadding(padding + insets.getSystemWindowInsetLeft(),
                padding + insets.getSystemWindowInsetTop(),
                padding + insets.getSystemWindowInsetRight(),
                padding + insets.getSystemWindowInsetBottom());
            return insets;
        });
        TextView title = new TextView(this);
        title.setText("오늘 할 일"); title.setTextSize(30);
        title.setTextColor(Color.rgb(24, 41, 75));
        root.addView(title);
        summary = new TextView(this); summary.setTextSize(16);
        root.addView(summary);
        input = new EditText(this);
        input.setHint("새 할 일을 적어 주세요");
        input.setSingleLine(true);
        input.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
        input.setImeOptions(EditorInfo.IME_ACTION_DONE);
        root.addView(input);
        Button add = new Button(this); add.setText("추가");
        root.addView(add);
        add.setOnClickListener(v -> addTask());
        input.setOnEditorActionListener((v, action, event) -> {
            if (action == EditorInfo.IME_ACTION_DONE) { addTask(); return true; }
            return false;
        });
        ScrollView scroll = new ScrollView(this);
        list = new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(list);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(root); render();
    }

    private void addTask() {
        String text = input.getText().toString().trim();
        if (text.isEmpty()) { input.setError("할 일을 입력해 주세요"); return; }
        if (text.length() > 200) { input.setError("200자 이내로 적어 주세요"); return; }
        JSONObject task = new JSONObject();
        try { task.put("text", text); task.put("done", false); }
        catch (JSONException e) { throw new IllegalStateException(e); }
        tasks.put(task); input.setText(""); save(); render();
    }

    private void save() {
        getPreferences(MODE_PRIVATE).edit().putString("tasks", tasks.toString()).apply();
    }

    private void render() {
        list.removeAllViews(); int done = 0;
        for (int i = 0; i < tasks.length(); i++) {
            JSONObject task = tasks.optJSONObject(i);
            if (task == null) continue;
            final int index = i;
            LinearLayout row = new LinearLayout(this);
            row.setGravity(android.view.Gravity.CENTER_VERTICAL);
            CheckBox check = new CheckBox(this);
            check.setText(task.optString("text")); check.setTextSize(18);
            check.setChecked(task.optBoolean("done"));
            if (check.isChecked()) done++;
            row.addView(check, new LinearLayout.LayoutParams(0, -2, 1));
            check.setOnCheckedChangeListener((button, checked) -> {
                try { task.put("done", checked); }
                catch (JSONException e) { throw new IllegalStateException(e); }
                save(); render();
            });
            Button delete = new Button(this); delete.setText("삭제");
            delete.setContentDescription(task.optString("text") + " 삭제");
            row.addView(delete);
            delete.setOnClickListener(v -> new android.app.AlertDialog.Builder(this)
                .setMessage("이 할 일을 삭제할까요?")
                .setNegativeButton("취소", null)
                .setPositiveButton("삭제", (dialog, which) -> {
                    tasks.remove(index); save(); render();
                }).show());
            list.addView(row);
        }
        summary.setText("전체 " + tasks.length() + "개 · 완료 " + done + "개");
        if (tasks.length() == 0) {
            TextView empty = new TextView(this);
            empty.setText("작은 할 일부터 하나씩 시작해 보세요.");
            empty.setPadding(0, dp(24), 0, 0); list.addView(empty);
        }
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
