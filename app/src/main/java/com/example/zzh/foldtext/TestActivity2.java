package com.example.zzh.foldtext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TestActivity2 extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        TextView textView = findViewById(R.id.textView);
        textView.setText("测试一个非常长的数据测试一个非常长的数据测试一个非常长的数据测试一个非常长的数据测试一个非常长的数据测试一个非常长的数据测试一个非常长的数据测试一个非常长的数据测试一个非常长的数据测试一个非常长的数据测试一个非常长的数据测试一个非常长的数据测试一个非常长的数据测试一个非常长的数据测试一个非常长的数据测试一个非常长的数据测试一个非常长的数据测试一个非常长的数据测试一个非常长的数据测试一个非常长的数据测试一个非常长的数据测试一个非常长的数据");
        recyclerView = findViewById(R.id.recyclerView);
        List<Bean> mList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Bean bean = new Bean();
            if ((i & 1) == 0) {
                bean.setTitle("阿斯顿发送到发送到发斯蒂芬金坷垃圣诞节疯狂了圣诞节疯狂链接阿萨德开了房进口量将阿斯顿发送到发送到发斯蒂芬金坷垃圣诞节疯狂了圣诞节疯狂链接阿萨德开了房进口量将阿萨德的撒是的方法大士大夫撒旦法" + i);
            } else {
                bean.setTitle("阿斯顿发送到发送到发斯蒂芬金坷垃圣诞节疯狂了圣诞节疯狂链接阿萨德开了房进口量将" + i);
            }
            mList.add(bean);
        }
        TestAdapter2 mAdapter = new TestAdapter2(this, mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }
}
