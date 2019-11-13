package helper_classes.DegenerateNN;

import android.content.res.AssetManager;

import java.io.DataInputStream;
import java.io.IOException;

class Matrix {
    private int rows;
    private int columns;
    private double[][] data;

    Matrix(int rows, int columns){
        setDimensions(rows, columns);
    }

    void setDimensions(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        data = new double[this.rows][this.columns];
    }

    void copyFrom(Matrix source) {
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                data[i][j] = source.getDataAt(i,j);
            }
        }
    }

    void setDataAt(int i, int j, double data){
        this.data[i][j] = data;
    }

    double getDataAt(int i, int j){
        return data[i][j];
    }

    int getRows() {
        return rows;
    }

    int getColumns(){
        return columns;
    }

    static Matrix add(Matrix A, Matrix B){
        Matrix C;
        if(A.getRows() == B.getRows() && A.getColumns() == B.getColumns()){
            C = new Matrix(A.getRows(), A.getColumns());
            for(int i = 0; i < C.getRows(); i++){
                for(int j = 0; j < C.getColumns(); j++){
                    C.setDataAt(i,j, A.getDataAt(i,j) + B.getDataAt(i,j));
                }
            }
        } else{
            C = new Matrix(0,0);
        }

        return C;
    }

    static Matrix multiply(Matrix A, Matrix B){
        Matrix C;
        if(A.getColumns() == B.getRows()){
            C = new Matrix(A.getRows(), B.getColumns());
            for(int i = 0; i < C.getRows(); i++){
                for(int j = 0; j < C.getColumns(); j++){
                    double s = 0.0;
                    for(int k = 0; k < A.getColumns(); k++){
                        s += A.getDataAt(i,k) * B.getDataAt(k,j);
                    }
                    C.setDataAt(i,j,s);
                }
            }
        } else{
            C = new Matrix(0,0);
        }

        return C;
    }

    static Matrix multiply(double d, Matrix A){
        Matrix C = new Matrix(A.getRows(), A.getColumns());
        for(int i = 0; i < C.getRows(); i++){
            for(int j = 0; j < C.getColumns(); j++){
                C.setDataAt(i,j, d * A.getDataAt(i,j));
            }
        }

        return C;
    }

    static Matrix readFromAssets(int prefixedRows, int prefixedColumns, AssetManager assetManager, String filename){
        Matrix A = new Matrix(prefixedRows, prefixedColumns);

        DataInputStream dataInputStream = null;
        try {
            dataInputStream = (DataInputStream) assetManager.open(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < A.getRows(); i++){
            for(int j = 0; j < A.getColumns(); j++){
                double data = 0;
                try {
                    data = dataInputStream.readDouble();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                A.setDataAt(i,j,data);
            }
        }
        try {
            dataInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return A;
    }

}
