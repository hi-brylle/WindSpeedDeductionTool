package activities.activity_gallery.gallery_image;

public class GalleryImage implements IGalleryImage{
    private boolean isSelected;
    private IImageSelectListeners imageSelectListener;

    public GalleryImage(){
        isSelected = false;
    }

    public void toggleSelection(){
        isSelected = !isSelected;

        if(isSelected) {
            imageSelectListener.onImageSelected();
        } else{
            imageSelectListener.onImageNotSelected();
        }

    }

    public boolean getSelectStatus(){
        return isSelected;
    }

    public void forceDeselect(){
        isSelected = false;

        imageSelectListener.onImageNotSelected();
    }

    public void addImageSelectListeners(IImageSelectListeners listener){
        imageSelectListener = listener;
    }
}
