package pl.chebad.bond.dam.newsappudacity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Data>> {

    private static final int DATA_LOADER_ID = 1;

    private EditText mEditText;
    private Button mButton;
    private ListView mListView;
    private TextView mEmptyMessageTextView;


    private NetworkInfo networkInfo;
    private ConnectivityManager connectManager;
    private LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.search_text_view);
        mButton = (Button) findViewById(R.id.search_button_view);
        mListView = (ListView) findViewById(R.id.news_list_view);
        mEmptyMessageTextView = (TextView) findViewById(R.id.empty_view);

        mListView.setEmptyView(mEmptyMessageTextView);

        ListAdapter adapter = new DataAdapter(this, new ArrayList<Data>());
        mListView.setAdapter(adapter);

        initiationConnection();
    }

    private void initiationConnection() {
        connectManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            loaderManager = getLoaderManager();
            loaderManager.initLoader(DATA_LOADER_ID, null, this);
        } else {
            View progressBar = findViewById(R.id.loading_bar);
            progressBar.setVisibility(View.GONE);
            mEmptyMessageTextView.setText(R.string.no_internet_connection);
        }
    }




    @Override
    public Loader<ArrayList<Data>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Data>> loader, ArrayList<Data> data) {

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Data>> loader) {

    }
}
