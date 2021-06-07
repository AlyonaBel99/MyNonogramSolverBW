package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.print("Загрузить картинку? (да или нет): ");
        String answer = in.nextLine();
        if (answer.equals("да") == true){
        System.out.println("\tВы выбрали создание кроссворда из картинки");
            System.out.print("Введите имя файла с расширением (например images.png): ");
            String name =in.nextLine();// имя файла
            System.out.print("Введите ширину создоваемого кроссворда (например 15): ");
            int Widht = in.nextInt();
            System.out.print("Введите высоту создоваемого кроссворда(например 15): ");
            int Height = in.nextInt();
            //вызываем класс который изменит картинку
            ChangePicture changePicture = new ChangePicture(name,Widht,Height);
            //получаем путь новой измененной картинки
            String fileName = changePicture.getRenderFileName();
            //создание кроссворда
            CreatCrossword creatCrossword = new CreatCrossword(fileName);
            //создание файла с сырыми данными  и возврат строки с названием файла
            fileName = fileName.substring(fileName.indexOf('/') + 1);
            fileName = fileName.substring(fileName.indexOf('/') + 1);
            String fileRowData = creatCrossword.saveRawData(fileName);
            //сохраняем кроссворд в виде картинки и возврат строки названием файла
            String fileNonogramImage= creatCrossword.saveImage(fileName);
            //System.out.print(fileNonogramImage);
            //вызов класса для решения кроссворда
            NonogramSolver nonogramSolver = new NonogramSolver();
            nonogramSolver.NonogramReadRowData(fileRowData); // метод который десериализует данные кроссворда
            nonogramSolver.Run();// запуск решателя
            nonogramSolver.saveResult(fileNonogramImage);// сохранение решенного кроссворда
            System.out.println("Измененая картика лежит в папке Pictures\\Modified_pictures\n" +
                    "Кроссворд с сырыми данными лежит в папке RowData\n" +
                    "Кроссворд лежит в папке Nonograms\\crossword\n" +
                    "Решенный кроссворд лежит в папке Nonograms\\solved_crossword");
        }else if (answer.equals("нет") == true){
            System.out.println("\tВы выбрали решение кроссворда из текстового файла");
            System.out.print("Введите имя файла с расширением (например Goat.txt): ");
            String fileTxt =in.nextLine(); // имя файла
            // для решения кроссворда из текстового файла
            NonogramSolver nonogramSolver = new NonogramSolver();
            nonogramSolver.NonogramReadTxt(fileTxt); // вызов метода, который читает кроссворд из файла

            nonogramSolver.Run(); // запуск решателя
            String rendFileTxt = fileTxt.substring(0, fileTxt.indexOf('.')); // создание нового имя файла
            nonogramSolver.saveResult(rendFileTxt); // сохранение решенного кроссворда
            System.out.println("Кроссворд лежит в папке Nonograms\\crossword\n" +
                    "Решенный кроссворд лежит в папке Nonograms\\solved_crossword");
        }
    }
}
