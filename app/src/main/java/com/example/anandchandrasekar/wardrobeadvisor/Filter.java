package com.example.anandchandrasekar.wardrobeadvisor;

/**
 * Created by anandchandrasekar on 11/9/15.
 */
public class Filter {
    private int id;
    private String filterName;
    private String filterKind;
    private String filterImagePath;

    public Filter(int id, String filterName, String filterKind, String filterImagePath) {
        this.id = id;
        this.filterName = filterName;
        this.filterKind = filterKind;
        this.filterImagePath = filterImagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getFilterKind() {
        return filterKind;
    }

    public void setFilterKind(String filterKind) {
        this.filterKind = filterKind;
    }

    public String getFilterImagePath() {
        return filterImagePath;
    }

    public void setFilterImagePath(String filterImagePath) {
        this.filterImagePath = filterImagePath;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Filter))return false;
        Filter otherFilter = (Filter)other;
        return (otherFilter.getId() == getId());
    }
}
