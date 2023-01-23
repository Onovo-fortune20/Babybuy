package com.example.babybuy.Model;

public class item {
    private String imageUrl;
    private String item_name;
    private String item_price;
    private String item_description;
    private int position;

    public item() {
        //empty constructor
    }

    public item (int position){
        this.position = position;

    }
    public item (String name, String imageUrl, String des){
        if (name.trim().equals("")){
            name = "No Name";

        }
        this.item_name = name;
        this.imageUrl = imageUrl;
        this.item_description = des;

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
