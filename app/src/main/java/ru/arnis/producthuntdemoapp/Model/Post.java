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

    @Column(unique = true, onUniqueConflict = Column.ConflictAction.IGNORE)
    private String name;

    @Column
    @SerializedName("tagline")
    private String description;

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

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }


}
