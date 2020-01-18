package com.k1a2.schoolcalculator.view.recyclerview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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

/**성적 추가/삭제 리스트 아이탬 관리 어댑터
 * 절대 수정금지**/

public class AskRecyclerAdapter extends RecyclerView.Adapter<AskRecyclerAdapter.ViewHolder> implements RecyclerItemClickListener.OnItemClickListener {

    private ArrayList<AskRecyclerItem> listViewList = new  ArrayList<AskRecyclerItem>();
    private int itemHeight = 0;
    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_explain, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AskRecyclerItem item = listViewList.get(position);
        holder.linearLayout.setBackgroundColor(item.getColor());
        holder.textTitle.setText(item.getTitle());
        holder.textSub.setText(item.getSubtitle());
        holder.image.setImageDrawable(item.getImage());
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

        LinearLayout linearLayout = null;
        TextView textTitle = null;
        TextView textSub = null;
        ImageView image = null;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            linearLayout = itemView.findViewById(R.id.view_card_back_layout);
            textTitle = itemView.findViewById(R.id.view_card_text_title);
            textSub = itemView.findViewById(R.id.view_card_text_sub);
            image = itemView.findViewById(R.id.view_card_image);
        }
    }

    public void addItem(AskRecyclerItem gradeRecyclerItem) {
        listViewList.add(gradeRecyclerItem);
        notifyItemInserted(listViewList.size());
    }

    public void removeItem(int position) {
        if (position != -1) {
            listViewList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public AskRecyclerItem getItem(int position) {
        return listViewList.get(position);
    }

    public void clearItem() {
        final int count = listViewList.size();
        listViewList.clear();
        notifyItemRangeRemoved(0, count);
    }
}
