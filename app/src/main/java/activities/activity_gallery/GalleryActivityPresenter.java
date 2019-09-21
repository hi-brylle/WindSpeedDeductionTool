package activities.activity_gallery;

import android.net.Uri;

import java.util.ArrayList;

import activities.activity_gallery.gallery_image.GalleryImage;
import activities.activity_gallery.gallery_image.GalleryImagesState;

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




}
