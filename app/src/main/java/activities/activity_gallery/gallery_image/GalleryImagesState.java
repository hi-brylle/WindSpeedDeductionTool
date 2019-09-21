package activities.activity_gallery.gallery_image;

public class GalleryImagesState{
    private int numSelected;
    private IGalleryImage.ISelectCountListeners selectCountListener;

    public GalleryImagesState(){
        numSelected = 0;
    }

    public void decrementSelections(){
        if(numSelected > 0){
            numSelected--;
            selectCountListener.onDecrement();
        }

        if(numSelected == 0){
            selectCountListener.onNoneSelected();
        }
    }

    public void incrementSelections(){
        numSelected++;

        selectCountListener.onAtLeastOneSelected();
    }

    public int returnNumSelected(){
        return numSelected;
    }

    public void addSelectCountListener(IGalleryImage.ISelectCountListeners listener){
        selectCountListener = listener;
    }
}
