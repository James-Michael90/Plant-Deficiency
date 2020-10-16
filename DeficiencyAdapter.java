package com.visione.amndd.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visione.amndd.R;
import com.visione.amndd.data.Deficiency;
import com.visione.amndd.data.DeficiencyDBHelper;
import com.visione.amndd.utils.ImageByteArray;

import java.util.List;

public class DeficiencyAdapter extends RecyclerView.Adapter<DeficiencyAdapter.ViewHolder> {
    private List<Deficiency> deficiencyList;
    private Context context;
    private DeficiencyDBHelper dbHelper;

    public DeficiencyAdapter(List<Deficiency> data, Context context) {
        this.deficiencyList = data;
        this.context = context;
        dbHelper = new DeficiencyDBHelper(context);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        ImageView thumbnail;
        CheckBox checkDiagnosed;
        TextView deficiencyText, solutionText;
        ImageView expand, collapse;

        ViewHolder(View itemView) {
            super(itemView);

            this.checkDiagnosed = itemView.findViewById(R.id.check_diagnosed);
            this.thumbnail = itemView.findViewById(R.id.image);
            this.deficiencyText = itemView.findViewById(R.id.deficiency);

            this.solutionText = itemView.findViewById(R.id.solution);
            this.expand = itemView.findViewById(R.id.expand);
            this.collapse = itemView.findViewById(R.id.collapse);

            expand.setOnClickListener(view -> {
                collapse.setVisibility(View.VISIBLE);
                expand.setVisibility(View.GONE);
                checkDiagnosed.setVisibility(View.VISIBLE);

            });


            collapse.setOnClickListener(view -> {
                collapse.setVisibility(View.GONE);
                expand.setVisibility(View.VISIBLE);
                checkDiagnosed.setVisibility(View.GONE);


            });

            checkDiagnosed.setOnCheckedChangeListener(this);

        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            Deficiency d = deficiencyList.get(getAdapterPosition());
            if (b) {
                d.setDiagnosed("Yes");
                if (dbHelper.updateDeficiency(d) > -1) {
                    //Toast.makeText(context, "Deficiency Updated", Toast.LENGTH_SHORT).show();
                    checkDiagnosed.setClickable(false);
                    checkDiagnosed.setAlpha(0.3f);
                } else
                    Toast.makeText(context, "Failed to update deficiency", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @NonNull
    @Override
    public DeficiencyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deficiency_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeficiencyAdapter.ViewHolder holder, final int position) {
        Deficiency def = deficiencyList.get(position);

        updateUI(holder, def);

    }

    @Override
    public int getItemCount() {
        return deficiencyList.size();
    }

    private void updateUI(ViewHolder holder, Deficiency def) {

        String deficiency = def.getDeficiency();
        String solution = def.getSolution();
        String diagnosed = def.getDiagnosed();
        Bitmap bitmap = ImageByteArray.convertByteArrayBitmap(def.getImage());
        holder.deficiencyText.setText(deficiency);
        holder.solutionText.setText(solution);
        holder.thumbnail.setImageBitmap(bitmap);


        if (diagnosed.equalsIgnoreCase("Yes")) {
            holder.checkDiagnosed.setChecked(true);
            holder.checkDiagnosed.setClickable(false);
            holder.checkDiagnosed.setAlpha(0.3f);
        }
        if (diagnosed.equalsIgnoreCase("No"))
            holder.checkDiagnosed.setChecked(false);


    }
}
