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
    @SerializedName("id")
    private int postID;

    @Column
    private String name;

    @Column
    @SerializedName("tagline")
    private String description;

    @Column
    @SerializedName("votes_count")
    private int upVotes;

    @Column
    private Thumbnail thumbnail;

    @Column
    @SerializedName("redirect_url")
    private String getItUrl;

    @Column
    @SerializedName("screenshot_url")
    private Screenshot screenshot;

    @Column
    @SerializedName("category_id")
    private int categoryID;

    public Post() {
        super();
    }


}
