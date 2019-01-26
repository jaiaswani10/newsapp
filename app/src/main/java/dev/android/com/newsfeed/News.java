package dev.android.com.newsfeed;

public class News {

    private String mThumbnail;
    private String mHeadline;
    private String mTags;
    private String mUrl;
    private String mTime;

    private String mAuthor;

    public News(String mThumbnail, String mHeadline, String mTags, String mUrl, String mTime, String mAuthor) {
        this.mThumbnail = mThumbnail;
        this.mHeadline = mHeadline;
        this.mTags = mTags;
        this.mUrl = mUrl;
        this.mTime = mTime;

        this.mAuthor = mAuthor;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }

    public void setmThumbnail(String mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

    public String getmHeadline() {
        return mHeadline;
    }

    public void setmHeadline(String mHeadline) {
        this.mHeadline = mHeadline;
    }

    public String getmTags() {
        return mTags;
    }

    public void setmTags(String mTags) {
        this.mTags = mTags;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }



    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }
}
