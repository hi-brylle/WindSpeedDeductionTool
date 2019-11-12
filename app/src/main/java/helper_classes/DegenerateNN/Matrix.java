package helper_classes.DegenerateNN;

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

}
