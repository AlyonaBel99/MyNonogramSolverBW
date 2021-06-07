package com.company;

import java.util.ArrayList;

public class Solver {
    private int width;
    private int height;
    private ArrayList<ArrayList<Integer>> xHints;   // массив ArrayLists (столбцы)
    private ArrayList<ArrayList<Integer>> yHints;   // массив ArrayLists (строки)
    // Для доски, '' означает пустой, 'x' означает отсутствие заливки, 'o' представляет заполненный квадрат
    public char[][] board;    // само изображение


    public Solver(ArrayList<ArrayList<Integer>> xHints, ArrayList<ArrayList<Integer>> yHints){
        this(yHints.size(), xHints.size(), xHints, yHints);
    }


    private Solver(int width, int height, ArrayList<ArrayList<Integer>> xHints, ArrayList<ArrayList<Integer>> yHints) {
        if (height != xHints.size() || width != yHints.size()) { // Размеры подсказок не совпадают с заданными параметрами
            this.width = 0;
            this.height = 0;
            this.xHints = null;
            this.yHints = null;
        } else {
            this.width = width;
            this.height = height;
            this.xHints = new ArrayList<ArrayList<Integer>>();
            for (ArrayList<Integer> item : xHints) {
                this.xHints.add(new ArrayList<Integer>(item));
            }
            this.yHints = new ArrayList<ArrayList<Integer>>();
            for (ArrayList<Integer> item : yHints) {
                this.yHints.add(new ArrayList<Integer>(item));
            }
            this.board = new char[width][height];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    this.board[x][y] = ' ';
                }
            }
        }
    }

    /** Создаем массив строк, представляющий каждую возможную перестановку
     * заданных подсказок в строке / столбце заданного размера с использованием рекурсии.
     * @param capacity TРазмер строки / столбца
     * @param hints ArrayList целых чисел, где каждое целое число представляет
     * сколько последовательных квадратов заполнено
     * @return ArrayList строк, где каждая строка состоит из символов `size`
     * и состоит только из 'o' или 'x', представляющих заполненный или пустой квадрат соответственно
     */
    private ArrayList<String> generatePermutations(int capacity, ArrayList<Integer> hints) {
        ArrayList<String> result = new ArrayList<String>();
        // Базовый случай
        if (hints.size() == 1) {
            int singleHint = hints.get(0);
            for (int i = 0; i <= (capacity - singleHint); i++) {  // # способов разместить одну группу в пробелах `capacity`
                String currString = "";
                // Добавляем ведущие x
                for (int j = 0; j < i; j++) currString += 'x';
                // Добавить кластер нулей
                for (int j = 0; j < singleHint; j++) currString += 'o';
                // Append trailing x's
                for (int j = 0; j < (capacity - i - singleHint); j++) currString += 'x';
                // Добавить в список
                result.add(currString);
            }
            return result;
        }
        // Рекурсивный шаг
        int sum = 0;
        for (int hint : hints) sum += (hint + 1);
        sum--;  // поправка на последний пробел
        for (int i = 0; i <= (capacity - sum); i++) {
            // Генерация всех возможных перестановок для всех кластеров, ЗА ИСКЛЮЧЕНИЕМ первого
            ArrayList<Integer> newHints = new ArrayList<Integer>(hints);
            newHints.remove(0);
            ArrayList<String> newPerms = generatePermutations(capacity - i - hints.get(0) - 1, newHints);
            // Используйте новые перестановки, чтобы сформировать больше перестановок
            for (String perm : newPerms) {
                String currString = "";
                // Append leading x's
                for (int j = 0; j < i; j++) currString += 'x';
                // Append cluster of o's
                for (int j = 0; j < hints.get(0); j++) currString += 'o';
                // Append at least one space
                currString += 'x';
                // Append permutation
                currString += perm;
                // Append this string to list
                result.add(currString);
            }
        }
        return result;
    }

    /** Более интересный метод, он возвращает символьный массив размеров
     * `width` и` height` сделано для представления завершенного объекта неограммы, заполненного
     * внутри с 'o' для обозначения закрашенного квадрата или 'x' для обозначения
     * пустой квадрат.
     */
    public char[][] resolveFullBoard() {
        System.out.println("Создание x перестановок ...");
        ArrayList<ArrayList<String>> xPermutations = new ArrayList<ArrayList<String>>();
        for (ArrayList<Integer> x : this.xHints) xPermutations.add(this.generatePermutations(this.width, x));
        System.out.println("Генерация y перестановок ...");
        ArrayList<ArrayList<String>> yPermutations = new ArrayList<ArrayList<String>>();
        for (ArrayList<Integer> y : this.yHints) yPermutations.add(this.generatePermutations(this.height, y));
        System.out.println("Solving...");
        this.board = new char[this.width][this.height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.board[x][y] = ' ';
            }
        }
        return this.solveBoard(this.board, xPermutations, yPermutations);
    }

    /** Вспомогательный метод для `resolveFullBoard ()`, он анализирует все возможные
     * учитывая перестановки доски и либо решает головоломку
     * алгоритмически или возвращает пустой массив, если головоломка не может быть
     * решено с заданными перестановками.
     * @param board Текущая плата
     * @param xPermutations - ArrayList строк, вероятно, предоставленный
     * Метод generatePermutations (), представляющий все потенциальные перестановки строк
     * @param yPermutations - ArrayList строк, вероятно, предоставленный
     * Метод generatePermutations (), представляющий все потенциальные перестановки столбцов
     * @return Либо решенная доска (представленная как массив двумерных символов), либо
     * пустая доска, если решения нонограммы не существует
     */
    private char[][] solveBoard(char[][] board, ArrayList<ArrayList<String>> xPermutations, ArrayList<ArrayList<String>> yPermutations) {
        while (true) {     // плохая практика, я знаю, но она работает, так что подайте на меня в суд: :\
            boolean madeChanges = false;
            // Цикл по каждой строке
            for (int i = 0; i < this.height; i++) {
                char[] temp = new char[this.width];
                // Проходим каждую перестановку
                for (String permutation : xPermutations.get(i)) {
                    // Проверяем, возможна ли перестановка на плате
                    boolean validPerm = true;
                    for (int j = 0; j < permutation.length(); j++) {
                        // Если квадрат на доске не совпадает с перестановкой, удаляем его
                        if ((board[j][i] != permutation.charAt(j)) && (board[j][i] != ' ')) {
                            validPerm = false;
                            break;
                        }
                    }
                    if (!validPerm) {
                        xPermutations.remove(permutation);
                        continue;
                    }
                    // Вычисляем, сколько квадратов добавить на доску
                    for (int j = 0; j < this.width; j++) {
                        if (temp[j] == '\0') temp[j] = permutation.charAt(j);
                        else if (temp[j] != permutation.charAt(j)) temp[j] = ' ';  // Ничего не помещать
                    }
                }
                // если все перестановки были удалены, решение невозможно; вернуть пустой
                if (xPermutations.get(i).size() == 0) return new char[0][0];
                // Вносим актуальные изменения в доску
                for (int j = 0; j < temp.length; j++) {
                    if ((temp[j] != ' ') && (board[j][i] != temp[j])) {
                        board[j][i] = temp[j];
                        madeChanges = true;
                    }
                }
            }

            System.out.println("x pass:");
            System.out.println(this.printBoard());
            System.out.println();

            // Цикл по каждому столбцу
            for (int i = 0; i < this.width; i++) {
                char[] temp = new char[this.height];
                // Проходим каждую перестановку
                for (String permutation : yPermutations.get(i)) {
                    // Проверяем, возможна ли перестановка на плате
                    boolean validPerm = true;
                    for (int j = 0; j < permutation.length(); j++) {
                        // Если квадрат на доске не совпадает с перестановкой, удаляем его
                        if ((board[i][j] != permutation.charAt(j)) && (board[i][j] != ' ')) {
                            validPerm = false;

                            break;
                        }
                    }
                    if (!validPerm) {
                        yPermutations.remove(permutation);
                        continue;
                    }
                    // Вычисляем, сколько квадратов добавить на доску
                    for (int j = 0; j < this.height; j++) {
                        if (temp[j] == '\0') temp[j] = permutation.charAt(j);
                        else if (temp[j] != permutation.charAt(j)) temp[j] = ' ';  // Don't put anything
                    }
                }
                // Если все перестановки были удалены, доска невозможна; вернуть пустой
                if (yPermutations.get(i).size() == 0) return new char[0][0];
                // Вносим актуальные изменения в доску
                for (int j = 0; j < temp.length; j++) {
                    if ((temp[j] != ' ') && (temp[j] != ' ') && (board[i][j] != temp[j])) {
                        board[i][j] = temp[j];
                        madeChanges = true;
                    }
                }
            }

            System.out.println("y pass:");
            System.out.println(this.printBoard());
            System.out.println();

            // Если одна итерация завершилась без внесения каких-либо изменений, делаем предположение
            if (!madeChanges) {
                // Никаких изменений не внесено, потому что плата решена
                boolean solved = true;
                for (int i = 0; i < this.width; i++) {
                    for (int j = 0; j < this.height; j++) {
                        if (board[i][j] == ' ') {
                            solved = false;
                            break;
                        }
                    }
                }
                if (solved) return board;
                // Никаких изменений не внесено, потому что мы должны сделать противоречие
                // (выберите случайную перестановку, чтобы она была истинной)
                boolean madeNewChange = false;
                // Цикл по строкам
                for (int i = 0; (i < this.height) && !madeNewChange; i++) {
                    while (xPermutations.get(i).size() > 1) {
                        // Если мы находимся в этом цикле, будет сделано как минимум одно новое изменение
                        madeNewChange = true;
                        // Заполняем предполагаемый квадрат на доске
                        String permutation = xPermutations.get(i).get(0);
                        for (int j = 0; j < permutation.length(); j++) {
                            board[j][i] = permutation.charAt(j);
                        }
                        // Создаем временные параметры
                        char[][] tempBoard = new char[this.width][this.height];
                        for (int x = 0; x < this.width; x++) {
                            for (int y = 0; y < this.height; y++) {
                                tempBoard[x][y] = board[x][y];
                            }
                        }
                        ArrayList<ArrayList<String>> tempXPerms = new ArrayList<ArrayList<String>>();
                        for (ArrayList<String> a : xPermutations) {
                            ArrayList<String> tempA = new ArrayList<String>();
                            for (String s : a) {
                                tempA.add(new String(s));
                            }
                            tempXPerms.add(tempA);
                        }
                        ArrayList<ArrayList<String>> tempYPerms = new ArrayList<ArrayList<String>>();
                        for (ArrayList<String> a : xPermutations) {
                            ArrayList<String> tempA = new ArrayList<String>();
                            for (String s : a) {
                                tempA.add(new String(s));
                            }
                            tempYPerms.add(tempA);
                        }
                        char[][] unsolvable = new char[this.width][this.height];
                        // Если доска решена из этого, вернуть его
                        if (this.solveBoard(tempBoard, tempXPerms, tempYPerms).length != 0) return tempBoard;
                        // В противном случае удалите его из коллекции и попробуйте новую перестановку
                        xPermutations.get(i).remove(permutation);
                    }
                }
                // Цикл по столбцам
                for (int i = 0; (i < this.width) && !madeNewChange; i++) {
                    while (yPermutations.get(i).size() > 1) {
                        // Если мы находимся в этом цикле, будет сделано как минимум одно новое изменение
                        madeNewChange = true;
                        // Заполняем предполагаемый квадрат на доске
                        String permutation = yPermutations.get(i).get(0);
                        for (int j = 0; j < permutation.length(); j++) {
                            board[j][i] = permutation.charAt(j);
                        }
                        // Создаем временные параметры
                        char[][] tempBoard = new char[this.width][this.height];
                        for (int x = 0; x < this.width; x++) {
                            for (int y = 0; y < this.height; y++) {
                                tempBoard[x][y] = board[x][y];
                            }
                        }
                        ArrayList<ArrayList<String>> tempXPerms = new ArrayList<ArrayList<String>>();
                        for (ArrayList<String> a : xPermutations) {
                            ArrayList<String> tempA = new ArrayList<String>();
                            for (String s : a) {
                                tempA.add(new String(s));
                            }
                            tempXPerms.add(tempA);
                        }
                        ArrayList<ArrayList<String>> tempYPerms = new ArrayList<ArrayList<String>>();
                        for (ArrayList<String> a : xPermutations) {
                            ArrayList<String> tempA = new ArrayList<String>();
                            for (String s : a) {
                                tempA.add(new String(s));
                            }
                            tempYPerms.add(tempA);
                        }
                        char[][] unsolvable = new char[this.width][this.height];
                        // Если доска решена из этого, вернуть его
                        if (this.solveBoard(tempBoard, tempXPerms, tempYPerms).length != 0) return tempBoard;
                        // В противном случае удалите его из коллекции и попробуйте новую перестановку
                        yPermutations.get(i).remove(permutation);
                    }
                }
            }
        }
    }

    /** Метод возврата для свойства width доски nonogram
     * @return Целочисленное значение ширины доски nonogram
     */
    public int getWidth() {
        return this.width;
    }

    /** Метод возврата для свойства height, AKA height доски nonogram
     * @return Целочисленное значение высоты доски nonogram
     */
    public int getHeight() {
        return this.height;
    }

   /* *//** Метод установки для одного из xHints платы в случае, если была введена одна подсказка
     * неправильно.
     * @param newHints - список целых чисел, представляющий новые подсказки для замены.
     * @param index Строка для замены подсказок (например, board [0] будет
     * соответствуют индексу 1)
     *//*
    public void setXHint(int index, ArrayList<Integer> newHints) {
        this.xHints.set(index - 1, newHints);
    }*/

    /** Метод установки для одного из yHints платы в случае, если была введена одна подсказка
     * неправильно.
     * @param newHints - список целых чисел, представляющий новые подсказки для замены.
     * @param index Столбец, который нужно заменить подсказками (например, board [0] будет
     * соответствуют индексу 1)
     *//*
    public void setYHint(int index, ArrayList<Integer> newHints) {
        this.yHints.set(index - 1, newHints);
    }*/

    /** Метод печати текущей доски нонограммы. Это будет либо пусто
     * или полностью решено, в зависимости от того, был ли вызван метод `resolveFullBoard ()`
     * @return Строковое представление доски nonogram
     */
    public String printBoard() {
        String result = new String();
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                switch (this.board[x][y]) {
                    case 'o':
                        result += "\u2593\u2593";
                        break;
                    case 'x':
                        result += "\u2591\u2591";
                        break;
                    default:
                        result += "??";
                }
            }
            result += '\n';
        }
        return result;
    }

   /* // Debug method
    public void printItems() {
        System.out.println("width:\t" + this.width + "\nheight:\t" + this.height);
        System.out.println("xHints: ");
        for (ArrayList<Integer> hintList : this.xHints) {
            System.out.print("\t");
            for (Integer hintNum : hintList) {
                System.out.print(hintNum + " ");
            }
            System.out.println("");
        }
        System.out.println("yHints:");
        for (ArrayList<Integer> hintList : this.yHints) {
            System.out.print("\t");
            for (Integer hintNum : hintList) {
                System.out.print(hintNum + " ");
            }
            System.out.println("");
        }
    }*/

   /* // Another debug method
    public void printPermutations() {
        System.out.println("Permutations:");
        for (ArrayList<Integer> row : this.xHints) {
            System.out.print("For hint ");
            for (int i : row) System.out.print(i + " ");
            System.out.println(",");
            ArrayList<String> myPerms = this.generatePermutations(this.getWidth(), row);
            for (String perm : myPerms) {
                System.out.println("\t" + perm);
            }
        }
        for (ArrayList<Integer> col : this.yHints) {
            System.out.print("For hint ");
            for (int i : col) System.out.print(i + " ");
            System.out.println(",");
            ArrayList<String> myPerms = this.generatePermutations(this.getHeight(), col);
            for (String perm : myPerms) {
                System.out.println("\t" + perm);
            }
        }
    }*/
}

