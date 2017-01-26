package ru.arnis.producthuntdemoapp.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Created by arnis on 24/01/2017.
 */
@Table(name = "Categories")
public class Category extends Model {
    @SerializedName("id")
    @Column(unique = true, onUniqueConflict = Column.ConflictAction.ABORT)
    private int catID;
    @Column
    private String slug;
    @Column
    private String name;

    public Category() {
        super();
    }

    public Category(String slug) {
        catID = 1;// Will be igonred by DB
        this.slug = slug;
        this.name = slug.substring(0, 1).toUpperCase() + slug.substring(1);
    }

    public String getSlug() {
        return slug;
    }

    public String getName() {
        return name;
    }

    public int getCatID() {
        return catID;
    }
}
