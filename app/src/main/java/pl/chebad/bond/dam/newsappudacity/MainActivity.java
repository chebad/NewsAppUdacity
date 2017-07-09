package pl.chebad.bond.dam.newsappudacity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Data>> {

    private static final int DATA_LOADER_ID = 1;

    private EditText mEditText;
    private Button mButton;
    private ListView mListView;
    private TextView mEmptyMessageTextView;

    private DataAdapter mAdapter;

    NetworkInfo networkInfo;
    ConnectivityManager connectManager;
    LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.search_text_view);
        mButton = (Button) findViewById(R.id.search_button_view);
        mListView = (ListView) findViewById(R.id.news_list_view);
        mEmptyMessageTextView = (TextView) findViewById(R.id.empty_view);

        mListView.setEmptyView(mEmptyMessageTextView);

        mAdapter = new DataAdapter(this, new ArrayList<Data>());
        mListView.setAdapter(mAdapter);

        initiationConnection();
        searchButtonClick();
        listItemClick();
    }

    private void initiationConnection() {
        connectManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            loaderManager = getLoaderManager();
            loaderManager.initLoader(DATA_LOADER_ID, null, this);
        } else {
            View progressBar = findViewById(R.id.loading_bar);
            progressBar.setVisibility(GONE);
            mEmptyMessageTextView.setText(R.string.no_internet_connection);
        }
    }

    private void searchButtonClick() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                networkInfo = connectManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    getLoaderManager().restartLoader(DATA_LOADER_ID, null, MainActivity.this);
                } else {
                    mAdapter.clear();
                    View progressBar = findViewById(R.id.loading_bar);
                    progressBar.setVisibility(GONE);
                    mEmptyMessageTextView.setText(R.string.no_internet_connection);
                }
            }
        });
    }

    private void listItemClick() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                connectManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                networkInfo = connectManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    Data singleNews = mAdapter.getItem(position);
                    Uri dataUri = Uri.parse(singleNews.getUrl());
                    Intent moveToCurrentWebsite = new Intent(Intent.ACTION_VIEW, dataUri);
                    startActivity(moveToCurrentWebsite);
                }
            }
        });
    }

    @Override
    public Loader<List<Data>> onCreateLoader(int id, Bundle args) {
        String query = mEditText.getText().toString();
        DataLoader dataLoader = new DataLoader(this, query);
        return dataLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Data>> loader, List<Data> data) {
        View progressBar = findViewById(R.id.loading_bar);
        progressBar.setVisibility(GONE);

        //clear search text view and adapter
        mEmptyMessageTextView.setText("");
        mAdapter.clear();

        //fill the adapter
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Data>> loader) {
        mAdapter.clear();
    }
}
