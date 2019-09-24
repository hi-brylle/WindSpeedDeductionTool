package helper_classes.photo_manager;

import android.net.Uri;

import java.util.ArrayList;

public class UriListSingleton {
    private static UriListSingleton THE_BOI;
    private ArrayList<Uri> UriList;

    private UriListSingleton(){
        UriList = new ArrayList<>();
    }

    public synchronized static UriListSingleton getInstance(){
        if(THE_BOI == null){
            THE_BOI = new UriListSingleton();
        }

        return THE_BOI;
    }

    public synchronized void addUri(Uri uri){
        if(UriList == null){
            UriList = new ArrayList<>();
        }
        UriList.add(uri);
    }

    public synchronized Uri getUriAt(int index){
        return UriList.get(index);
    }

    public synchronized int getUriListSize(){
        return UriList.size();
    }

    public synchronized  void removeUriAt(int index){
        UriList.remove(index);
    }

    public synchronized void clearUriList(){
        UriList.clear();
    }
}
