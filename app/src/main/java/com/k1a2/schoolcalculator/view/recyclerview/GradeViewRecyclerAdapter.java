package com.k1a2.schoolcalculator.view.recyclerview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.k1a2.schoolcalculator.R;
import com.k1a2.schoolcalculator.activity.ActivityKey;
import com.k1a2.schoolcalculator.activity.MainActivity;
import com.k1a2.schoolcalculator.activity.ScoreEditActivity;
import com.k1a2.schoolcalculator.database.DatabaseKey;
import com.k1a2.schoolcalculator.database.ScoreDatabaseHelper;
import com.uni.unitor.unitorm2.view.recycler.listener.RecyclerItemClickListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**성적 추가/삭제 리스트 아이탬 관리 어댑터
 * 절대 수정금지**/

public class GradeViewRecyclerAdapter extends RecyclerView.Adapter<GradeViewRecyclerAdapter.ViewHolder> implements RecyclerItemClickListener.OnItemClickListener {

    private ArrayList<GradeViewItem> listViewList = new  ArrayList<GradeViewItem>();
    private int itemHeight = 0;
    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_grade_all, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final GradeViewItem item = listViewList.get(position);
        holder.gradeTitleText.setText(item.getGradeTitle());
        holder.grade1Text.setText(String.valueOf(item.getGrade1()));
        holder.grade2Text.setText(String.valueOf(item.getGrade2()));
        holder.gradeEditButton.setText((position + 1) + "학년 성적 입력하기");
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

    public int getItemHeight() {
        return itemHeight;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView gradeTitleText = null;
        TextView grade1Text = null;
        TextView grade2Text = null;
        Button gradeEditButton = null;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            gradeTitleText = itemView.findViewById(R.id.main_grade_title);
            grade1Text = itemView.findViewById(R.id.main_1text);
            grade2Text = itemView.findViewById(R.id.main_2text);
            gradeEditButton = itemView.findViewById(R.id.main_button_editScore);

            gradeEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (getAdapterPosition()) {
                        case 0: {
                            ((MainActivity) context).startActivityScore(1);
//                            startScoreEditAvtivity(1);
                            break;
                        }
                        case 1: {
                            ((MainActivity) context).startActivityScore(2);
//                            startScoreEditAvtivity(2);
                            break;
                        }
                        case 2: {
                            ((MainActivity) context).startActivityScore(3);
//                            startScoreEditAvtivity(3);
                            break;
                        }
                    }
                }
            });
        }
    }

    //성적 입력하기 액티비티 띄우는 함수
    private void startScoreEditAvtivity(Integer mode) {//mode값에 따라 몇학년 탭을 보여줄지 결정
        Intent intent = new Intent(context, ScoreEditActivity.class);
        switch (mode) {
            case 0: {//all
                intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_ALL);//1학년
                break;
            }
            case 1: {//1
                intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_FIRST);//1학년
                break;
            }
            case 2: {//2
                intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_SECOND);//2학년
                break;
            }
            case 3: {//3
                intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_THIRD);//3학년
                break;
            }
        }

        context.startActivity(intent);
    }

    public void addItem(GradeViewItem gradeRecyclerItem) {
        listViewList.add(gradeRecyclerItem);
        notifyItemInserted(listViewList.size());
    }

    public void removeItem(int position) {
        if (position != -1) {
            listViewList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public GradeViewItem getItem(int position) {
        return listViewList.get(position);
    }

    public void clearItem() {
        final int count = listViewList.size();
        listViewList.clear();
        notifyItemRangeRemoved(0, count);
    }
}
