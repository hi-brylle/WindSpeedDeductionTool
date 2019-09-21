package activities.activity_gallery;

import android.net.Uri;

import java.util.ArrayList;

import activities.activity_gallery.gallery_image.GalleryImage;
import activities.activity_gallery.gallery_image.GalleryImagesState;
import activities.activity_gallery.gallery_image.IGalleryImage;

class GalleryActivityPresenter implements IGalleryActivityMVP.IGalleryActivityPresenter {
    private IGalleryActivityMVP.IGalleryActivityView mvpView;
    private ArrayList<GalleryImage> galleryImages;
    private GalleryImagesState galleryImagesState;

    GalleryActivityPresenter(IGalleryActivityMVP.IGalleryActivityView mvpView){
        this.mvpView = mvpView;
    }

    @Override
    public void initGalleryImages(ArrayList<Uri> photoSetURIs) {
        galleryImages = new ArrayList<>();

        for(int i = 0; i < photoSetURIs.size(); i++){
            galleryImages.add(new GalleryImage(photoSetURIs.get(i)));
        }

        galleryImagesState = new GalleryImagesState();
    }

    @Override
    public ArrayList<Uri> getPhotoURIs() {
        ArrayList<Uri> photoURIs = new ArrayList<>();
        for(int i = 0; i < galleryImages.size(); i++){
            photoURIs.add(galleryImages.get(i).getPhotoUri());
        }

        return photoURIs;
    }

    @Override
    public void setupSelectListeners() {
        for(int i = 0; i < galleryImages.size(); i++){
            galleryImages.get(i).addImageSelectListeners(new IGalleryImage.IImageSelectListeners() {
                @Override
                public void onImageNotSelected() {
                    galleryImagesState.decrementSelections();
                }

                @Override
                public void onImageSelected() {
                    galleryImagesState.incrementSelections();
                }
            });
        }

        galleryImagesState.addSelectCountListener(new IGalleryImage.ISelectCountListeners() {
            @Override
            public void onNoneSelected() {
                mvpView.hideCancel();
                mvpView.disableDelete();
                mvpView.hideSelectCount();
            }

            @Override
            public void onAtLeastOneSelected() {
                mvpView.showCancel();
                mvpView.enableDelete();
                mvpView.showAndUpdateSelectCount(galleryImagesState.getNumSelected());
            }

            @Override
            public void onDecrement() {
                mvpView.showAndUpdateSelectCount(galleryImagesState.getNumSelected());
            }
        });
    }

    @Override
    public void toggleSelection(int index) {
        galleryImages.get(index).toggleSelection();
    }

    @Override
    public boolean isCurrentImageSelected(int i) {
        return galleryImages.get(i).getSelectStatus();
    }

    @Override
    public boolean noPhotosSelected() {
        return galleryImagesState.getNumSelected() == 0;
    }


}
