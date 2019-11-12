package helper_classes.DegenerateNN;

public class Weights {
    private Matrix weights;
    private Matrix biases;

    Weights(){}

    void initialize(int followingLayerSize, int precedingLayerSize/*, Matrix trainedWeights, Matrix trainedBiases*/){
        weights.setDimensions(followingLayerSize, precedingLayerSize);
        biases.setDimensions(followingLayerSize, 1);

        /*weights.copyFrom(trainedWeights);
        biases.copyFrom(trainedBiases);*/
    }

    public Matrix getWeights() {
        return weights;
    }
    public Matrix getBiases() {
        return biases;
    }
}
