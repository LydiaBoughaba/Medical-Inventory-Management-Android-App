package com.boughaba.medicinestockmanagement.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.boughaba.medicinestockmanagement.Constants;
import com.boughaba.medicinestockmanagement.R;
import com.boughaba.medicinestockmanagement.adapter.MedicineAdapter;
import com.boughaba.medicinestockmanagement.room.model.Medicine;
import com.boughaba.medicinestockmanagement.ui.add.AddActivity;
import com.boughaba.medicinestockmanagement.viewmodels.MedicineViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private MedicineViewModel viewModel;
    private MedicineAdapter adapter;

    @BindView(R.id.rv_med_list)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        disableDarkMode();
        init();
        getSupportActionBar().setTitle("Liste des médicaments");
    }

    public void init() {
        ButterKnife.bind(this);
        viewModel = new ViewModelProvider(this).get(MedicineViewModel.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MedicineAdapter();
        recyclerView.setAdapter(adapter);
        viewModel.getMedicines().observe(this, new Observer<List<Medicine>>() {
            @Override
            public void onChanged(List<Medicine> medicines) {
                adapter.setMedicineList(medicines);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT /*| ItemTouchHelper.RIGHT*/) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.delete(adapter.getMedicineAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @OnClick(R.id.bt_add)
    public void addClicked() {
        goToAddActivity();
    }

    public void goToAddActivity() {
        Intent intent = new Intent(this, AddActivity.class);
        startActivityForResult(intent, Constants.ADD_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ADD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Medicine medicine = (Medicine) data.getSerializableExtra("medicine");
                insertMedicine(medicine);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void insertMedicine(Medicine medicine) {
        viewModel.insert(medicine);
    }

    public void disableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}