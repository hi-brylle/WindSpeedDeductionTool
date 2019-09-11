package activities.activity_add_new_entry;

public class AddNewEntryActivityPresenter implements IAddNewEntryActivityMVP.IAddNewEntryActivityPresenter {
    private IAddNewEntryActivityMVP.IAddNewEntryActivityView mvpView;
    private IAddNewEntryActivityMVP.IAddNewEntryActivityModel mvpModel;
    private IGPSFixListener gpsFixListener;

    //"Double" is a non-primitive type and thus can be null
    //used to check if gps has gotten a fix yet
    private Double longitude;
    private Double latitude;

    //qualitative descriptions of damages per component
    private String roofDamageDesc;
    private String windowsDamageDesc;
    private String wallsDamageDesc;

    private byte[][] byteArrayArray;

    public AddNewEntryActivityPresenter(IAddNewEntryActivityMVP.IAddNewEntryActivityView mvpView,
                                        IAddNewEntryActivityMVP.IAddNewEntryActivityModel mvpModel){
        this.mvpView = mvpView;
        this.mvpModel = mvpModel;
    }

    @Override
    public void updateCurrentLongLat(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;

        //callback when long and lat are both non-null
        gpsFixListener.onGPSFix();
    }

    @Override
    public void getDamageDescriptions(String roofDmg, String windowsDmg, String wallsDmg) {
        roofDamageDesc = roofDmg;
        windowsDamageDesc = windowsDmg;
        wallsDamageDesc = wallsDmg;
    }

    @Override
    public void getBitmapByteArrays(byte[][] byteArrayArray) {
        this.byteArrayArray = byteArrayArray;
    }

    @Override
    public boolean passDataToDBHelper() {
        return mvpModel.insertToDB(longitude, latitude, roofDamageDesc, windowsDamageDesc, wallsDamageDesc, byteArrayArray);
    }

    @Override
    public void dumpVariables() {
        roofDamageDesc = null;
        windowsDamageDesc = null;
        wallsDamageDesc = null;
        byteArrayArray = null;
    }

    public void addGPSFixListener(IGPSFixListener listener){
        gpsFixListener = listener;
    }

}
