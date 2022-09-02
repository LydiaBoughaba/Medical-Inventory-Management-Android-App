package com.boughaba.medicinestockmanagement.room.repository;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.boughaba.medicinestockmanagement.Constants;
import com.boughaba.medicinestockmanagement.room.dao.MedicineDao;
import com.boughaba.medicinestockmanagement.room.dao.SyncDao;
import com.boughaba.medicinestockmanagement.room.database.AppDataBase;
import com.boughaba.medicinestockmanagement.room.model.Medicine;
import com.boughaba.medicinestockmanagement.room.model.SyncMedicine;
import com.boughaba.medicinestockmanagement.room.model.SyncMedicineAndMedicine;
import com.boughaba.medicinestockmanagement.volley.BooleanSerializer;
import com.boughaba.medicinestockmanagement.volley.FileRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicineRepository {
    private MedicineDao dao;
    private SyncDao syncDao;
    private LiveData<List<Medicine>> allMedicine;
    private List<SyncMedicineAndMedicine> syncMedicines;

    public final String ip = "192.168.1.3";
    public final String urlGetMedicines = "http://" + ip + "/Medicine/listMedicine.php";
    private final String urlInsertMedicine = "http://" + ip + "/Medicine/insertMedicine.php";
    private final String urlDeleteMedicine = "http://" + ip + "/Medicine/deleteMedicine.php";
    Application application;

    public MedicineRepository(Application application) {
        this.application = application;
        AppDataBase database = AppDataBase.getDatabase(application);
        dao = database.medicineDao();
        allMedicine = dao.getAll();
        syncDao = database.syncDao();
        syncMedicines = syncDao.getAllWithMedicine();
        getMedicinesFromServer();
    }

    public void insert(Medicine medicine) {
        if (connectedToNetwork()) {
            insertToServer(medicine);
        } else {
            SyncMedicine syncMedicine = new SyncMedicine();
            syncMedicine.setMedicineId(medicine.getBarCode());
            syncMedicine.setAction(Constants.INSERT_ACTION);
            new InsertSyncMedicineAsyncTask(syncDao).execute(syncMedicine);
        }
        new InsertMedicineAsyncTask(dao).execute(medicine);
    }

    public void delete(Medicine medicine) {
        if (connectedToNetwork()) {
            deleteFromServer(medicine.getBarCode());
        } else {
            SyncMedicine syncMedicine = new SyncMedicine();
            syncMedicine.setMedicineId(medicine.getBarCode());
            syncMedicine.setAction(Constants.DELETE_ACTION);
            new InsertSyncMedicineAsyncTask(syncDao).execute(syncMedicine);
        }
        new DeleteMedicineAsyncTask(dao).execute(medicine);
    }

    public LiveData<List<Medicine>> getAllMedicine() {
        return allMedicine;
    }

    private void getMedicinesFromServer() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, urlGetMedicines, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                GsonBuilder b = new GsonBuilder();
                b.registerTypeAdapter(Boolean.class, new BooleanSerializer());
                Gson gson = b.create();
                List<Medicine> medicineList = new ArrayList<>();
                Type listType = new TypeToken<List<Medicine>>() {
                }.getType();
                medicineList = gson.fromJson(response.toString(), listType);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String refundable = jsonObject.getString("refundable");
                        if (Integer.parseInt(refundable) == 1) {
                            medicineList.get(i).setRefundable(true);
                        } else {
                            medicineList.get(i).setRefundable(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                syncFromServer(medicineList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        FileRequest.getInstance(application).addToRequest(jsonArrayRequest);
    }

    private void syncFromServer(List<Medicine> medicines) {
        List<String> barCodes = new ArrayList<>();
        for (Medicine medicine : medicines) {
            new InsertMedicineAsyncTask(dao).execute(medicine);
            barCodes.add(medicine.getBarCode());
        }
        new DeleteAllMedicinesAsyncTask(dao).execute(barCodes);
    }

    public void insertToServer(Medicine medicine) {
        StringRequest request = new StringRequest(Request.Method.POST, urlInsertMedicine, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> medicineMap = new HashMap();
                medicineMap.put("barCode", "" + medicine.getBarCode());
                medicineMap.put("name", "" + medicine.getName());
                medicineMap.put("classTh", "" + medicine.getClassTh());
                medicineMap.put("laboratory", "" + medicine.getLaboratory());
                medicineMap.put("denomination", "" + medicine.getDenomination());
                medicineMap.put("formMedicine", "" + medicine.getFormMedicine());
                medicineMap.put("conservationDuration", "" + medicine.getConservationDuration());
                if (medicine.getRefundable() == true) {
                    medicineMap.put("refundable", "" + 1);
                } else if (medicine.getRefundable() == false) {
                    medicineMap.put("refundable", "" + 0);
                }
                medicineMap.put("lot", "" + medicine.getLot());
                medicineMap.put("manufactureDate", "" + convertDateToString(medicine.getManufactureDate()));
                medicineMap.put("expirationDate", "" + convertDateToString(medicine.getExpirationDate()));
                medicineMap.put("description", "" + medicine.getDescription());
                medicineMap.put("quantity", "" + medicine.getQuantity());
                medicineMap.put("price", "" + medicine.getPrice());
                return medicineMap;
            }
        };
        FileRequest.getInstance(application).addToRequest(request);
    }

    public void deleteFromServer(String barCode) {
        StringRequest request = new StringRequest(Request.Method.POST, urlDeleteMedicine, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> medicineMap = new HashMap();
                medicineMap.put("barCode", "" + barCode);
                return medicineMap;
            }
        };
        FileRequest.getInstance(application).addToRequest(request);
    }

    private static class InsertMedicineAsyncTask extends AsyncTask<Medicine, Void, Void> {
        private MedicineDao dao;

        private InsertMedicineAsyncTask(MedicineDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Medicine... medicines) {
            dao.insert(medicines[0]);
            return null;
        }
    }

    private static class DeleteAllMedicinesAsyncTask extends AsyncTask<List<String>, Void, Void> {
        private MedicineDao dao;

        private DeleteAllMedicinesAsyncTask(MedicineDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(List<String>... ids) {
            dao.deleteWhereIn(ids[0]);
            return null;
        }

    }

    private static class DeleteMedicineAsyncTask extends AsyncTask<Medicine, Void, Void> {
        private MedicineDao dao;

        private DeleteMedicineAsyncTask(MedicineDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Medicine... medicines) {
            dao.delete(medicines[0]);
            return null;
        }
    }

    private static class InsertSyncMedicineAsyncTask extends AsyncTask<SyncMedicine, Void, Void> {
        private SyncDao dao;

        private InsertSyncMedicineAsyncTask(SyncDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(SyncMedicine... syncMedicines) {
            dao.insert(syncMedicines[0]);
            return null;
        }
    }

    private static class DeleteSyncMedicineAsyncTask extends AsyncTask<SyncMedicine, Void, Void> {
        private SyncDao dao;

        private DeleteSyncMedicineAsyncTask(SyncDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(SyncMedicine... syncMedicines) {
            dao.delete(syncMedicines[0]);
            return null;
        }
    }

    //Check if the app is connected to internet
    public boolean connectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    //Synchronization
    public void sync() {
        syncMedicines = syncDao.getAllWithMedicine();
        //sync from local database to the server
        if (syncMedicines.size() > 0 && connectedToNetwork()) {
            System.out.println(syncMedicines.size());
            for (int i = 0; i < syncMedicines.size(); i++) {
                if (syncMedicines.get(i).getSyncMedicine().getAction() == Constants.INSERT_ACTION) {
                    //insert to server and delete from syncMedicine
                    insertToServer(syncMedicines.get(i).getMedicine());
                    new DeleteSyncMedicineAsyncTask(syncDao).execute(syncMedicines.get(i).getSyncMedicine());
                } else if (syncMedicines.get(i).getSyncMedicine().getAction() == Constants.DELETE_ACTION) {
                    //delete from server and delete from syncMedicine
                    deleteFromServer(syncMedicines.get(i).getSyncMedicine().getMedicineId());
                    new DeleteSyncMedicineAsyncTask(syncDao).execute(syncMedicines.get(i).getSyncMedicine());
                }
            }
        }
        //sync from server to local database
        if (connectedToNetwork()) {
            getMedicinesFromServer();
        }
    }

    //Convert a Date to a String
    private String convertDateToString(Date date) {
        String stringDate = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        stringDate = dateFormat.format(date);
        return stringDate;
    }
}
