package activities.activity_main;

public class MainActivityPresenter implements IMainActivityMVP.IMainActivityPresenter {
    private IMainActivityMVP.IMainActivityView mMVPView;

    public MainActivityPresenter(IMainActivityMVP.IMainActivityView mvpView){
        mMVPView = mvpView;
    }

    @Override
    public void handleNewButtonClick() {
        mMVPView.openAddNewEntryActivity();
    }
}
