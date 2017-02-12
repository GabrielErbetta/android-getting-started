package models;

import java.io.Serializable;

/**
 * Created by Erbetta on 04/02/2017.
 */

public class Contact implements Serializable {
    private Long id;
    private String name, address, phone, website, photo_path;
    private double rating;

    public Contact() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPhotoPath() { return photo_path; }

    public void setPhotoPath(String path) { this.photo_path = path; }

    @Override
    public String toString() {
        return this.name;
    }
}
