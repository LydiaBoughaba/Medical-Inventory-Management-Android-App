package com.boughaba.medicinestockmanagement.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.boughaba.medicinestockmanagement.Constants;
import com.boughaba.medicinestockmanagement.room.repository.MedicineRepository;
import com.boughaba.medicinestockmanagement.ui.main.MainActivity;

//A service to launch the sync between the local database and the sever
public class SyncService extends Service {
    private Handler handler;
    private MedicineRepository repository;

    public SyncService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        repository = new MedicineRepository(getApplication());
        handler = new Handler(getMainLooper());
        handler.postDelayed(syncRunnable, 0);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Runnable syncRunnable = new Runnable() {
        @Override
        public void run() {
            syncData();
            handler.postDelayed(syncRunnable, Constants.SYNC_DELAY);
        }
    };

    public void syncData() {
        repository.sync();
    }
}