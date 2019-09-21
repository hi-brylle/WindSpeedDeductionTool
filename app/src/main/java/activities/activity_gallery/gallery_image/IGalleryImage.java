package activities.activity_gallery.gallery_image;

public interface IGalleryImage {
    interface IImageSelectListeners {
        void onImageNotSelected();
        void onImageSelected();
    }
    interface ISelectCountListeners {
        void onNoneSelected();
        void onAtLeastOneSelected();
        void onDecrement();
    }
}
