package com.example.urmil.myapplication;

/**
 * Created by Urmil on 12-04-2016.
 */
public class Document {

    private String name;
    private String tags;
    private String image;

    public Document() {
        // TODO Auto-generated constructor stub
    }

    public Document(String name, String tags, String image) {
        super();
        this.name = name;
        this.tags = tags;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
