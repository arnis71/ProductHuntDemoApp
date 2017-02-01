package ru.arnis.producthuntdemoapp.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Created by arnis on 24/01/2017.
 */
@Table(name = "Posts")
public class Post extends Model {

    @Column(unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String name;

    @Column
    @SerializedName("tagline")
    private String description;

    @Column
    @SerializedName("category_id")
    private long fromCategory;

    @Column
    @SerializedName("votes_count")
    private int upVotes;

    @Column
    private String thumbnailUrl;

    @Column
    @SerializedName("redirect_url")
    private String getItUrl;

    @Column
    private String screenshotUrl;

    public Post() {
        super();
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setScreenshotUrl(String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public void setGetItUrl(String getItUrl) {
        this.getItUrl = getItUrl;
    }

    public void setFromCategory(long fromCategory) {
        this.fromCategory = fromCategory;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public String getGetItUrl() {
        return getItUrl;
    }

    public long getFromCategory() {
        return fromCategory;
    }
}
