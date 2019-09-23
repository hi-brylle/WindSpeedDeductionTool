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
    public Uri getPhotoURIAt(int index) {
        return galleryImages.get(index).getPhotoUri();
    }

    @Override
    public int getURIsSize() {
        int size = 0;
        for(int i = 0; i < galleryImages.size(); i++){
            if(galleryImages.get(i).getPhotoUri() != null){
                size++;
            }
        }

        return size;
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
    public void toggleSelectionAt(int index) {
        galleryImages.get(index).toggleSelection();
    }

    @Override
    public boolean isImageSelected(int index) {
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
