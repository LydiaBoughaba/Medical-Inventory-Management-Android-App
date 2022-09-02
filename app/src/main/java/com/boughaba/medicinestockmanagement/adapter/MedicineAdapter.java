package com.boughaba.medicinestockmanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boughaba.medicinestockmanagement.R;

import com.boughaba.medicinestockmanagement.room.model.Medicine;
import com.boughaba.medicinestockmanagement.ui.details.ViewDetails;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private List<Medicine> medicineList;
    private Context context;

    public MedicineAdapter() {
        medicineList = new ArrayList<>();
    }

    public void setMedicineList(List<Medicine> medicineList) {
        this.medicineList.clear();
        this.medicineList.addAll(medicineList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.medicine_item, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public Medicine getMedicineAt(int position) {
        return medicineList.get(position);
    }


    public class MedicineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_name)
        TextView textViewName;
        @BindView(R.id.tv_quantity)
        TextView textViewQuantity;
        @BindView(R.id.tv_price)
        TextView textViewPrice;


        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            textViewName.setText(medicineList.get(position).getName());
            textViewQuantity.setText("" + medicineList.get(position).getQuantity());
            textViewPrice.setText("" + medicineList.get(position).getPrice());
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Intent intent = new Intent(v.getContext(), ViewDetails.class);
            intent.putExtra("medicine", medicineList.get(position));
            v.getContext().startActivity(intent);
        }
    }
}


