package activities.activity_add_new_entry;

public interface IAddNewEntryActivityMVP {
    interface IAddNewEntryActivityView{

    }

    interface IAddNewEntryActivityPresenter{
        void updateCurrentLongLat(double longitude, double latitude);
        void getDamageDescriptions(String roofDmg, String windowsDmg, String wallsDmg);
        void getBitmapByteArrays(byte[][] byteArrayArray);
        boolean passDataToDBHelper();
        void dumpVariables();
    }

    interface IAddNewEntryActivityModel{
        boolean insertToDB(double longitude, double latitude, String roofDmg, String windowsDmg, String wallsDmg, byte[][] byteArrayArray);
    }
}
