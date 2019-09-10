package activities.activity_main;

public interface IMainActivityMVP {
    interface IMainActivityView{
        //hmm so far, all these methods are self-contained
        //they don't use the reference to the presenter
        void openAddNewEntryActivity();
    }

    interface IMainActivityPresenter{
        void handleNewButtonClick();
    }
}
