package com.example.zzh.foldtext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
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
        TestAdapter mAdapter = new TestAdapter(this, mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }
}
