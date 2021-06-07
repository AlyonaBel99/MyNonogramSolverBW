package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ChangePicture {
    public String renderFileName;

    public ChangePicture(String fileName, int changeWidht,int changeHeight){
        try {

            // Открываем изображение
        File file = new File("doc/Pictures/"+ fileName);
        BufferedImage source = null;
            source = ImageIO.read(file);

        // вызываем метод для уменьшения картинки
        BufferedImage res = Resize(source,changeWidht,changeHeight);

        // вызываем метод для изменения цвета картинки в чб
        BufferedImage im = CollorWB(res);

        //создаем название новой картинки



        renderFileName =  "doc/Pictures/Modified_pictures/"+fileName.substring(0, fileName.indexOf('.')) +"Render" + ".png";
        // сохраняем картинку
        ImageIO.write(im, "PNG", new File(renderFileName));

        // запись в виде 0 и 1 картинки в текстовый файл
        /*int sourceArr[][] = new int[im.getWidth()][im.getHeight()];
            int count = 0;
            for(int i=0; i<im.getWidth(); i++) {
                for(int j=0; j<im.getHeight(); j++) {
                    count++;
                    Color c = new Color(im.getRGB(i,j));
                    if(c.getRed() < 100 && c.getGreen() < 100 && c.getBlue() <100 )
                        sourceArr[i][j] = 0;
                    else
                        sourceArr[i][j] = 1;
                    //System.out.println(  "  S.No: " + count + "  " + c +  "  "  + sourceArr[i][j]);
                }*/

                /*try {
                    File output = new File("pixel.txt");
                    if(file.exists())
                        file.createNewFile();
                    PrintWriter pw = new PrintWriter(output);

                    for(int l=0; l<sourceArr[l].length; l++) {
                        for(int j=0; j<sourceArr.length; j++) {
                       pw.print(sourceArr[j][l]);
                        }
                        pw.print("\n");
                    }
                    pw.close();

                }catch (IOException e){
                            System.out.println("Ошибка создания или записи: " + e);
                        }
            }*/



        } catch (IOException e) {
            // При открытии и сохранении файлов, может произойти неожиданный случай.
            // И на этот случай у нас try catch
            System.out.println("Файл не найден или не удалось сохранить");
        }
    }

    public String getRenderFileName(){return renderFileName;}

    private BufferedImage  CollorWB(BufferedImage source){
        // изменение цвета картинки в чб
        BufferedImage im = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        // Get the graphics context for the black-and-white image.
        Graphics2D g2d = im.createGraphics();
        // Render the source image on it.
        g2d.drawImage(source, 0, 0, null);
        return im;
    }

    private BufferedImage Resize(BufferedImage source, int changeWidht, int changeHeight){
        //  уменьшаем картинку
        BufferedImage changeSource = new BufferedImage(changeWidht, changeHeight, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) changeSource.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(source, 0, 0, changeWidht,changeHeight, null);
        g2d.dispose();
        return changeSource;
    }
}
