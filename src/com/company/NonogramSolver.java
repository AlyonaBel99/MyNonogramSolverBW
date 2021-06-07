package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class NonogramSolver {
    ArrayList<ArrayList<Integer>> xHints = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> yHints = new ArrayList<ArrayList<Integer>>();
    char[][] result;


    public void NonogramReadRowData(String fileName){
        //метод в котором происходит десериализация объекта с кроссвордом
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Nonogram nonogram = (Nonogram) objectInputStream.readObject();
            objectInputStream.close();
            this.xHints = nonogram.getNamberHeight();
            this.yHints = nonogram.getNamberWidth();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Файл не найден, ошибка: "+e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Объект не существует, ошибка: "+e);
        }

        System.out.println(xHints + "\n" + yHints);
    }

    public void Run(){
        // передаем кроссворд в решатель
        Solver solver = new Solver( yHints ,xHints);
        // вызываем метод с решателем
        char[][] res = solver.resolveFullBoard();
        // вывод результата
        System.out.println("Solution:\n");
        System.out.println(solver.printBoard());
        //System.out.print(res.length);

        //возврат решенного кроссворда
        result = new char[res[0].length][res.length];
        for (int i=0;i<res.length;i++){
            for (int j=0; j<res[i].length;j++){
                result[j][i] = res[i][j];
            }
        }
    }

    public void saveResult(String file) throws IOException {

               /* for (int i=0;i<result.length;i++) {
                    for (int j = 0; j < result[i].length; j++) {
                        System.out.print(result[i][j] +" ");
                    }
                    System.out.println();
                }*/

       // System.out.print(file);
        String renderFileName = "Nonograms/solved_crossword/Result_" +file ;

        //System.out.print(renderFileName);
        CreatImage creatImage=new CreatImage(this.yHints,this.xHints,renderFileName,result);
    }


    public void NonogramReadTxt(String fileName) throws IOException
    {
        //int Column = 0,Line =0;

        BufferedReader br = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(new File("NonogramTxt/" +fileName)));
            while ((sCurrentLine = br.readLine()) != null) {
                /*if (sCurrentLine.startsWith("width")) {
                    Column = Integer.parseInt(sCurrentLine.split(" ")[1]);
                }
                if (sCurrentLine.startsWith("height")) {
                    Line = Integer.parseInt(sCurrentLine.split(" ")[1]);
                }*/
                if (sCurrentLine.startsWith("rows")) {
                    int line = 0;
                    // read all line
                    while (!(sCurrentLine = br.readLine()).equals("columns")) {
                        //int cmpt = 0;
                        yHints.add(new ArrayList<Integer>());
                        if (!sCurrentLine.equals("")) {
                            for (String element : sCurrentLine.split(",")) {
                                int parseInt = Integer.parseInt(element);
                                //cmpt += parseInt;
                                yHints.get(line).add(parseInt);
                            }
                        }
                        //this.sumLine.add(cmpt);
                        line++;

                    }
                    // read all column
                    int column = 0;
                    while ((sCurrentLine = br.readLine()) != null) {
                        //int cmpt = 0;
                        xHints.add(new ArrayList<Integer>());
                        if (!sCurrentLine.equals("")) {
                            for (String element : sCurrentLine.split(",")) {
                                int parseInt = Integer.parseInt(element);
                                //cmpt += parseInt;
                                xHints.get(column).add(parseInt);
                            }
                        }
                        //this.sumColumn.add(cmpt);
                        column++;

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        //System.out.println(Column +" " + Line);
        //System.out.println(yHints);
        //System.out.println(xHints);
        fileName = "Nonograms/crossword/"+fileName.substring(0, fileName.indexOf('.'));
        CreatImage creatImage = new CreatImage(this.yHints,this.xHints,fileName);
    }

}
