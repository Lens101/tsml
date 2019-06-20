package classifiers.distance_based.knn.sampling;

import classifiers.distance_based.elastic_ensemble.iteration.DynamicIterator;
import util.Sampler;
import utilities.CollectionUtilities;
import utilities.Utilities;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;

public class DistributedRandomSampler
    extends DynamicIterator<Instance, DistributedRandomSampler> {
    private final Random random;
    private final Map<Double, List<Instance>> instancesByClass;
    private final List<ClassLikelihood> probabilities = new ArrayList<>();
    private List<Instance> instances;
    private int index = 0;

    @Override
    public DistributedRandomSampler iterator() {
        return new DistributedRandomSampler(this);
    }

    private static class ClassLikelihood
        implements Comparable<ClassLikelihood> {
        private final double classValue;

        public double getClassValue() {
            return classValue;
        }

        private double probability;
        private final double classDistributionProbability;

        private ClassLikelihood(final double classValue,
                                final double classDistributionProbability) {
            this.classValue = classValue;
            this.probability = classDistributionProbability;
            this.classDistributionProbability = classDistributionProbability;
        }

        public void chosen() {
            probability--;
        }

        public void increment() {
            probability += classDistributionProbability;
        }

        @Override
        public int compareTo(final ClassLikelihood classLikelihood) {
            return Double.compare(probability, classLikelihood.probability);
        }
    }

    public DistributedRandomSampler(Instances instances, Random random) { // todo update to seeding system
        this.random = random;
        instancesByClass = Utilities.instancesByClassValue(instances);
        for(Map.Entry<Double, List<Instance>> entry : instancesByClass.entrySet()) {
            double probability = (double) entry.getValue().size() / instances.size();
            probabilities.add(new ClassLikelihood(entry.getKey(), probability));
        }
    }

    public DistributedRandomSampler(Random random) {
        this.random = random;
        instancesByClass = null;
    }

    public DistributedRandomSampler(DistributedRandomSampler other) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasNext() {
        return !instancesByClass.isEmpty();
    }

    @Override
    public Instance next() {
        ClassLikelihood classLikelihood = CollectionUtilities.findBest(probabilities, random);
        classLikelihood.chosen();
        for(ClassLikelihood a : probabilities) {
            a.increment();
        }
        instances = instancesByClass.get(classLikelihood.getClassValue());
        index = random.nextInt(instances.size());
        return instances.get(index);
    }

    @Override
    public void remove() {
        instances.remove(index);
    }

    @Override
    public void add(final Instance instance) {
        throw new UnsupportedOperationException();
    }

}
