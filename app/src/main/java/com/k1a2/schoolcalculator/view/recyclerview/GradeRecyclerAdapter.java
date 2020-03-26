package com.k1a2.schoolcalculator.view.recyclerview;

import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.SystemClock;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.k1a2.schoolcalculator.R;
import com.k1a2.schoolcalculator.activity.MainActivity;
import com.k1a2.schoolcalculator.database.DatabaseKey;
import com.k1a2.schoolcalculator.database.ScoreDatabaseHelper;
import com.k1a2.schoolcalculator.sharedpreference.PreferenceKey;
import com.uni.unitor.unitorm2.view.recycler.listener.RecyclerItemClickListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**성적 추가/삭제 리스트 아이탬 관리 어댑터
 * 절대 수정금지**/

public class GradeRecyclerAdapter extends RecyclerView.Adapter<GradeRecyclerAdapter.ViewHolder> implements RecyclerItemClickListener.OnItemClickListener {

    private ArrayList<GradeRecyclerItem> listViewList = new  ArrayList<GradeRecyclerItem>();
    private int level = 0;
    private int grade = 0;
    private int itemHeight = 0;
    private String table = "";
    private ScoreDatabaseHelper scoreDatabaseHelper = null;
    private Context context;

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
        holder.typeView.setSelection(item.getType());
        holder.pointButton.setText(item.getPoint());
        holder.gradeButton.setText(item.getRank());
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
        ImageButton button_delete = null;
        Spinner typeView = null;
        Button gradeButton = null;
        Button pointButton = null;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            subjectNameView = itemView.findViewById(R.id.content_sunjectName);
            button_delete = itemView.findViewById(R.id.content_delete);
            typeView = itemView.findViewById(R.id.card_subject);
            gradeButton = itemView.findViewById(R.id.content_grade_picker);
            pointButton = itemView.findViewById(R.id.content_point_picker);

            gradeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.PickerAlertDialog);
                    final View layout = ((AppCompatActivity)context).getLayoutInflater().inflate(R.layout.dialog_nimberpicker, null,  false);
                    final NumberPicker numberPicker = layout.findViewById(R.id.dialog_numberpicker);
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>등급 선택</font>"));
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>해당 과목의 등급을 선택해주세요.</font>"));
                    numberPicker.setMinValue(1);
                    numberPicker.setMaxValue(9);
                    numberPicker.setValue(Integer.parseInt(gradeButton.getText().toString()));
                    numberPicker.setWrapSelectorWheel(false);
                    builder.setView(layout);
                    builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onScoreEditPicker((Button) v, context, DatabaseKey.KEY_VALUE_GRADE, String.valueOf(numberPicker.getValue()), getAdapterPosition());
                        }
                    });
                    builder.setNegativeButton("취소", null);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_dialog_rate));
                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
