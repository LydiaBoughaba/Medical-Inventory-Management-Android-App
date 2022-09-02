package com.boughaba.medicinestockmanagement.room.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Medicine implements Serializable {
    @PrimaryKey
    @NonNull
    private String barCode;
    private String name;
    private String classTh;
    private String laboratory;
    private double price;
    private String denomination;
    private String formMedicine;
    @SerializedName("duration")
    private int conservationDuration;
    private boolean refundable;
    private String lot;
    private Date manufactureDate;
    private Date expirationDate;
    private String description;
    private int quantity;

    public Medicine() {
    }

    @Ignore
    public Medicine(String name, String classTh, String laboratory, double price, String denomination, String formMedicine, int conservationDuration, boolean refundable, String lot, Date manufactureDate, Date expirationDate, String description, int quantity, String barCode) {
        this.name = name;
        this.classTh = classTh;
        this.laboratory = laboratory;
        this.price = price;
        this.denomination = denomination;
        this.formMedicine = formMedicine;
        this.conservationDuration = conservationDuration;
        this.refundable = refundable;
        this.lot = lot;
        this.manufactureDate = manufactureDate;
        this.expirationDate = expirationDate;
        this.description = description;
        this.quantity = quantity;
        this.barCode = barCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassTh() {
        return classTh;
    }

    public void setClassTh(String classTh) {
        this.classTh = classTh;
    }

    public String getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(String laboratory) {
        this.laboratory = laboratory;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getFormMedicine() {
        return formMedicine;
    }

    public void setFormMedicine(String formMedicine) {
        this.formMedicine = formMedicine;
    }

    public int getConservationDuration() {
        return conservationDuration;
    }

    public void setConservationDuration(int conservationDuration) {
        this.conservationDuration = conservationDuration;
    }

    public boolean getRefundable() {
        return refundable;
    }

    public void setRefundable(boolean refundable) {
        this.refundable = refundable;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public Date getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Date manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
}
