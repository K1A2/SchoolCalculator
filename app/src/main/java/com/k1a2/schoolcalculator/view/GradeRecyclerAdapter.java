package com.k1a2.schoolcalculator.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.k1a2.schoolcalculator.R;

import java.util.ArrayList;

public class GradeRecyclerAdapter extends RecyclerView.Adapter<GradeRecyclerAdapter.ViewHolder> {

    private ArrayList<GradeRecyclerItem> listViewList = new  ArrayList<GradeRecyclerItem>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final GradeRecyclerItem item = listViewList.get(position);
        holder.subjectNameView.setText(item.getSubjectName());
        holder.gradeView.setText(item.getRank());
        holder.pointView.setText(item.getPoint());
    }

    @Override
    public int getItemCount() {
        return listViewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        EditText subjectNameView = null;
        EditText gradeView = null;
        EditText pointView = null;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectNameView = itemView.findViewById(R.id.content_sunjectName);
            gradeView = itemView.findViewById(R.id.content_grade);
            pointView = itemView.findViewById(R.id.content_point);
        }
    }

    public void addItem(GradeRecyclerItem gradeRecyclerItem) {
        listViewList.add(gradeRecyclerItem);
        notifyItemInserted(listViewList.size());
    }

    public GradeRecyclerItem getItem(int position) {
        return listViewList.get(position);
    }

    public void clearItem() {
        final int count = listViewList.size();
        listViewList.clear();
        notifyItemRangeRemoved(0, count);
    }
}
