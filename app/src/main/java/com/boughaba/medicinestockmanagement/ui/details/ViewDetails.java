package com.boughaba.medicinestockmanagement.ui.details;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.boughaba.medicinestockmanagement.R;
import com.boughaba.medicinestockmanagement.room.model.Medicine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewDetails extends AppCompatActivity {
    @BindView(R.id.tv_name_details)
    TextView name;
    @BindView(R.id.tv_classTh)
    TextView classTh;
    @BindView(R.id.tv_laboratory)
    TextView laboratory;
    @BindView(R.id.tv_denomination)
    TextView denomination;
    @BindView(R.id.tv_form_medicine)
    TextView formMedicine;
    @BindView(R.id.tv_duration)
    TextView duration;
    @BindView(R.id.tv_refundable)
    TextView refundable;
    @BindView(R.id.tv_lot)
    TextView lot;
    @BindView(R.id.tv_manufactureDate)
    TextView manufactureDate;
    @BindView(R.id.tv_expirationDate)
    TextView expirationDate;
    @BindView(R.id.tv_description)
    TextView description;
    @BindView(R.id.tv_quantity_details)
    TextView quantity;
    @BindView(R.id.tv_price_details)
    TextView price;
    @BindView(R.id.tv_bar_code)
    TextView barCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        Medicine medicine = new Medicine();
        medicine = (Medicine) extras.getSerializable("medicine");
        initView(medicine);
    }

    public void initView(Medicine medicine) {
        name.setText(medicine.getName());
        price.setText("" + medicine.getPrice());
        quantity.setText("" + medicine.getQuantity());
        classTh.setText("" + medicine.getClassTh());
        laboratory.setText("" + medicine.getLaboratory());
        denomination.setText("" + medicine.getDenomination());
        formMedicine.setText("" + medicine.getFormMedicine());
        duration.setText("" + medicine.getConservationDuration());
        if (medicine.getRefundable() == true) {
            refundable.setText("Remboursable");
        } else if (medicine.getRefundable() == false) {
            refundable.setText("Non remboursable");
        }
        lot.setText("" + medicine.getLot());
        manufactureDate.setText("" + convertDateToString(medicine.getManufactureDate()));
        expirationDate.setText("" + convertDateToString(medicine.getExpirationDate()));
        description.setText("" + medicine.getDescription());
        barCode.setText("" + medicine.getBarCode());
    }

    private String convertDateToString(Date date) {
        String stringDate = "";
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        stringDate = dateFormat.format(date);
        return stringDate;
    }
}
