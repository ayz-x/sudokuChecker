public class SudokuChecker {

    public static void main(String[] args) {
        //test cases
        int[][] solved = { //should return true
                {5, 3, 4, 6, 7, 8, 9, 1, 2},
                {6, 7, 2, 1, 9, 5, 3, 4, 8},
                {1, 9, 8, 3, 4, 2, 5, 6, 7},
                {8, 5, 9, 7, 6, 1, 4, 2, 3},
                {4, 2, 6, 8, 5, 3, 7, 9, 1},
                {7, 1, 3, 9, 2, 4, 8, 5, 6},
                {9, 6, 1, 5, 3, 7, 2, 8, 4},
                {2, 8, 7, 4, 1, 9, 6, 3, 5},
                {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };
        int[][] wrong = { //should return false
                {5, 3, 4, 6, 7, 8, 9, 1, 5}, // row 0: duplicate 5
                {6, 7, 2, 1, 9, 5, 3, 4, 6}, // row 1: duplicate 6
                {1, 9, 8, 3, 4, 2, 5, 6, 7}, // row 2: ok
                {8, 5, 9, 7, 6, 1, 4, 2, 3}, // row 3: ok
                {4, 2, 6, 8, 5, 3, 7, 9, 2}, // row 4: duplicate 2
                {7, 1, 3, 9, 2, 4, 8, 5, 7}, // row 5: duplicate 7
                {9, 6, 1, 5, 3, 7, 2, 8, 4}, // row 6: ok
                {2, 8, 7, 4, 1, 9, 6, 3, 8}, // row 7: duplicate 8
                {3, 4, 5, 2, 8, 6, 1, 7, 9}  // row 8: ok
        };

        System.out.println(puzzleIsSolved(solved));
        System.out.println(puzzleIsSolved(wrong));

    }
    //Precondition: grid is 9x9
    public static boolean puzzleIsSolved(int[][] grid){

        //lines
        for (int i = 0; i < 9; i++) {
            //horizontal
            int[] row = new int[9];
            for (int j = 0; j < 9; j++) { //can avoid this part entirely if you put the code in directly instead of using method
                row[j] = grid[i][j];
            }
            if(!checkWithPrimeProducts(row)){ //alternatively can use checkWithPrimeProducts(grid[i])
                return false;
            }

            //vertical
            int[] column = new int[9];
            for (int j = 0; j < 9; j++) { //can avoid this part entirely if you put the code in directly instead of using method
                column[j] = grid[j][i];
            }
            if(!checkWithCount(column)){
                return false;
            }
        }

        //squares
        /* 1 for loop per square implementation:
            for(int i = 0; i < 9; i++){ //big square
                int bigR = (i/3)*3;
                int bigC = (1%3)*3;

                int[] square = new int[9]; //can avoid this part entirely if you put the code in directly instead of using method
                for(int j = 0; j < 9; j++){ //small square
                    int r = (j/3)*3;
                    int c = (j%3)*3;
                    square[j] = grid[r][c];
                }
            }
            //however, this is actually more operations because we need to calculate r and c
            //less writing if you're on a paper test tho
        */
        //two for loops per square:

        for (int rr = 0; rr < 9; rr+=3) { //big square row
            for (int cc = 0; cc < 9; cc+=3) { //big square column
                int[] square = new int[9]; //can avoid this part entirely if you put the code in directly instead of using method
                int index = 0;
                for (int r = rr; r < rr+3; r++) { //small square row
                    for (int c = cc; c < cc+3; c++) { //small square column
                        square[index] = grid[r][c];
                        index++;
                    }
                }
                if(!checkWithBinary(square)){
                    return false;
                }
            }
        }


        return true; //absolutely no duplicates
    }


    //methods to detect dupes in an int array, all O(n)


    //precondition: in list, if n is max num in list and m is list.length, n^m must be less than 2^63-1, n>0
    public static boolean checkWithPrimeProducts(int[] list){
        int[] primes = {2,3,5,7,11,13,17,19,23}; //maps each number from 1-9 to a prime
        long product = 1L; //must use long because 23^9 exceeds integer limit in worst case
        //longs must have "L" at the end, that's why it's there
        //longs are just a primitive type that can store larger numbers than int can
        for (int x : list) {//x represents the num on sudoku board
            int index = x - 1; //-1 because array indexes start at 0
            product *= primes[index];
        }
        return product == 223092870L;
    }

    //precondiiton: list must not contain a number x outside of 0<x<10
    //however if needed it can be easily coded in
    public static boolean checkWithCount(int[] list){
        int[] count = new int[9];
        for (int x : list) {
            int index = x - 1; //x represents the num on sudoku board
            count[index]++;
            if (count[index] > 1) { //if that number appeared more than once
                return false;
            }
        }
        return true;
    }

    //precondition: list.length cannot exceed 31, 0<n<32
    public static boolean checkWithBinary(int[] list){
        int sum = 0;
        for (int x : list) { //x represents the num on sudoku board
            sum += (int) Math.pow(2,x);
        }
        return sum == 1022; //equal to 2^10 - 2, or 1111111110 in binary (we don't have a 0 in sudoku so the 2^0 isn't added)
    }

}
