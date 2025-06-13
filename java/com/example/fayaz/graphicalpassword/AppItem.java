package com.example.fayaz.graphicalpassword;

public class AppItem {
    private String id;
    private String icon;
    private String appname;
    private String locked;

    public AppItem(String id, String icon, String appname, String locked) {
        this.id = id;
        this.icon = icon;
        this.appname = appname;
        this.locked = locked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }
}
