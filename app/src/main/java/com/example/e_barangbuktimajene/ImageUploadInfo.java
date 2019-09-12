package com.example.e_barangbuktimajene;

public class ImageUploadInfo {

    public String imageName;

    public String imageURL;

    public String registrationName;

    public String statusName;

    public String informationName;

    public String imageID;

    public ImageUploadInfo() {

    }

    public ImageUploadInfo(String name, String reg, String info, String status, String url, String id) {

        this.imageName = name;
        this.registrationName = reg;
        this.informationName = info;
        this.statusName = status;
        this.imageURL = url;
        this.imageID = id;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageID() {
        return imageID;
    }

    public String getRegistrationName() {
        return registrationName;
    }

    public String getInformationName() {
        return informationName;
    }

    public String getStatusName() {
        return statusName;
    }

    public String getImageURL() {
        return imageURL;
    }

}