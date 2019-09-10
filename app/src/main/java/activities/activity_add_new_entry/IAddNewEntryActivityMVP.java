package activities.activity_add_new_entry;

public interface IAddNewEntryActivityMVP {
    interface IAddNewEntryActivityView{

    }

    interface IAddNewEntryActivityPresenter{
        void updateCurrentLongLat(double longitude, double latitude);
        void fetchDamageDescriptions(String roofDmg, String windowsDmg, String wallsDmg);
        void logFetches();
        boolean passDataToDBHelper();
    }

    interface IAddNewEntryActivityModel{
        boolean insertToDB(double longitude, double latitude, String roofDmg, String windowsDmg, String wallsDmg);
    }
}
