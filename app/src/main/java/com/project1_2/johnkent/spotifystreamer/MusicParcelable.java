package com.project1_2.johnkent.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by John on 7/11/2015.
 */
public class MusicParcelable implements Parcelable {
    String artistName;
    String trackName;
    String id;
    String imgUrl;
    String albumName;
    String previewUrl;

    public MusicParcelable (String name, String id, String imgUrl){
        this.artistName=name;
        this.id=id;
        this.imgUrl=imgUrl;
    }

    public MusicParcelable (String name, String artistName, String albumName, String previewUrl, String imgUrl){
        this.trackName=name;
        this.artistName=artistName;
        this.albumName=albumName;
        this.previewUrl=previewUrl;
        this.imgUrl=imgUrl;
    }

    public int describeContents(){
        return 0;
    }

    @Override
    public String toString(){
        if (trackName.equals(null)){
            return artistName + ": " + id + ": " + imgUrl;
        } else{
            return trackName + ": " +  artistName + ": " + albumName + ": " + previewUrl + ": " + imgUrl;
        }
    }

    private MusicParcelable(Parcel in){
        artistName=in.readString();
        id=in.readString();
        albumName=in.readString();
        previewUrl=in.readString();
        imgUrl=in.readString();
    }

    public void writeToParcel(Parcel out, int flags){
        out.writeString(artistName);
        out.writeString(trackName);
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
