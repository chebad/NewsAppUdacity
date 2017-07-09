package pl.chebad.bond.dam.newsappudacity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Damian Bondaruk on 2017-07-09.
 */

public class DataAdapter extends ArrayAdapter<Data> {
    public DataAdapter(@NonNull Context context, ArrayList<Data> news) {
        super(context,0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = null;
        
        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.single_news,parent,false
            );
            holder = new ViewHolder();
            holder.articleTittle = (TextView) view.findViewById(R.id.article_tittle_view);
            holder.sectionTittle = (TextView) view.findViewById(R.id.section_tittle_view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Data singleNews = getItem(position);

        holder.articleTittle.setText(singleNews.getArticleTittle());
        holder.sectionTittle.setText(singleNews.getSectionName());
        
        return view;
    }
    
    private static class ViewHolder {
        TextView articleTittle;
        TextView sectionTittle;
    }
}
