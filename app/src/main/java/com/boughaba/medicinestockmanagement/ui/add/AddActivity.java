package com.boughaba.medicinestockmanagement.ui.add;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.boughaba.medicinestockmanagement.Constants;
import com.boughaba.medicinestockmanagement.R;
import com.boughaba.medicinestockmanagement.camera.barcode;
import com.boughaba.medicinestockmanagement.room.model.Medicine;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class AddActivity extends AppCompatActivity {
    private static final String TAG = "AddActivity";
    @BindView(R.id.tf_name)
    TextInputLayout editTextName;
    @BindView(R.id.tf_classTh)
    TextInputLayout editTextClassTh;
    @BindView(R.id.tf_laboratory)
    TextInputLayout editTextLaboratory;
    @BindView(R.id.tf_denomination)
    TextInputLayout editTextDenomination;
    @BindView(R.id.tf_form_medicine)
    TextInputLayout editTextFormMedicine;
    @BindView(R.id.tf_duration)
    TextInputLayout editTextDuration;
    @BindView(R.id.tf_refundable)
    TextInputLayout editTextRefundable;
    @BindView(R.id.tf_lot)
    TextInputLayout editTextLot;
    @BindView(R.id.tf_manufactureDate)
    TextInputLayout editTextManufactureDate;
    @BindView(R.id.tf_expirationDate)
    TextInputLayout editTextExpirationDate;
    @BindView(R.id.tf_description)
    TextInputLayout editTextDescription;
    @BindView(R.id.tf_quantity)
    TextInputLayout editTextQuantity;
    @BindView(R.id.tf_price)
    TextInputLayout editTextPrice;
    @BindView(R.id.dropdown_refundable)
    AutoCompleteTextView dropdownRefundable;
    @BindView(R.id.tf_barcode)
    TextInputLayout barcode;

    private boolean refundable;
    private int field;
    private Bitmap imageBitmap;

    private DatePickerDialog.OnDateSetListener setListener;
    private DatePickerDialog.OnDateSetListener setListener1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initManufactureDate();
        initExpirationDate();
        initBarCode();
        initName();
        initPrice();

        String[] itemsRefundable = new String[]{"Remboursable", "Non remboursable"};
        ArrayAdapter<String> adapterRefundable = new ArrayAdapter<>(
                this, R.layout.dropdown_item_refundable, itemsRefundable
        );
        dropdownRefundable.setAdapter(adapterRefundable);
        dropdownRefundable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "onItemClick: ");
                if (position == 0) {
                    refundable = true;
                } else {
                    refundable = false;
                }
            }
        });
    }

    @OnClick(R.id.bt_ok)
    void okClicked() {
        Intent intent = new Intent();
        Medicine medicine = new Medicine();
        medicine = setMedicine();
        System.out.println(editTextRefundable.getEditText().getText());
        intent.putExtra("medicine", medicine);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.bt_cancel)
    void cancelClicked() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private Medicine setMedicine() {
        Medicine m = new Medicine();
        m.setName(editTextName.getEditText().getText().toString());
        m.setClassTh(editTextClassTh.getEditText().getText().toString());
        m.setLaboratory(editTextLaboratory.getEditText().getText().toString());
        m.setDenomination(editTextDenomination.getEditText().getText().toString());
        m.setFormMedicine(editTextFormMedicine.getEditText().getText().toString());
        m.setConservationDuration(Integer.parseInt(editTextDuration.getEditText().getText().toString()));
        m.setLot(editTextLot.getEditText().getText().toString());
        m.setDescription(editTextDescription.getEditText().getText().toString());
        m.setQuantity(Integer.parseInt(editTextQuantity.getEditText().getText().toString()));
        m.setPrice(Double.parseDouble(editTextPrice.getEditText().getText().toString()));
        m.setManufactureDate(convertStringToDate(editTextManufactureDate.getEditText().getText().toString()));
        m.setExpirationDate(convertStringToDate(editTextExpirationDate.getEditText().getText().toString()));
        m.setRefundable(refundable);
        m.setBarCode(barcode.getEditText().getText().toString());
        return m;
    }

    private Date convertStringToDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private void initName() {
        editTextName.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                field = 1;
                launchCamera();
            }
        });
    }

    private void initPrice() {
        editTextPrice.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                field = 2;
                launchCamera();
            }
        });
    }

    private void initBarCode() {
        barcode.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scannerBarcode();
            }
        });
    }

    private void textDetection(int field) {
        InputImage image = InputImage.fromBitmap(imageBitmap, 0);
        TextRecognizer recognizer = TextRecognition.getClient();
        recognizer.process(image)
                .addOnSuccessListener(
                        new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text text) {
                                String blockText = null;
                                for (Text.TextBlock block : text.getTextBlocks()) {
                                    blockText = block.getText();
                                }
                                if (field == 1) {
                                    editTextName.getEditText().setText(blockText);
                                } else {
                                    editTextPrice.getEditText().setText(blockText);
                                }
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                e.printStackTrace();
                            }
                        });
    }

    private void scannerBarcode() {
        IntentIntegrator intent = new IntentIntegrator(this);
        intent.setCaptureActivity(barcode.class);
        intent.setOrientationLocked(false);
        intent.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intent.setPrompt("Scanning code");
        intent.initiateScan();
    }

    //launch the camera
    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //capture d'image
        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            //if it's for the name or the price
            textDetection(field);
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
        }
        //Barcode
        else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            barcode.getEditText().setText(result.getContents().toString());
        }
    }

    private void initManufactureDate() {
        Calendar calender = Calendar.getInstance();
        final int year = calender.get(Calendar.YEAR);
        final int month = calender.get(Calendar.MONTH);
        final int day = calender.get(Calendar.DAY_OF_MONTH);

        editTextManufactureDate.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        , setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = " " + dayOfMonth + "-" + month + "-" + year;
                editTextManufactureDate.getEditText().setText(date);
            }
        };
    }

    private void initExpirationDate() {
        Calendar calender = Calendar.getInstance();
        final int year = calender.get(Calendar.YEAR);
        final int month = calender.get(Calendar.MONTH);
        final int day = calender.get(Calendar.DAY_OF_MONTH);

        editTextExpirationDate.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        , setListener1, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = "" + dayOfMonth + "-" + month + "-" + year;
                editTextExpirationDate.getEditText().setText(date);
            }
        };
    }
}
