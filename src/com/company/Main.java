package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.print("Загрузить картинку? (yes или not): ");
        String answer = in.nextLine();
        if (answer.equals("yes") == true){
        System.out.println("\tВы выбрали создание кроссворда из картинки");
            System.out.print("Введите путь к файлу с расширением (например C:\\images.png): ");

            String name =in.nextLine();// путь к файлу

            System.out.print("Введите ширину создоваемого кроссворда (например 15): ");
            int Widht = in.nextInt();
            System.out.print("Введите высоту создоваемого кроссворда(например 15): ");
            int Height = in.nextInt();

            //вызываем класс который изменит картинку
            ChangePicture changePicture = new ChangePicture(name,Widht,Height);

            //получаем путь к новой измененной картинки(изменяется только название картинки)
            String fileName = changePicture.getRenderFileName();

            //создание кроссворда
            CreatCrossword creatCrossword = new CreatCrossword(fileName);

           /* fileName = fileName.substring(fileName.indexOf('/') + 1);
            fileName = fileName.substring(fileName.indexOf('/') + 1);
            fileName = fileName.substring(fileName.indexOf('/') + 1);*/

            //создание файла с сырыми данными
            // и получаем путь к новой измененной картинки(изменяется только название картинки)
            String fileRowData = creatCrossword.saveRawData(fileName);

            //сохраняем кроссворд в виде картинки и возврат строки названием файла
            String fileNonogramImage= creatCrossword.saveImage(fileName);

            //вызов класса для решения кроссворда
            NonogramSolver nonogramSolver = new NonogramSolver();

            nonogramSolver.NonogramReadRowData(fileRowData); // метод который десериализует данные кроссворда

            nonogramSolver.Run();// запуск решателя

            nonogramSolver.saveResult(fileNonogramImage);// сохранение решенного кроссворда

            System.out.println("Измененная картинка, сырые данные, кроссворд и решенный " +
                    "кроссворд находятся там же, где и картинка, путь к которой вы ранее указали, а именно: " +
                    name);
        }else if (answer.equals("not") == true){
            System.out.println("\tВы выбрали решение кроссворда из текстового файла");
            System.out.print("Введите путь к файлу с расширением (например C:\\Goat.txt): ");
            String fileTxt =in.nextLine(); // имя файла
            // для решения кроссворда из текстового файла
            NonogramSolver nonogramSolver = new NonogramSolver();
            nonogramSolver.NonogramReadTxt(fileTxt); // вызов метода, который читает кроссворд из файла а потом сохранит его в виде картинки

            nonogramSolver.Run(); // запуск решателя
            String rendFileTxt = fileTxt.substring(0, fileTxt.indexOf('.')); // создание нового имя файла
            nonogramSolver.saveResult(rendFileTxt); // сохранение решенного кроссворда
            System.out.println("Кроссворд и решенный кроссвор находятся там же, где и текстовый файл," +
                    " путь к которой вы ранее указали, а именно: " + fileTxt);
        }
    }
}
