package org.dlminer.nn.poc;

/**
 * Created by slava on 10/09/17.
 */
public abstract class Unit {

    protected Unit[] nextLayer;

    protected Unit[] previousLayer;

    protected double[] weights;

    protected Double learningRate;

    protected Double delta;

    protected Double label;

    protected Double output;


    public void setDelta(Double delta) {
        this.delta = delta;
    }

    public void setLabel(Double label) {
        this.label = label;
    }

    public void setOutput(Double output) {
        this.output = output;
    }

    public void setLearningRate(Double learningRate) {
        this.learningRate = learningRate;
    }

    public void setPreviousLayer(Unit[] previousLayer) {
        this.previousLayer = previousLayer;
    }

    public void setNextLayer(Unit[] nextLayer) {
        this.nextLayer = nextLayer;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public void setRandomWeights() {
        weights = new double[previousLayer.length + 1];
        for (int i=0; i<weights.length; i++) {
            weights[i] = Math.random() - 0.5;
        }
    }


    public abstract double computeOutput();

    public abstract void updateWeights();

    protected abstract double computeDelta();


}
