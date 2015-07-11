package com.project1_2.johnkent.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by John on 7/11/2015.
 */
public class MusicParcelable implements Parcelable {
    String name;
    String id;
    String imgUrl;
    String albumName;
    String previewUrl;

    public MusicParcelable (String name, String id, String imgUrl){
        this.name=name;
        this.id=id;
        this.imgUrl=imgUrl;
    }

    public MusicParcelable (String name, String albumName, String previewUrl, String imgUrl){
        this.name=name;
        this.albumName=albumName;
        this.previewUrl=previewUrl;
        this.imgUrl=imgUrl;
    }

    public int describeContents(){
        return 0;
    }

    @Override
    public String toString(){
        if (albumName.equals(null)){
            return name + ": " + id + ": " + imgUrl;
        } else{
            return name + ": " + albumName + ": " + previewUrl + ": " + imgUrl;
        }
    }

    private MusicParcelable(Parcel in){
        name=in.readString();
        id=in.readString();
        albumName=in.readString();
        previewUrl=in.readString();
        imgUrl=in.readString();
    }

    public void writeToParcel(Parcel out, int flags){
        out.writeString(name);
        out.writeString(id);
        out.writeString(albumName);
        out.writeString(previewUrl);
        out.writeString(imgUrl);
    }

    public static final Parcelable.Creator<MusicParcelable> CREATOR = new Parcelable.Creator<MusicParcelable>(){

        public MusicParcelable createFromParcel(Parcel in){
            return new MusicParcelable(in);
        }

        public MusicParcelable[] newArray(int size){
            return new MusicParcelable[size];
        }

    };







}
