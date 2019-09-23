package activities.activity_main;

import helper_classes.db_helper.IDBHelper;

public interface IMainActivityMVP {
    interface IMainActivityView{
        void openAddNewEntryActivity();
    }

    interface IMainActivityPresenter{
        void handleNewButtonClick();
        IDBHelper getDBHelper();
    }
}
