package com.example.travelmantics;

import java.io.Serializable;

public class TravelDeal  implements Serializable {
    private  String id;
    private  String  Title;
    private  String  Price;
    private  String Description;
    private  String imageUrl;
    private String  ImageName;

    public TravelDeal() {}

    public  TravelDeal (String Title,String Price ,String Description, String imageUrl ,String ImageName){
        this.setId(id);
        this.setTitle(Title);
        this.setPrice(Price);
        this.setDescription(Description);
        this.setImageUrl(imageUrl);
        this.setImageName(ImageName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }
}
