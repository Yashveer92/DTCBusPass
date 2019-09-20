package com.example.dtcbuspass.SelectPass;

import android.net.Uri;

public class AddCustomerFDocumentData {

    private String buttonId, btnstate;
    private Uri photoUri;


    AddCustomerFDocumentData(String buttonId, Uri photoUri, String btnstate) {
        this.buttonId = buttonId;
        this.photoUri = photoUri;
        this.btnstate = btnstate;
    }

    public AddCustomerFDocumentData() {
    }


    public String getButtonId() {
        return buttonId;
    }

    public void setButtonId(String buttonId) {
        this.buttonId = buttonId;
    }

    public String getBtnstate() {
        return btnstate;
    }

    public void setBtnstate(String btnstate) {
        this.btnstate = btnstate;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }
}
