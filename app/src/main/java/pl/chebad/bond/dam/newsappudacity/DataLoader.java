package pl.chebad.bond.dam.newsappudacity;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Damian Bondaruk on 2017-07-09.
 */

public class DataLoader extends AsyncTaskLoader<List<Data>> {
    private String query;

    public DataLoader(Context context, String query) {
        super(context);
        this.query = query;
    }

    @Override
    public List<Data> loadInBackground() {
        if(query == null){
            return null;
        }

        ArrayList<Data> newsList = DataUtils.fetchData(query);
        return newsList;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