//                            Resources resources = alertDialog.getContext().getResources();
//                            int alertTitleId = resources.getIdentifier("alertTitle", "id", "android");
//                            int alertMessageId = resources.getIdentifier("alertMessage", "id", "android");
//                            final TextView alertTitle = (TextView) alertDialog.getWindow().getDecorView().findViewById(alertTitleId);
//                            final TextView alertMessage = (TextView) alertDialog.getWindow().getDecorView().findViewById(alertMessageId);
//                            alertTitle.setTextColor(Color.WHITE);
//                            alertMessage.setTextColor(Color.WHITE);
                            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.rgb(46, 144, 242));
                            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.rgb(255, 110, 158));
                            //l.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(Color.rgb(255, 255, 255));
                        }
                    });
                    alertDialog.show();
                }
            });
            pointButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.PickerAlertDialog);
                    final View layout = ((AppCompatActivity)context).getLayoutInflater().inflate(R.layout.dialog_nimberpicker, null, false);
                    final NumberPicker numberPicker = layout.findViewById(R.id.dialog_numberpicker);
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>해당 과목의 단위수을 선택해주세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>단위수 선택</font>"));
                    numberPicker.setMinValue(1);
                    numberPicker.setMaxValue(150);
                    numberPicker.setValue(Integer.parseInt(pointButton.getText().toString()));
                    builder.setView(layout);
                    builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onScoreEditPicker((Button) v, context, DatabaseKey.KEY_VALUE_POINT, String.valueOf(numberPicker.getValue()), getAdapterPosition());
                        }
                    });
                    builder.setNegativeButton("취소", null);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_dialog_rate));
                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.rgb(46, 144, 242));
                            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.rgb(255, 110, 158));
                            //l.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(Color.rgb(255, 255, 255));
                        }
                    });
                    alertDialog.show();
                }
            });

            gradeButton.setText("1");
            pointButton.setText("1");

            subjectNameView.setText("국어");
            typeView.setSelection(0);

            ArrayAdapter<String> a = new ArrayAdapter<String>(itemView.getContext(), R.layout.spinner_background, itemView.getContext().getResources().getStringArray(R.array.subject));
            typeView.setAdapter(a);

            button_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder a = new AlertDialog.Builder(itemView.getContext());
                    a.setTitle(String.format("\'%s\'를 삭제하시겠습니까?", subjectNameView.getText().toString()));
                    a.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final int p = getAdapterPosition();
                            scoreDatabaseHelper.delete(String.valueOf(level)+String.valueOf(grade), getAdapterPosition());
                            removeItem(p);
                            button_delete.setActivated(false);
                        }
                    });
                    a.setNegativeButton("취소", null);
                    a.setMessage(String.format("성적에서 \'%s\'를 삭제하시겠습니까?", subjectNameView.getText().toString()));
                    final AlertDialog alertDialog = a.create();
                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.rgb(235, 64, 52));
                        }
                    });
                    alertDialog.show();
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
                    onScoreEdit(subjectNameView, context, DatabaseKey.KEY_VALUE_SUBJECT, editable.toString(), getAdapterPosition());
                    listViewList.get(getAdapterPosition()).setSubjectName(editable.toString());
                }
            });
            typeView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    onScoreEdit(null, context, DatabaseKey.KEY_VALUE_TYPE, String.valueOf(i), getAdapterPosition());
                    listViewList.get(getAdapterPosition()).setType(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

//    final View.OnClickListener pickerClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            final View layout = ((AppCompatActivity)context).getLayoutInflater().inflate(R.layout.dialog_nimberpicker, null, false);
//            final NumberPicker numberPicker = layout.findViewById(R.id.dialog_numberpicker);
//            switch (v.getId()) {
//                case R.id.content_grade_picker: {
//                    builder.setTitle("등급 선택");
//                    builder.setMessage("해당 과목의 등급을 선택해주세요.");
//                    numberPicker.setMinValue(1);
//                    numberPicker.setMaxValue(9);
//                    numberPicker.setWrapSelectorWheel(false);
//                    break;
//                }
//                case R.id.content_point_picker: {
//                    builder.setMessage("해당 과목의 단위수을 선택해주세요.");
//                    builder.setTitle("단위수 선택");
//                    numberPicker.setMinValue(1);
//                    numberPicker.setMaxValue(150);
//                    break;
//                }
//            }
//            builder.setView(layout);
//            builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    switch (v.getId()) {
//                        case R.id.content_grade_picker: {
//                            onScoreEdit((Button) v, context, DatabaseKey.KEY_VALUE_GRADE, ((Button) v).getText(), getAdapterPosition());
//                            break;
//                        }
//                        case R.id.content_point_picker: {
//                            break;
//                        }
//                    }
//                }
//            });
//            builder.setNegativeButton("취소", null);
//            final AlertDialog alertDialog = builder.create();
//            alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_dialog_rate));
//            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                @Override
//                public void onShow(DialogInterface dialogInterface) {
//                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.rgb(46, 144, 242));
//                    alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.rgb(255, 110, 158));
//                    //l.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(Color.rgb(255, 255, 255));
//                }
//            });
//            alertDialog.show();
//        }
//    };

    private synchronized void onScoreEdit(EditText editText, Context context, String type, String value, int position) {
        table = String.valueOf(level)+String.valueOf(grade);
        if (scoreDatabaseHelper == null) {
            scoreDatabaseHelper = new ScoreDatabaseHelper(context, DatabaseKey.KEY_DB_NAME, null, 1);
        }
        if (!scoreDatabaseHelper.isExisit(table, position)) {
            insertDatabase(position, type, value);
        } else {
            updateDatabase(editText, type, value, position);//TODO
        }
    }

    private synchronized void onScoreEditPicker(Button button, Context context, String type, String value, int position) {
        table = String.valueOf(level)+String.valueOf(grade);
        if (scoreDatabaseHelper == null) {
            scoreDatabaseHelper = new ScoreDatabaseHelper(context, DatabaseKey.KEY_DB_NAME, null, 1);
        }
        if (!scoreDatabaseHelper.isExisit(table, position)) {
            insertDatabase(position, type, value);
        } else {
            updateDatabase(button, type, value, position);//TODO
        }
    }

    public void addItem(GradeRecyclerItem gradeRecyclerItem) {
        listViewList.add(gradeRecyclerItem);
        notifyItemInserted(listViewList.size());
    }

    public void removeItem(int position) {
        if (position != -1) {
            listViewList.remove(position);
            notifyItemRemoved(position);
        }
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
                if (Double.parseDouble(value) <= Integer.MAX_VALUE) {
                    if (value.isEmpty()) {
                        value = "1";
                    }
                    insertDatabase(position, "", Integer.parseInt(value), 1, 0);
                } else {
                    Toast.makeText(context, "숫자가 " + String.valueOf(Integer.MAX_VALUE) + "보다 작아야 합니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case DatabaseKey.KEY_VALUE_POINT: {
                if (Double.parseDouble(value) <= Integer.MAX_VALUE) {
                    if (value.isEmpty()) {
                        value = "1";
                    }
                    insertDatabase(position, "", 1, Integer.parseInt(value), 0);
                } else {
                    Toast.makeText(context, "숫자가 " + String.valueOf(Integer.MAX_VALUE) + "보다 작아야 합니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case DatabaseKey.KEY_VALUE_TYPE: {
                if (Double.parseDouble(value) <= Integer.MAX_VALUE) {
                    if (value.isEmpty()) {
                        value = "0";
                    }
                    insertDatabase(position, "", 1, 1, Integer.parseInt(value));
                } else {
                    Toast.makeText(context, "숫자가 " + String.valueOf(Integer.MAX_VALUE) + "보다 작아야 합니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case DatabaseKey.KEY_VALUE_POSITION: {
                break;
            }
        }
    }

    private void updateDatabase(EditText editText, String type, String value, int position) {
        switch (type) {
            case DatabaseKey.KEY_VALUE_SUBJECT: {
                scoreDatabaseHelper.update(String.valueOf(level)+String.valueOf(grade), type, value, position);
                break;
            }
            default: {
                if (value.isEmpty()) {
                    scoreDatabaseHelper.update(String.valueOf(level)+String.valueOf(grade), type, value, position);
                } else {
                    if (Double.parseDouble(value) <= Integer.MAX_VALUE) {
                        scoreDatabaseHelper.update(String.valueOf(level)+String.valueOf(grade), type, value, position);
                    } else {
                        Toast.makeText(context, "숫자가 " + String.valueOf(Integer.MAX_VALUE) + "보다 작아야 합니다.", Toast.LENGTH_SHORT).show();
                        if (editText != null) {
                            editText.setText("1");
                        }
                    }
                }
                break;
            }
        }
    }

    private void updateDatabase(Button button, String type, String value, int position) {
        switch (type) {
            case DatabaseKey.KEY_VALUE_SUBJECT: {
                scoreDatabaseHelper.update(String.valueOf(level)+String.valueOf(grade), type, value, position);
                break;
            }
            default: {
                button.setText(value);
                scoreDatabaseHelper.update(String.valueOf(level)+String.valueOf(grade), type, value, position);
//                if (value.isEmpty()) {
//                    scoreDatabaseHelper.update(String.valueOf(level)+String.valueOf(grade), type, value, position);
//                } else {
//                    if (Double.parseDouble(value) <= Integer.MAX_VALUE) {
//                        scoreDatabaseHelper.update(String.valueOf(level)+String.valueOf(grade), type, value, position);
//                    } else {
//                        Toast.makeText(context, "숫자가 " + String.valueOf(Integer.MAX_VALUE) + "보다 작아야 합니다.", Toast.LENGTH_SHORT).show();
//                        if (button != null) {
//                            button.setText("1");
//                        }
//                    }
//                }
                break;
            }
        }
    }

    public void clearItem() {
        final int count = listViewList.size();
        listViewList.clear();
        notifyItemRangeRemoved(0, count);
    }
}
