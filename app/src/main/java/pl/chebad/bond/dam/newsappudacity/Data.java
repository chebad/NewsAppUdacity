package pl.chebad.bond.dam.newsappudacity;

/**
 * Created by Damian Bondaruk on 2017-07-09.
 */

public class Data {
    private String mArticleTittle, mSectionName, mUrl;

    public Data(String mArticleTittle, String mSectionName, String mUrl) {
        this.mArticleTittle = mArticleTittle;
        this.mSectionName = mSectionName;
        this.mUrl = mUrl;
    }

    public String getArticleTittle() {
        return mArticleTittle;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public String getUrl() {
        return mUrl;
    }
}
