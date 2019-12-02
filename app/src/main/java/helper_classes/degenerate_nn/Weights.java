package helper_classes.degenerate_nn;

public class Weights {
    private Matrix weights;
    private Matrix biases;

    Weights(){}

    void initialize(int followingLayerSize, int precedingLayerSize){
        weights = new Matrix(followingLayerSize, precedingLayerSize);
        biases = new Matrix(followingLayerSize, 1);
    }

    void setWeights(Matrix trainedWeights){
        weights.copyFrom(trainedWeights);
    }

    public void setBiases(Matrix trainedBiases){
        biases.copyFrom(trainedBiases);
    }

    Matrix getWeights() {
        return weights;
    }
    Matrix getBiases() {
        return biases;
    }
}
