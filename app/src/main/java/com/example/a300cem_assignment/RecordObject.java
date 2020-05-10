package com.example.a300cem_assignment;

public class RecordObject {

    private  String id;
    private  String title;
    private  String description;
    private  String date;
    private  String audioFileName;
    private  String photoPath;

    RecordObject(String title, String description, String date, String audioFileName, String photoPath) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.audioFileName = audioFileName;
        this.photoPath = photoPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String getTitle() {
        return title;
    }

    String getDescription() {
        return description;
    }

    String getDate() {
        return date;
    }

    String getAudioFileName() {
        return audioFileName;
    }

    String getPhotoPath() {
        return photoPath;
    }
}
