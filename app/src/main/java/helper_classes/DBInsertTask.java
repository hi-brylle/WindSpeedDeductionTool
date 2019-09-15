package helper_classes;

import android.os.AsyncTask;

import java.util.HashMap;

import activities.activity_add_new_entry.IAddNewEntryActivityMVP;

public class DBInsertTask extends AsyncTask<Object, Void, Boolean> {

    private IAddNewEntryActivityMVP.IAddNewEntryActivityView mvpView;
    private IAddNewEntryActivityMVP.IAddNewEntryActivityPresenter mvpPresenter;

    public DBInsertTask(IAddNewEntryActivityMVP.IAddNewEntryActivityView mvpView,
                        IAddNewEntryActivityMVP.IAddNewEntryActivityPresenter mvpPresenter){

        this.mvpView = mvpView;
        this.mvpPresenter = mvpPresenter;
    }

    @Override
    protected Boolean doInBackground(Object... objects) {
        HashMap<String, String> componentToDmgDescriptions = (HashMap<String, String>) objects[0];
        byte[][] finalByteArrayArray = (byte[][]) objects[1];

        return mvpPresenter.passDataToDBHelper(componentToDmgDescriptions, finalByteArrayArray);
    }

    @Override
    protected void onPostExecute(Boolean insertSuccess) {
        mvpView.showToastOnDBInsert(insertSuccess);
    }
}
