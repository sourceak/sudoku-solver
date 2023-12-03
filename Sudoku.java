import java.io.*;   // allows us to read from a file
import java.util.*;

public class Sudoku {    
    // The current contents of the cells of the puzzle. 
    private int[][] grid;
    
    private boolean[][] valIsFixed;
    
    private boolean[][][] subgridHasVal;
    
    private int[] rowHasVal;
    private int[] colHasVal;
    
    public Sudoku() {
        this.grid = new int[9][9];
        this.valIsFixed = new boolean[9][9];
        this.subgridHasVal = new boolean[3][3][10];        

        /*** INITIALIZE YOUR ADDITIONAL FIELDS HERE. ***/
        this.rowHasVal = new int[81];
        this.colHasVal = new int[81];
    }
    
    public void placeVal(int val, int row, int col) {
        //System.out.println("placed");
        this.grid[row][col] = val;
        this.subgridHasVal[row/3][col/3][val] = true;


        
        /*** UPDATE YOUR ADDITIONAL FIELDS HERE. ***/
        this.rowHasVal[9 * row + col] = val;
        this.colHasVal[9 * col + row] = val;
           
    }
       
    public void removeVal(int val, int row, int col) {
        //System.out.println("removed");
        this.grid[row][col] = 0;
        this.subgridHasVal[row/3][col/3][val] = false;
        
        /*** UPDATE YOUR ADDITIONAL FIELDS HERE. ***/
        this.rowHasVal[9 * row + col] = 0;
        this.colHasVal[9 * col + row] = 0;
    }  
       
    public void readConfig(Scanner input) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int val = input.nextInt();
                this.placeVal(val, r, c);
                if (val != 0) {
                    this.valIsFixed[r][c] = true;
                }
            }
            input.nextLine();
        }
    }
                
    /*
     * Displays the current state of the puzzle.
     */        
    public void printGrid() {
        for (int r = 0; r < 9; r++) {
            this.printRowSeparator();
            for (int c = 0; c < 9; c++) {
                System.out.print("|");
                if (this.grid[r][c] == 0) {
                    System.out.print("   ");
                } else {
                    System.out.print(" " + this.grid[r][c] + " ");
                }
            }
            System.out.println("|");
        }
        this.printRowSeparator();
    }
        
    // A private helper method used by display() 
    // to print a line separating two rows of the puzzle.
    private static void printRowSeparator() {
        for (int i = 0; i < 9; i++) {
            System.out.print("----");
        }
        System.out.println("-");
    }
    
    private boolean checkCell(int val, int row, int col){
       
        if(this.subgridHasVal[row/3][col/3][val]){
            //System.out.println("Failed subgrid: [row/3][col/3][val] T/F" + row/3 + " " + col/3 + " " + val + " " + this.subgridHasVal[row/3][col/3][val]);
            return false;
        }
       

        for(int i=0; i < 9; i++){
            if(rowHasVal[row * 9 + i] == val){
                //System.out.println("value found in row " + Arrays.toString(rowHasVal));
                //System.out.println("Failed row: [rowN] rowHasVal[i]: " + row * 9 + i + " " + rowHasVal[row * 9 + i]);
                return false;
            }
        }

        for(int i=0; i < 9; i++){
            if(colHasVal[col * 9 + i] == val){
                //int colIndex = col * 9 + i;
                //System.out.println("value found in col " + Arrays.toString(colHasVal));
                //System.out.println("Failed col: [colN] colHasVal[i]: " + colIndex + " " + colHasVal[col * 9 + i]);
                return false;
            }
        }


        
        return true;
    }

    private boolean solveRB(int n) {
        //System.out.println("solveRB called");
        
        if(n >= this.rowHasVal.length){
            return true;
        }
        int rowN = n/9;
        int colN = n%9;

        //System.out.println("row and col: " + rowN + " " + colN);


        if (valIsFixed[rowN][colN]) {
            return solveRB(n + 1);
        }
        //System.out.println("current: " + rowN + " " + colN);
        for(int i=1; i<10; i++){

           
            if(checkCell(i, rowN, colN)){
                //System.out.println("passed check [val][rowN][colN]: " + i + " " + rowN + " " + colN);
                this.placeVal(i, rowN, colN);

                
                if(this.solveRB(n+1)){
                    //System.out.println("Failed valFixed: [rowN][colN] T/F" + rowN + " " + colN + " " + this.valIsFixed[rowN][colN]);
                    return true;
                }

                this.removeVal(i, rowN, colN);
            }
            else{
                //System.out.println("failed check [val][rowN][colN]: " + i + " " + rowN + " " + colN);
            }
        }    
        return false;
    } 
    
    /*
     * public "wrapper" method for solveRB().
     * Makes the initial call to solveRB, and returns whatever it returns.
     */
    public boolean solve() { 
        boolean foundSol = this.solveRB(0);
        return foundSol;
    }
    
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Sudoku puzzle = new Sudoku();
        
        System.out.print("Enter the name of the puzzle file: ");
        String filename = scan.nextLine();
        
        try {
            Scanner input = new Scanner(new File(filename));
            puzzle.readConfig(input);
        } catch (IOException e) {
            System.out.println("error accessing file " + filename);
            System.out.println(e);
            System.exit(1);
        }
        
        System.out.println();
        System.out.println("Here is the initial puzzle: ");
        puzzle.printGrid();
        System.out.println();
        
        if (puzzle.solve()) {
            System.out.println("Here is the solution: ");
        } else {
            System.out.println("No solution could be found.");
            System.out.println("Here is the current state of the puzzle:");
        }
        puzzle.printGrid();  
    }    
}
