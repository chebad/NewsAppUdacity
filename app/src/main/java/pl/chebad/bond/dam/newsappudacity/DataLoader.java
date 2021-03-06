package pl.chebad.bond.dam.newsappudacity;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Damian Bondaruk on 2017-07-09.
 *
 */

class DataLoader extends AsyncTaskLoader<List<Data>> {
    private String query;

    DataLoader(Context context, String query) {
        super(context);
        this.query = query;
    }

    @Override
    public List<Data> loadInBackground() {
        if(query == null){
            return null;
        }

        return DataUtils.fetchData(query);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
