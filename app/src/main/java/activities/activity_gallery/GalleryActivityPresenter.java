package activities.activity_gallery;

class GalleryActivityPresenter implements IGalleryActivityMVP.IGalleryActivityPresenter {
    private IGalleryActivityMVP.IGalleryActivityView mvpView;

    GalleryActivityPresenter(IGalleryActivityMVP.IGalleryActivityView mvpView){
        this.mvpView = mvpView;
    }
}
