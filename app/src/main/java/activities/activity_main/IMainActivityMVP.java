package activities.activity_main;

public interface IMainActivityMVP {
    interface IMainActivityView{
        void openAddNewEntryActivity();
    }

    interface IMainActivityPresenter{
        void handleNewButtonClick();
    }
}
