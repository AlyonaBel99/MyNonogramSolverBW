package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class CreatImage {
    int picsel=0;// оптимальный размер
    int Width; //ширина картинки
    int Height;// высота картинки
    int lenH;// высота шапки
    int lenW;// ширина шапки


    public CreatImage(ArrayList <ArrayList< Integer >> yHints, ArrayList < ArrayList < Integer > > xHints,
                      String  fileNameNonogram) throws IOException {
        this(yHints,xHints,fileNameNonogram,null,false);
    }

    public CreatImage(ArrayList <ArrayList< Integer >> yHints , ArrayList < ArrayList < Integer > > xHints,
                      String  fileNameNonogram, char [][] res) throws IOException {
        this(yHints,xHints,fileNameNonogram,res,true);
    }


    private CreatImage(ArrayList <ArrayList< Integer >> yHints ,
                      ArrayList < ArrayList < Integer > > xHints,
                      String  fileNameNonogram, char [][] res, boolean result) throws IOException {

        //нахождение максимального числа из 2-х листов
        int MaxNamW=maxIntrjerArrList(yHints);
        int MaxNamH=maxIntrjerArrList(xHints);
        int MaxNam = MaxNamH;
        if(MaxNamH<MaxNamW)MaxNam=MaxNamW;
        //нахождение оптимального размера используя максимальное число

        for (int n=10,m=15;n<100000;n=n*10,m+=5){
            if(MaxNam<n&&MaxNam>(n/10)) picsel=m;
        }

        //нахождение длинны шапки
        lenH = maxLengthArrList(xHints)*14;
        lenW = maxLengthArrList(yHints)*picsel;

        Width = lenW + xHints.size()*picsel + 1;
        Height = lenH + yHints.size()*picsel + 1;
        //System.out.println(xHints.size() + " - "+Width + "  " +Height);
        BufferedImage bufferedImage = new BufferedImage(Width,Height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0,0,Width,Height);

        //рисование сетки
        g2d.setColor(Color.black);
        g2d.drawLine(0,0,Width,0);
        g2d.drawLine(0,0,0,Height);
        //рисование сетки
        for (int i = lenW,j=0; i<Width; i=i+picsel,j++){
            g2d.setColor(Color.gray);
            if(j%10==0||j%10==5) g2d.setColor(Color.black);
            g2d.drawLine(i,0,i,Height);//рисовение столбцов
        }

        for (int i = lenH,j=0; i<Height; i=i+picsel,j++){
            g2d.setColor(Color.gray);
            if(j%10==0||j%10==5) g2d.setColor(Color.black);
            g2d.drawLine(0,i,Width,i);//рисовение строк
        }

        //рисование шапки
        g2d.setColor(Color.BLUE);
        //боковая часть
        int x=(lenW-picsel)+((picsel-10)/2), y=(lenH+picsel)-((picsel-10)/2);
        //System.out.println(y);
        for (int i=0;i<yHints.size();i++){
            for (int j=0;j<yHints.get(i).size();j++){
                //System.out.println(x + "  "+y + "n= "+arrH[i][j]);
                g2d.drawString(Integer.toString(yHints.get(i).get(j)),x,y);
                x-=picsel;
            }
            y+=picsel;
            x=(lenW-picsel)+((picsel-10)/2);
        }

        //верхняя часть
        x=lenW+((picsel-7)/2);
        y=lenH-((picsel-10)/2);
        //System.out.println(y);
        for (int i=0;i<xHints.size();i++){
            for (int j=0;j<xHints.get(i).size();j++){
                // System.out.println(x + "  "+y + "n= "+arrH[i][j]);
                g2d.drawString(Integer.toString(xHints.get(i).get(j)),x,y);
                y-=12;
            }
            y=lenH-((picsel-10)/2);
            x+=picsel;
        }

        if (result == true) {
            g2d.setColor(Color.black);
            //рисование кроссворда
            for (int xx = 0,yPos = lenH;xx<res.length;xx++,yPos = yPos + picsel){
                for (int yy = 0,xPos= lenW; yy<res[xx].length;yy++,xPos = xPos + picsel){
                    //System.out.print(res[xx][yy] + " ");
                    if (res[xx][yy] == 'o')
                        g2d.fillRect(xPos, yPos, picsel, picsel);//рисовение столбцов


                }
                //System.out.println();
            }
        }

        g2d.dispose();
        String fileName = fileNameNonogram + ".png";
        //System.out.println(fileName);
        File file = new File(fileName);
        ImageIO.write(bufferedImage,"png",file);

    }

    private int maxIntrjerArrList(ArrayList <ArrayList< Integer >> arrList){
        //метод для поиска максимального числа в двумерном листе
        int result = Integer.MIN_VALUE;
        for (int i =0;i< arrList.size();i++) {

            result = Math.max(result, Collections.max(arrList.get(i)));
        }
        return result;
    }
    private int maxLengthArrList(ArrayList <ArrayList< Integer >> arrList){
        int result = Integer.MIN_VALUE;
        for (int i =0;i< arrList.size();i++) {

            result = Math.max(result, arrList.get(i).size());
        }
        return result;
    }
}
