package com.tp.macarte;

/**
 * Created by HOME on 18/03/2017.
 */

public class MonMarqueur {
    private long id;
    private String titre;
    private String type;
    private float lat;
    private float lng;
    private String image;
    private String video;
    private String description;
    private String adresse;
    private String telephone;
    private String email;
    private float vote = 0;
    private int fav = 0;


    public MonMarqueur() {}

    public MonMarqueur( long id, String titre, String type, float lat, float lng, String  image, String  video,  String description, String adresse, String telephone, String email, float vote, int fav) {
        this.id = id;
        this.titre = titre ;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
        this.image = image;
        this.video = video;
        this.description = description;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.vote = vote;
        this.fav = fav ;
    }
    public MonMarqueur(  String titre, String type, float lat, float lng, String  image, String  video,  String description, String adresse, String telephone, String email , float vote , int fav) {
        this.titre = titre ;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
        this.image = image;
        this.video = video;
        this.description = description;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.vote = vote;
        this.fav = fav ;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLat() {
        return lat;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public float getLng() {
        return lng;
    }

    public void setImage(String  image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }


    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideo() {
        return video;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setVote(float vote) {
        this.vote = vote;
    }

    public float getVote() {
        return vote;
    }


    public void setFav(int fav) {
        this.fav = fav;
    }

    public int getFav() {
        return fav;
    }

}
