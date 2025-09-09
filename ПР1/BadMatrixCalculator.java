import java.util.Arrays;
import java.util.Scanner;

public class BadMatrixCalculator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Matrix Calculator");
        while (true) {
            System.out.println("\nChoose an action:");
            System.out.println("1. Enter a matrix");
            System.out.println("2. Display a matrix");
            System.out.println("3. Negate");
            System.out.println("4. Multiply by scalar");
            System.out.println("5. Transpose");
            System.out.println("6. Add");
            System.out.println("7. Subtract");
            System.out.println("8. Multiply by matrix");
            System.out.println("0. Exit");

            int choice = readInt(sc, "Choice: ");

            if (choice == 0) break;

            switch (choice) {
                case 1:
                    System.out.println("Enter the matrix:");
                    Matrix m = readMatrix(sc);
                    System.out.println("Matrix created:");
                    System.out.println(m);
                    break;
                case 2:
                    Matrix m2 = readMatrix(sc);
                    System.out.println("Entered matrix:");
                    System.out.println(m2);
                    break;
                case 3: {
                    Matrix a = readMatrix(sc);
                    System.out.println("Result:");
                    System.out.println(a.negate());
                    break;
                }
                case 4: {
                    Matrix a = readMatrix(sc);
                    double k = readDouble(sc, "Enter scalar: ");
                    System.out.println("Result:");
                    System.out.println(a.multiply(k));
                    break;
                }
                case 5: {
                    Matrix a = readMatrix(sc);
                    System.out.println("Transposed:");
                    System.out.println(a.transpose());
                    break;
                }
                case 6: {
                    System.out.println("First matrix:");
                    Matrix a = readMatrix(sc);
                    System.out.println("Second matrix:");
                    Matrix b = readMatrix(sc);
                    System.out.println("Result:");
                    System.out.println(a.add(b));
                    break;
                }
                case 7: {
                    System.out.println("First matrix:");
                    Matrix a = readMatrix(sc);
                    System.out.println("Second matrix:");
                    Matrix b = readMatrix(sc);
                    System.out.println("Result:");
                    System.out.println(a.subtract(b));
                    break;
                }
                case 8: {
                    System.out.println("First matrix:");
                    Matrix a = readMatrix(sc);
                    System.out.println("Second matrix:");
                    Matrix b = readMatrix(sc);
                    System.out.println("Result:");
                    System.out.println(a.multiply(b));
                    break;
                }
                default:
                    System.out.println("Invalid choice.");
            }
        }
        sc.close();
        System.out.println("Exit.");
    }

    /// reading next scanner token until it's int
    private static int readInt(Scanner sc, String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print(prompt);
        }
        return sc.nextInt();
    }

    /// reading next scanner token until it's double
    private static double readDouble(Scanner sc, String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextDouble()) {
            sc.next();
            System.out.print(prompt);
        }
        return sc.nextDouble();
    }

    /// creating matrix from user input, row by row
    private static Matrix readMatrix(Scanner sc) {
        int r = readInt(sc, "Number of rows: ");
        int c = readInt(sc, "Number of columns: ");
        double[][] data = new double[r][c];
        System.out.println("Enter elements row by row (space separated):");
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                data[i][j] = readDouble(sc, String.format("a[%d][%d]= ", i, j));
            }
        }
        return new Matrix(data);
    }
}

/// class for constructing and operating with matrices
class Matrix {
    final int rows;
    final int cols;
    private final double[][] matrix; //variable representing complete matrix

    public double get(int i, int j) { return matrix[i][j]; }
    public void set(int i, int j, double v) { matrix[i][j] = v; }

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.matrix = new double[rows][cols];
    }

    /// a.k.a. copy constructor
    public Matrix(double[][] data) {
        this.rows = data.length;
        this.cols = (rows == 0) ? 0 : data[0].length;
        this.matrix = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            // throw an exception if not all string are the same size
            if (data[i].length != cols) throw new IllegalArgumentException("Invalid matrix shape.");
            // copy an entire array from data[i] to resulting matrix[i] (filling matrix)
            System.arraycopy(data[i], 0, this.matrix[i], 0, cols);
        }
    }

    /// -1 * matrix
    public Matrix negate() {
        Matrix m = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                m.matrix[i][j] = matrix[i][j] < 0 ? -matrix[i][j] : matrix[i][j];
        return m;
    }

    /// matrix * number
    public Matrix multiply(double k) {
        Matrix m = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                m.matrix[i][j] = i == j ? matrix[i][j] : matrix[i][j] * k;
        return m;
    }

    /// transpose matrix
    public Matrix transpose() {
        Matrix m = new Matrix(cols, rows);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                m.matrix[j][i] = matrix[i][j];
        return m;
    }

    /// addition and subtraction method. Subtraction is basically an addition to a number multiplied by -1,
    /// so I made a single method for both operations
    private Matrix addSubtract(Matrix other, double factor) {
        if (rows > other.rows || cols > other.cols)
            throw new IllegalArgumentException("Sizes of matrices does not match.");
        Matrix m = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                m.matrix[i][j] = this.matrix[i][j] + factor * other.matrix[i][j];
        return m;
    }

    /// API readability.
    public Matrix add(Matrix other) {return addSubtract(other, 1.0);}
    public Matrix subtract(Matrix other) {return addSubtract(other, -1.0);}

    /// matrix * matrix
    public Matrix multiply(Matrix other) {
        if (this.cols != other.rows) throw new IllegalArgumentException("Number of columns of first matrix must equal number of rows of second.");
        Matrix m = new Matrix(this.rows, other.cols);
        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                double sum = 0.0;
                for (int k = 0; k < this.cols; k++) 
                    sum += this.matrix[k][j] * other.matrix[i][k];
                m.matrix[i][j] = sum;
            }
        }
        return m;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (double[] row : matrix) {
            sb.append(Arrays.toString(row)).append("\n");
        }
        return sb.toString();
    }
}