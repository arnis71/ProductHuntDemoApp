package ru.arnis.producthuntdemoapp.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by arnis on 24/01/2017.
 */
@Table(name = "Categories")
public class Category extends Model {
    @Column(unique = true, onUniqueConflict = Column.ConflictAction.IGNORE)
    private String slug;
    @Column
    private String name;

    public Category() {
        super();
    }

    public String getSlug() {
        return slug;
    }
}
