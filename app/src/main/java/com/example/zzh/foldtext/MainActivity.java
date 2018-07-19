package com.example.zzh.foldtext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SpannableFoldTextView spanTextView, sTextView;
    private FoldTextView foldTextView, fTextView;
    private FrameLayout parent;
    private FrameLayout parent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spanTextView = findViewById(R.id.text);
        spanTextView.setText("111111123阿斯顿发阿斯顿发送到大。厦法定阿萨【德法师打发斯蒂芬撒地】方阿萨德法师打发斯问问蒂芬撒地方阿萨德法师打发斯蒂。芬撒地方发送到发送到发送到发送到发送到发送，到发送到发送到发送到，发送111111123阿斯顿发阿斯顿发送到大。厦法定阿萨【德法师打发斯蒂芬撒地】方阿萨德法师打发斯问问蒂芬撒地方阿萨德法师打发斯蒂。芬撒地方发送到发送到发送到发送到发送到发送，到发送到发送到发送到，发送");
        spanTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "textView点击事件", Toast.LENGTH_SHORT).show();
            }
        });
        sTextView = findViewById(R.id.text1);
        sTextView.setText("111111123阿斯顿发阿斯顿发送到大。厦法定阿萨【德法师打发斯蒂芬撒地】方阿萨德法师打发斯问问蒂芬撒地方阿萨德法师打发斯蒂。芬撒地方发送到发送到发送到发送到发送到发送，到发送到发送到发送到，发送111111123阿斯顿发阿斯顿发送到大。厦法定阿萨【德法师打发斯蒂芬撒地】方阿萨德法师打发斯问问蒂芬撒地方阿萨德法师打发斯蒂。芬撒地方发送到发送到发送到发送到发送到发送，到发送到发送到发送到，发送");
        sTextView.setParentClick(true);
        parent = findViewById(R.id.parent1);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "父View点击事件", Toast.LENGTH_SHORT).show();
            }
        });

        foldTextView = findViewById(R.id.text2);
        foldTextView.setText("111111123阿斯顿发阿斯顿发送到大。厦法定阿萨【德法师打发斯蒂芬撒地】方阿萨德法师打发斯问问蒂芬撒地方阿萨德法师打发斯蒂。芬撒地方发送到发送到发送到发送到发送到发送，到发送到发送到发送到，发送111111123阿斯顿发阿斯顿发送到大。厦法定阿萨【德法师打发斯蒂芬撒地】方阿萨德法师打发斯问问蒂芬撒地方阿萨德法师打发斯蒂。芬撒地方发送到发送到发送到发送到发送到发送，到发送到发送到发送到，发送");
        foldTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "textView点击事件", Toast.LENGTH_SHORT).show();
            }
        });
        fTextView = findViewById(R.id.text3);
        fTextView.setText("111111123阿斯顿发阿斯顿发送到大。厦法定阿萨【德法师打发斯蒂芬撒地】方阿萨德法师打发斯问问蒂芬撒地方阿萨德法师打发斯蒂。芬撒地方发送到发送到发送到发送到发送到发送，到发送到发送到发送到，发送111111123阿斯顿发阿斯顿发送到大。厦法定阿萨【德法师打发斯蒂芬撒地】方阿萨德法师打发斯问问蒂芬撒地方阿萨德法师打发斯蒂。芬撒地方发送到发送到发送到发送到发送到发送，到发送到发送到发送到，发送");
        parent2 = findViewById(R.id.parent3);
        parent2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "父View点击事件", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
