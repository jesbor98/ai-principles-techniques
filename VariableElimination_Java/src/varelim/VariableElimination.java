package varelim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class VariableElimination {
    
    public static void variableElimination(UserInterface ui, ArrayList<Variable> variables) {
        // Get the query and observed variables from the user interface
        Variable query = ui.getQueriedVariable();
        ArrayList<ObsVar> observed = ui.getObservedVariables();

        // Initialize factors with the probabilities of each variable
        List<Factor> factors = new ArrayList<>();
        for (Variable variable : variables) {
            Factor factor = new Factor(Arrays.asList(variable));
            factor.setValues(variable.getProbabilities());
            factors.add(factor);
        }

        // Reduce factors based on observed variables
        for (ObsVar observedVar : observed) {
            for (Factor factor : factors) {
                if (factor.getVariables().contains(observedVar.getVar())) {
                    factor.reduce(observedVar.getVar(), observedVar.getValue());
                }
            }
        }

        // Perform variable elimination
        List<Variable> eliminationOrder = getEliminationOrder(variables, query, observed);
        for (Variable variable : eliminationOrder) {
            List<Factor> relevantFactors = getRelevantFactors(variable, factors);
            Factor productFactor = multiplyFactors(relevantFactors);
            productFactor.marginalize(variable);

            // Remove the old factors and add the new one
            factors.removeAll(relevantFactors);
            factors.add(productFactor);
        }

        // Multiply the remaining factors to get the final result
        Factor resultFactor = multiplyFactors(factors);
        // Display the final result
        System.out.println("Probability distribution for " + query.getName() + ":");
        for (Map.Entry<Condition, Double> entry : resultFactor.getValues().entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }
    }


    private static List<Variable> getEliminationOrder(ArrayList<Variable> variables, Variable query, ArrayList<ObsVar> observed) {
        // Implement your elimination order logic here
        // This method should return a list of variables in the order they should be eliminated
        // You can use heuristics or other strategies to determine the elimination order
        // For now, a simple order based on the variable index is used as an example
        List<Variable> eliminationOrder = new ArrayList<>(variables);
        eliminationOrder.remove(query);
        List<Variable> observedVariables = new ArrayList<>();

        for (ObsVar obsVar : observed) {
            observedVariables.add(obsVar.getVar());
        }

        eliminationOrder.removeAll(observedVariables);

        return eliminationOrder;
    }

    private static List<Factor> getRelevantFactors(Variable variable, List<Factor> factors) {
        // Implement logic to get relevant factors for elimination of a variable
        // This method should return a list of factors relevant to the given variable
        // For now, all factors containing the variable are considered relevant
        List<Factor> relevantFactors = new ArrayList<>();
        for (Factor factor : factors) {
            if (factor.getVariables().contains(variable)) {
                relevantFactors.add(factor);
            }
        }
        return relevantFactors;
    }

    private static Factor multiplyFactors(List<Factor> factors) {
        // Implement logic to multiply a list of factors
        // This method should return a single factor resulting from the multiplication
        // For now, a simple multiplication is performed by combining their values
        List<Variable> allVariables = null;  // Declare the list outside the loop

            for (Factor factor : factors) {
                if(allVariables == null) {
                    allVariables = new ArrayList<>();
                }
                allVariables.addAll(factor.getVariables());
            }
            Factor resultFactor = new Factor(allVariables);

            // Multiply values for all factors
            for (Factor factor : factors) {
                resultFactor.product(factor.getValues());
            }
            return resultFactor;
        
    }
}
