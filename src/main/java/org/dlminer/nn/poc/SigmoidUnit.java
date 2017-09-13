package org.dlminer.nn.poc;

/**
 * Created by slava on 10/09/17.
 */
public class SigmoidUnit extends Unit {



    public double computeOutput() {
        if (output != null) {
            return output;
        }
        output = 1.0 / (1.0 + Math.exp( - computeDotProduct()));
        return output;
    }


    private double computeDotProduct() {
        // threshold
        double product = weights[previousLayer.length];
        // incoming units
        for (int i=0; i<previousLayer.length; i++) {
            product += weights[i] * previousLayer[i].computeOutput();
        }
        return product;
    }



    public void updateWeights() {
        computeDelta();
        // threshold
        weights[previousLayer.length] += learningRate * delta;
        // weights
        for (int i=0; i<previousLayer.length; i++) {
            weights[i] += learningRate * delta * previousLayer[i].computeOutput();
        }
    }




    protected double computeDelta() {
        if (delta != null) {
            return delta;
        }
        double output = computeOutput();
        double error = 0;
        if (label != null) {
            error = label - output;
        } else {
            for (int i=0; i<nextLayer.length; i++) {
                Unit unit = nextLayer[i];
                error += unit.weights[i] * unit.computeDelta();
            }
        }
        delta = output * (1 - output) * error;
        return delta;
    }




}
