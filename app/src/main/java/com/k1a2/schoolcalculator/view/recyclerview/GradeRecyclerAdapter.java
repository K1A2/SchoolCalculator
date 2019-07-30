package com.k1a2.schoolcalculator.view.recyclerview;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.k1a2.schoolcalculator.R;
import com.uni.unitor.unitorm2.view.recycler.listener.RecyclerItemClickListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class GradeRecyclerAdapter extends RecyclerView.Adapter<GradeRecyclerAdapter.ViewHolder> implements RecyclerItemClickListener.OnItemClickListener
{

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

        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long downTime = SystemClock.uptimeMillis();
                long eventTime = SystemClock.uptimeMillis();
                MotionEvent down_event = MotionEvent.obtain(downTime, eventTime,   MotionEvent.ACTION_DOWN, 0,0, 0);
                MotionEvent up_event = MotionEvent.obtain(downTime, eventTime,   MotionEvent.ACTION_UP, 0, 0, 0);
                holder.itemView.dispatchTouchEvent(down_event);
                holder.itemView.dispatchTouchEvent(up_event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listViewList.size();
    }

    @Override
    public void onItemClicked(@NotNull View view, int position) {
        Toast.makeText(view.getContext(), position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongItemClicked(@Nullable View view, int position) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        EditText subjectNameView = null;
        EditText gradeView = null;
        EditText pointView = null;
        ImageButton button_delete = null;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectNameView = itemView.findViewById(R.id.content_sunjectName);
            gradeView = itemView.findViewById(R.id.content_grade);
            pointView = itemView.findViewById(R.id.content_point);
            button_delete = itemView.findViewById(R.id.content_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int p = getAdapterPosition();
                    removeItem(p);
                }
            });
        }
    }

    public void addItem(GradeRecyclerItem gradeRecyclerItem) {
        listViewList.add(gradeRecyclerItem);
        notifyItemInserted(listViewList.size());
    }

    public void removeItem(int position) {
        listViewList.remove(position);
        notifyItemRemoved(position);
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
