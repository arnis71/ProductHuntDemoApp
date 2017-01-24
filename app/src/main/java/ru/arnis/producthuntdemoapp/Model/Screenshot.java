package ru.arnis.producthuntdemoapp.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Created by arnis on 24/01/2017.
 */
@Table(name = "Screenshots")
public class Screenshot extends Model {
    @Column//(unique = true, onUniqueConflict = Column.ConflictAction.IGNORE)
    @SerializedName("300px")
    private String smallPicUrl;

    @Column//(unique = true, onUniqueConflict = Column.ConflictAction.IGNORE)
    @SerializedName("850px")
    private String bigPicUrl;

    public Screenshot() {
        super();
    }
}
