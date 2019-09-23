package activities.activity_main;

import helper_classes.db_helper.IDBHelper;

public class MainActivityPresenter implements IMainActivityMVP.IMainActivityPresenter {
    private IMainActivityMVP.IMainActivityView mvpView;
    private IDBHelper dbHelper;

    MainActivityPresenter(IMainActivityMVP.IMainActivityView mvpView, IDBHelper dbHelper){
        this.mvpView = mvpView;
        this.dbHelper = dbHelper;
    }

    @Override
    public void handleNewButtonClick() {
        mvpView.openAddNewEntryActivity();
    }

    @Override
    public IDBHelper getDBHelper() {
        return dbHelper;
    }
}
