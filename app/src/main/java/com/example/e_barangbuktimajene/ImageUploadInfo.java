package com.example.e_barangbuktimajene;

public class ImageUploadInfo {

    public String imageName;

    public String imageURL;

    public String registrationName;

    public String statusName;

    public String informationName;

    public ImageUploadInfo() {

    }

    public ImageUploadInfo(String name, String reg, String info, String status, String url) {

        this.imageName = name;
        this.registrationName = reg;
        this.informationName = info;
        this.statusName = status;
        this.imageURL = url;
    }

    public String getImageName() {
        return imageName;
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