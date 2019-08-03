package com.k1a2.schoolcalculator.view.recyclerview;

import android.content.Context;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.k1a2.schoolcalculator.R;
import com.k1a2.schoolcalculator.database.DatabaseKey;
import com.k1a2.schoolcalculator.database.ScoreDatabaseHelper;
import com.uni.unitor.unitorm2.view.recycler.listener.RecyclerItemClickListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class GradeRecyclerAdapter extends RecyclerView.Adapter<GradeRecyclerAdapter.ViewHolder> implements RecyclerItemClickListener.OnItemClickListener {

    private ArrayList<GradeRecyclerItem> listViewList = new  ArrayList<GradeRecyclerItem>();
    private int level = 0;
    private int grade = 0;
    private int itemHeight = 0;
    private String table = "";
    private ScoreDatabaseHelper scoreDatabaseHelper = null;

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
        holder.typeView.setSelection(item.getType());
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

        EditText subjectNameView = null;
        EditText gradeView = null;
        EditText pointView = null;
        ImageButton button_delete = null;
        Spinner typeView = null;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectNameView = itemView.findViewById(R.id.content_sunjectName);
            gradeView = itemView.findViewById(R.id.content_grade);
            pointView = itemView.findViewById(R.id.content_point);
            button_delete = itemView.findViewById(R.id.content_delete);
            typeView = itemView.findViewById(R.id.card_subject);

            subjectNameView.setText("국어");
            gradeView.setText("1");
            pointView.setText("1");
            typeView.setSelection(0);

            button_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int p = getAdapterPosition();
                    scoreDatabaseHelper.delete(String.valueOf(level)+String.valueOf(grade), getAdapterPosition());
                    removeItem(p);
                    button_delete.setActivated(false);
                }
            });

            final Context context = itemView.getContext();

            subjectNameView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    onScoreEdit(context, DatabaseKey.KEY_VALUE_SUBJECT, editable.toString(), getAdapterPosition());
                    listViewList.get(getAdapterPosition()).setSubjectName(editable.toString());
                }
            });
            gradeView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    onScoreEdit(context, DatabaseKey.KEY_VALUE_GRADE, editable.toString(), getAdapterPosition());
                    listViewList.get(getAdapterPosition()).setRank(editable.toString());
                }
            });
            pointView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    onScoreEdit(context, DatabaseKey.KEY_VALUE_POINT, editable.toString(), getAdapterPosition());
                    listViewList.get(getAdapterPosition()).setPoint(editable.toString());
                }
            });
            typeView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    onScoreEdit(context, DatabaseKey.KEY_VALUE_TYPE, String.valueOf(i), getAdapterPosition());
                    listViewList.get(getAdapterPosition()).setType(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    private synchronized void onScoreEdit(Context context, String type, String value, int position) {
        table = String.valueOf(level)+String.valueOf(grade);
        if (scoreDatabaseHelper == null) {
            scoreDatabaseHelper = new ScoreDatabaseHelper(context, DatabaseKey.KEY_DB_NAME, null, 1);
        }
        if (!scoreDatabaseHelper.isExisit(table, position)) {
            insertDatabase(position, type, value);
        } else {
            updateDatabase(type, value, position);
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

    public void setLevel(int level) {
        this.level = level;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    private void insertDatabase(int position, String edit_subject, int edit_grade, int edit_point, int type) {
        scoreDatabaseHelper.insert(String.valueOf(level)+String.valueOf(grade), edit_subject, edit_grade, edit_point, type, position);
    }

    private void insertDatabase(int position, String type, String value) {
        switch (type) {
            case DatabaseKey.KEY_VALUE_SUBJECT: {
                insertDatabase(position, value, 1, 1, 0);
                break;
            }
            case DatabaseKey.KEY_VALUE_GRADE: {
                if (value.isEmpty()) {
                    value = "1";
                }
                insertDatabase(position, "", Integer.parseInt(value), 1, 0);
                break;
            }
            case DatabaseKey.KEY_VALUE_POINT: {
                if (value.isEmpty()) {
                    value = "1";
                }
                insertDatabase(position, "", 1, Integer.parseInt(value), 0);
                break;
            }
            case DatabaseKey.KEY_VALUE_TYPE: {
                if (value.isEmpty()) {
                    value = "0";
                }
                insertDatabase(position, "", 1, 1, Integer.parseInt(value));
                break;
            }
            case DatabaseKey.KEY_VALUE_POSITION: {
                break;
            }
        }
    }

    private void updateDatabase(String type, String value, int position) {
        scoreDatabaseHelper.update(String.valueOf(level)+String.valueOf(grade), type, value, position);
    }

    public void clearItem() {
        final int count = listViewList.size();
        listViewList.clear();
        notifyItemRangeRemoved(0, count);
    }
}
