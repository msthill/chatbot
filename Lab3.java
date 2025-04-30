//COSC 20203
//Name: Micah St.Hill
//Date: 03/13/2025
//Email: micah.sthill@tcu.edu
//Assignment: Lab 3 - Sudoku Solver

// Imports java.util.Scanner to use instances of scanner to read input from the console.
import java.util.Scanner;

public class Lab3
{
   public static void main(String[] args)
   {
    // Creates a new instance of Scanner to read input from the console.
       Scanner input = new Scanner(System.in);
       //Reads the first line of input and parses it as an integer to get the number of puzzles.
       int numberOfPuzzles = Integer.parseInt(input.nextLine());
       // Initializes a variable to keep track of the current puzzle number.
       int currentPuzzle = 1;

       // Loops through each puzzle until the number of puzzles is reached.
       while(currentPuzzle <= numberOfPuzzles)
       {
           String board = "";
           // Reads the next 9 lines of input to get the Sudoku board.
           int row = 0;
           // Loops through each row of the Sudoku board until 9 rows are read.
           while(row < 9)
           {
               board += input.nextLine();
               row++;
           }
           // Calls the solveIt method to solve the Sudoku puzzle and get the number of solutions.
           String result = solveIt(board, 0, 0, "");

           // Checks the result of the solveIt method to determine if the puzzle has a solution or not.
           if(result.equals("0"))
           {
           // If the result is "0", it means the puzzle has no solution.
               System.out.println("Puzzle " + currentPuzzle + " has no solution");
           }
           //If the result is "1", it will print the exact solution
           else if(result.length() > 1 && result.charAt(1) == ':')
           {
               System.out.println("Puzzle " + currentPuzzle + " solution is");
               printBoard(result.substring(2));
           }
           //If the result is greater than 1, it will print the number of solutions
           else
           {
               System.out.println("\nPuzzle " + currentPuzzle + " has " + result + " solutions");
           }
           currentPuzzle++;
       }
   }

   //This method solves the Sudoku puzzle using recursion and returns the number of solutions found.
   public static String solveIt(String board, int currentIndex, int numberOfSolutions, String solution)
   {
    // Checks if the current index is at the end of the board (81).
       if (currentIndex == 81)
       {
        // If the number of solutions is 0, it means a solution has been found.
           String result = String.valueOf(numberOfSolutions + 1);
           if (numberOfSolutions == 0)
           {
               result += ":" + board;
           }
           return result;
       }
 
       // Checks if the current index is not empty (not an underscore).
       if (board.charAt(currentIndex) != '_')
       {
        // If the current index is not empty, it means a digit is already placed.
        // So, it will call the solveIt method recursively with the next index.
           return solveIt(board, currentIndex + 1, numberOfSolutions, solution);
       }
 
       // Calculates the row and column of the current index.
       // The row is calculated by dividing the current index by 9, and the column is calculated by taking the modulus of the current index by 9.
       int row = currentIndex / 9;
       int col = currentIndex % 9;


       char digit = '1';
       
       // Loops through the digits from '1' to '9' to check for valid placements.
       while (digit <= '9')
       {
           boolean isValid = true;
 
           //
           int i = 0;
           while (i < 9)
           {
            // Checks if the digit is already present in the current row or column.
            // If it is, set isValid to false and break the loop.
               if (board.charAt(row * 9 + i) == digit || board.charAt(i * 9 + col) == digit)
               {
                   isValid = false;
                   break;
               }
               i++;
           }
 
           // Checks if the digit is already present in the current 3x3 box.
           if (isValid)
           {
            // The boxRow and boxCol variables are calculated to determine the starting index of the 3x3 box.
            // The boxRow is calculated by dividing the row by 3 and multiplying it by 3, and the boxCol is calculated similarly for the column.
               int boxRow = (row / 3) * 3;
               int boxCol = (col / 3) * 3;
               int r = 0;
               // Loops through the 3x3 box to check if the digit is already present.
               // The loop iterates through the rows (r) and columns (c) of the box.
               while (r < 3)
               {
                   int c = 0;
                   while (c < 3)
                   {
                    // Checks if the digit is already present in the current box.
                    // If it is, set isValid to false and break the loop.
                       int checkIndex = (boxRow + r) * 9 + (boxCol + c);
                       if (board.charAt(checkIndex) == digit)
                       {
                           isValid = false;
                           break;
                       }
                       c++;
                   }
                   // Checks if isValid is false, if it is, break the loop.
                   // This is to avoid unnecessary iterations if the digit is already found in the box.
                   if (!isValid) break;
                   r++;
               }
           }
 
           if (isValid)
           {
            // If the digit is valid, it will create a new board with the digit placed at the current index.
            // It will then call the solveIt method recursively with the next index.
               String nextBoard = board.substring(0, currentIndex) + digit + board.substring(currentIndex + 1);
               String result = solveIt(nextBoard, currentIndex + 1, numberOfSolutions, solution);
 
               // Checks if the result contains a colon (":").
               // If it does, it means a solution has been found.
               if (result.contains(":"))
               {
                   int split = result.indexOf(':');
                   numberOfSolutions = Integer.parseInt(result.substring(0, split));
                   solution = result.substring(split + 1);
               }
               // If the result does not contain a colon, it means the number of solutions has been incremented.
               // It will parse the result to get the number of solutions.
               else
               {
                   numberOfSolutions = Integer.parseInt(result);
               }
           }
 
           digit++;
       }

       // If no valid placements are found, return the number of solutions found so far.
       // If the number of solutions is 0, it means no solution has been found.
       String result = String.valueOf(numberOfSolutions);
       if (numberOfSolutions == 1) {
           result += ":" + solution;
       }
       return result;
   }

   // This method prints the Sudoku board in a formatted way.
   // It takes a string representation of the board as input and prints it in a 9x9 grid format.
   public static void printBoard(String board)
   {
       for(int i = 0; i < 81; i++)
       {
           System.out.print(board.charAt(i));
           if(i % 9 == 8)
           {
               System.out.println();
           }
       }
   }
}

