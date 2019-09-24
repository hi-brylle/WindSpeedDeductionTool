package activities.activity_gallery;

import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

import activities.activity_gallery.gallery_image.GalleryImage;
import activities.activity_gallery.gallery_image.GalleryImagesState;
import activities.activity_gallery.gallery_image.IGalleryImage;
import helper_classes.photo_manager.UriListSingleton;

class GalleryActivityPresenter implements IGalleryActivityMVP.IGalleryActivityPresenter {
    private IGalleryActivityMVP.IGalleryActivityView mvpView;
    private ArrayList<GalleryImage> galleryImages;
    private GalleryImagesState galleryImagesState;

    GalleryActivityPresenter(IGalleryActivityMVP.IGalleryActivityView mvpView){
        this.mvpView = mvpView;
    }

    @Override
    public void initGalleryImages() {
        if(galleryImages == null){
            galleryImages = new ArrayList<>();
        } else{
            galleryImages.clear();
        }

        UriListSingleton uriListSingleton = UriListSingleton.getInstance();
        for(int i = 0; i < uriListSingleton.getUriListSize(); i++){
            galleryImages.add(new GalleryImage());
        }

        if(galleryImagesState == null){
            galleryImagesState = new GalleryImagesState();
        } else{
            galleryImagesState.resetNumSelections();
        }

        setupSelectListeners();
    }

    @Override
    public Uri getPhotoURIAt(int index) {
        UriListSingleton uriListSingleton = UriListSingleton.getInstance();
        return uriListSingleton.getUriAt(index);
    }

    @Override
    public int getUriListSize() {
        UriListSingleton uriListSingleton = UriListSingleton.getInstance();
        return uriListSingleton.getUriListSize();
    }

    @Override
    public void setupSelectListeners() {
        for(int i = 0; i < galleryImages.size(); i++){
            Log.d("MY TAG", "LISTENERS: " + i + 1);
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
    public void toggleSelectionAt(int index) {
        galleryImages.get(index).toggleSelection();
    }

    @Override
    public boolean isImageSelectedAt(int index) {
        return galleryImages.get(index).getSelectStatus();
    }

    @Override
    public boolean noPhotosSelected() {
        return galleryImagesState.getNumSelected() == 0;
    }

    @Override
    public void removeGalleryImageAt(int index) {
        galleryImages.remove(index);
        galleryImages.trimToSize();
        galleryImagesState.decrementSelections();
    }

    @Override
    public void forceDeselectAll() {
        for(int i = 0; i < galleryImages.size(); i++){
            galleryImages.get(i).forceDeselect();
        }
    }


}
