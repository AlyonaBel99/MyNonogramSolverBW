package com.company;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;

public class Nonogram implements Serializable {
    private ArrayList <ArrayList< Integer >> numberHeight =  new  ArrayList < ArrayList < Integer > > ();
    private ArrayList < ArrayList < Integer > > numberWidth =  new  ArrayList < ArrayList < Integer > > ();
    private  transient String numberHeight_MD5;
    private transient String numberWidth_MD5;

    public Nonogram(ArrayList < ArrayList < Integer > > Width, ArrayList <ArrayList< Integer >> Height){
        this.numberHeight = Height;
        this.numberWidth = Width;
    }
    public ArrayList <ArrayList< Integer >> getNamberHeight() {
        return numberHeight;
    }

    public ArrayList <ArrayList< Integer >> getNamberWidth() {
        return numberWidth;
    }

    public String getNumberHeight_MD5() {
        return numberHeight_MD5;
    }

    public String getNumberWidth_MD5() {
        return numberWidth_MD5;
    }

    public String toString(){
        numberHeight_MD5 = (String) getHash(numberHeight.toString().getBytes());
        numberWidth_MD5 = (String) getHash(numberWidth.toString().getBytes());
        return "Numbers for width :" + numberWidth_MD5 + "\n" +"Numbers for height :"+
                numberHeight_MD5 + "\n" ;
    }

    private static String getHash(byte[] inputBytes){
        String hashValue = "";
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageDigest.update(inputBytes);
        byte[] digestedBytes = messageDigest.digest();
        hashValue = Base64.getEncoder().encodeToString(digestedBytes);
        return hashValue;
    }

}
