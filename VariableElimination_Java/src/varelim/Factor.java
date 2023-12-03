package varelim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to represent a factor in Bayesian networks.
 * 
 * @author Pepe Tiebosch
 */
public class Factor {

    private List<Variable> variables;
    private Map<Condition, Double> values;

    /**
     * Constructor for the factor class.
     * 
     * @param variables List of variables in the factor.
     */
    public Factor(List<Variable> variables) {
        this.variables = new ArrayList<>(variables);
        this.values = new HashMap<>();
    }

    /**
     * Getter for the values of the factor.
     * 
     * @return Map of conditions and corresponding probabilities.
     */
    public Map<Condition, Double> getValues() {
        return values;
    }

    /**
     * Getter for the variables in the factor.
     * 
     * @return List of variables in the factor.
     */
    public List<Variable> getVariables() {
        return variables;
    }

    /**
     * Set values for the factor.
     * 
     * @param values Map of conditions and corresponding probabilities.
     */
    public void setValues(Map<Condition, Double> values) {
        this.values = values;
    }

    public void multiplyValues(Map<Condition, Double> otherValues) {
        if(values.isEmpty()) {
            values.putAll(otherValues);
        } else {
            for (Map.Entry<Condition, Double> entry : values.entrySet()) {
                Condition condition = entry.getKey();
                double value = entry.getValue();
                Double otherValue = otherValues.get(condition);
                if (otherValue != null) {
                    values.put(condition, value * otherValue);
                }
            }
        }
    }

    /**
     * Perform reduction on the factor by fixing a variable to a specific value.
     * 
     * @param variable Variable to be fixed.
     * @param value    Value to which the variable is fixed.
     */
    public void reduce(Variable variable, String value) {
        List<Variable> updatedVariables = new ArrayList<>(variables);
        updatedVariables.remove(variable);
        List<Condition> conditionsToRemove = new ArrayList<>();

        for (Condition condition : values.keySet()) {
            for (ObsVar observedVar : condition.getObserved()) {
                if (observedVar.getVar().equals(variable) && !observedVar.getValue().equals(value)) {
                    conditionsToRemove.add(condition);
                    break;
                }
            }
        }

        for (Condition conditionToRemove : conditionsToRemove) {
            values.remove(conditionToRemove);
        }

        this.variables = updatedVariables;
    }

    /**
     * Perform product operation with another factor.
     * 
     * @param otherFactor Factor to perform product with.
     * @return Resulting factor after the product operation.
     */
    public Factor product(Factor otherFactor) {
        List<Variable> combinedVariables = new ArrayList<>(variables);
        combinedVariables.addAll(otherFactor.variables);

        Factor resultFactor = new Factor(combinedVariables);

        for (Condition condition1 : values.keySet()) {
            for (Condition condition2 : otherFactor.values.keySet()) {
                if (condition1.contains(condition2)) {
                    double productValue = values.get(condition1) * otherFactor.values.get(condition2);
                    Condition combinedCondition = new Condition(condition1.getObserved());
                    combinedCondition.getObserved().addAll(condition2.getObserved());
                    resultFactor.values.put(combinedCondition, productValue);
                }
            }
        }

        return resultFactor;
    }

    /**
     * Perform marginalization by summing out a variable.
     * 
     * @param variable Variable to be marginalized.
     */
    public void marginalize(Variable variable) {
        // Marginalize the factor by summing over a variable
        List<Condition> conditionsToRemove = new ArrayList<>();
        Map<Condition, Double> newValues = new HashMap<>();
        for (Map.Entry<Condition, Double> entry : values.entrySet()) {
            Condition condition = entry.getKey();
            double value = entry.getValue();
            List<ObsVar> varsToRemove = new ArrayList<>();
            for (ObsVar obsVar : condition.getObserved()) {
                if (obsVar.getVar().equals(variable)) {
                    varsToRemove.add(obsVar);
                }
            }
            Condition newCondition = new Condition(new ArrayList<>(condition.getObserved()));
            newCondition.getObserved().removeAll(varsToRemove);
            if (!newValues.containsKey(newCondition)) {
                newValues.put(newCondition, value);
            } else {
                newValues.put(newCondition, newValues.get(newCondition) + value);
            }
            conditionsToRemove.add(condition);
        }
        for (Condition conditionToRemove : conditionsToRemove) {
            values.remove(conditionToRemove);
        }
        values.putAll(newValues);
    }

    @Override
        public String toString() {
            return "Factor{" +
                    "variables=" + variables +
                    ", values=" + values +
                    '}';
        }


}
