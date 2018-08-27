package com.example.zzh.foldtext;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by zhangzhihao on 2018/8/27 10:46.
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {
    private Context context;
    private List<Bean> mList;

    public TestAdapter(Context context, List<Bean> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_test, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Bean bean = mList.get(position);
        holder.textView.setExpand(bean.isExpand())
                .setOnTipClickListener(new FoldTextView.onTipClickListener() {
                    @Override
                    public void onTipClick(boolean flag) {
                        bean.setExpand(flag);
                    }
                });
        holder.textView.setText(bean.getTitle());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        FoldTextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
        }
    }
}
