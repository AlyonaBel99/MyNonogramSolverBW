package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class CreatCrossword {
    private static int[][] pixelArr;//массив пикселей картинки
    private static int Width,Height;// переменные ширины и высоты соотв-но
    //будет храниться кроссворд в виде чисел по х-у и по у
    ArrayList <ArrayList< Integer >> xHints =  new  ArrayList < ArrayList < Integer > > ();
    ArrayList < ArrayList < Integer > > yHints =  new  ArrayList < ArrayList < Integer > > ();

    public CreatCrossword(String picture) throws IOException {
        // создание массива пикселей и их цвета
        File file = new File(picture);
        BufferedImage imageToCrossword = ImageIO.read(file);
        this.Height = imageToCrossword.getHeight();
        this.Width = imageToCrossword.getWidth();
        pixelArr = new int[Height*Width][3];
        int pixel = 0;
        for (int x = 0; x < Width; x++) {
            for (int y = 0; y < Height; y++) {
                Color c = new Color(imageToCrossword.getRGB(x, y));
                pixelArr[pixel][0] =  c.getRed();
                pixelArr[pixel][1] = c.getGreen();
                pixelArr[pixel][2] = c.getBlue();
                pixel++;
            }
        }
        creatCross();
    }

    private void creatCross() throws IOException{
        int namberH = 1, namberW = 1;
        ArrayList<Integer> namX = new ArrayList<>();
        ArrayList<Integer> colorX = new ArrayList<>();
        ArrayList<Integer> namY = new ArrayList<>();
        ArrayList<Integer> colorY = new ArrayList<>();

        // реализация подсчета одинаковых пикселей по столбцам
        int i=0,y=0;
        boolean black=false;
        boolean strNull=false;
        do {
            int m = (Height -1) + (Height * y);
            // проверка есть ли в столбце черный пиксель
            if (pixelArr[i][0] == 0)black = true;

            // подсчет подряд идущих черных пикселей
            if((i < pixelArr.length-1)&&(i != m) && pixelArr[i][0] == pixelArr[i + 1][0] && pixelArr[i][1] == 0 ){
                namberH++;
                //System.out.print("+");
                black = true;
                strNull =true;
            }//если больше нету подряд идущих пикселей и если они были то записывается в лист
            else if(black==true){
                namY.add(namberH);
                namberH = 1;
                black =false;
            }

            // если закончился столбец
            if ((i +1) == (m+1) ) {
                // если лист не пустой записываем кроссворд
                if (strNull == true) {
                    ArrayList<Integer> Y = (ArrayList<Integer>) namY.clone();
                    xHints.add(Y);
                }
                namY.clear();
                colorY.clear();
                strNull =false;
                y++;
            }
            i++;
        }while (i < pixelArr.length);


        // реализация подсчета одинаковых пикселей по строкам
        int  x = 0;
        i=0;
        boolean blackW = false;
        boolean strNullW = false;
        do {
            int m = x + (Height * (Width -1));

            // проверка есть ли в столбце черный пиксель
            if (pixelArr[i][0] == 0) blackW = true;

            // подсчет подряд идущих черных пикселей
            if ( (i < pixelArr.length-1)&&(i != m) && pixelArr[i][0] == pixelArr[i + Height][0] && pixelArr[i][1] == 0){
                namberW++;
                blackW = true;
                strNullW =true;
            }//если больше нету подряд идущих пикселей и если они были то записывается в лист
            else if(blackW==true){
                namX.add(namberW);
                blackW =false;
                namberW = 1;
            }
            // если закончился строка
            if ((i +1) == (m+1) ) {
                // если лист не пустой записываем кроссворд
                if (strNullW == true) {
                    ArrayList<Integer> X = (ArrayList<Integer>) namX.clone();
                    yHints.add(X);
                    /*System.out.println(namX+" + "+yHints.get(xx));
                    xx++;*/
                }
                strNullW =false;
                namX.clear();
                colorX.clear();
                x++;
                if (x < Height) {
                    i = x;
                }
            }
            else {i+=Height;
            }
            //System.out.println(i);
        }while (i < pixelArr.length);

        /*System.out.print(xHints.size());
        System.out.print(yHints.size());
        for(int ii =0;ii<xHints.size();ii++){
            for(int xx = 0;xx<namberHeight[ii].length;xx++){
                System.out.print(ii+"  "+" - "+namberHeight[ii][xx]+"  ");
            }
            System.out.println(xHints.get(ii) );

        }*/
    }
    public String saveRawData(String nameFileRawData)  {
        //создание имя файла
        String renderFileName = "RowData/"+ nameFileRawData.substring(0, nameFileRawData.indexOf('.')) +"RawData" + ".dat";
        //System.out.println(renderFileName);
        // сериализация кроссворда
        Nonogram serialization = new Nonogram(yHints, xHints);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(renderFileName);

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(serialization);
        objectOutputStream.close();

    } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Файла не существует, ошибка:" + e);
        }
        //вывод сырых данных
        serialization.toString();
        return renderFileName;
    }

    public String saveImage(String nameFileImage) throws IOException {
        String renderFileName = "Nonograms/crossword/Nonogram_" + nameFileImage.substring(0, nameFileImage.indexOf('.')) ;
        //System.out.println(renderFileName);
        CreatImage creatImage = new CreatImage(yHints,xHints,renderFileName);
        renderFileName = renderFileName.substring(renderFileName.indexOf('/') + 1);
        renderFileName = renderFileName.substring(renderFileName.indexOf('/') + 1);
        return renderFileName;
    }
}
