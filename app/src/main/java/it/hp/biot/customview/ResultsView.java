package it.hp.biot.customview;



import java.util.List;
import it.hp.biot.tflite.Classifier.Recognition;

public interface ResultsView {
    public void setResults(final List<Recognition> results);
}
