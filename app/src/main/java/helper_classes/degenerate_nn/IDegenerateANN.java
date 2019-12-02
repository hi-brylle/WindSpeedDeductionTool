package helper_classes.degenerate_nn;

import java.util.HashMap;

public interface IDegenerateANN {
    int predictDOD(HashMap<String, String> componentToDmgDescriptions);
}
