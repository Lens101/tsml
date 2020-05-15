package tsml.classifiers.distance_based.utils.contracting;

import tsml.classifiers.TestTimeContractable;
import tsml.classifiers.distance_based.utils.classifier_mixins.TestTimeable;

public interface ContractedTest extends TestTimeable, TestTimeContractable {

    long getTestTimeLimit();

    default boolean hasTestTimeLimit() {
        return getTestTimeLimit() > 0;
    }

    default boolean insideTestTimeLimit(long nanos) {
        return !hasTestTimeLimit() || nanos < getTestTimeLimit();
    }

}
