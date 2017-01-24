package ru.arnis.producthuntdemoapp.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Created by arnis on 24/01/2017.
 */
@Table(name = "Thumbnails")
public class Thumbnail extends Model {
    @Column//(unique = true, onUniqueConflict = Column.ConflictAction.IGNORE)
    @SerializedName("media_type")
    private String type;

    @Column//(unique = true, onUniqueConflict = Column.ConflictAction.IGNORE)
    @SerializedName("image_url")
    private String url;

    public Thumbnail() {
        super();
    }
}
