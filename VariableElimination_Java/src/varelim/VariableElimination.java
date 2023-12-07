package varelim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class VariableElimination {
    private static final List<Factor> factors = new ArrayList<>();
    
    
    public static void variableElimination(UserInterface ui, ArrayList<Variable> variables, String heuristic) {
        int totalCounter = 0;

        // Get the query and observed variables from the user interface
        Variable query = ui.getQueriedVariable();
        ArrayList<ObsVar> observed = ui.getObservedVariables();

        // Initialize factors with the probabilities of each variable
        
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
                    totalCounter++;
                    System.out.println("Counter inside 1st loop: " + totalCounter);
                }
            }
        }

        // Perform variable elimination
        List<Variable> eliminationOrder = getEliminationOrder(variables, query, observed, heuristic, factors);
        for (Variable variable : eliminationOrder) {
            List<Factor> relevantFactors = getRelevantFactors(variable, factors);
            Factor productFactor = multiplyFactors(relevantFactors);
            productFactor.marginalize(variable);

            // Remove the old factors and add the new one
            factors.removeAll(relevantFactors);
            factors.add(productFactor);
            totalCounter++;
            System.out.println("Counter inside 2nd loop: " + totalCounter);
        }

        // Multiply the remaining factors to get the final result
        Factor resultFactor = multiplyFactors(factors);
        // Display the final result
        System.out.println("Probability distribution for " + query.getName() + ":");
        for (Map.Entry<Condition, Double> entry : resultFactor.getValues().entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }

        for(Factor factor: factors) {
            totalCounter += factor.getCounter();
        }
        System.out.println("Total number of operations for factors: " + totalCounter);
    }

   

    private static List<Variable> getEliminationOrder(final ArrayList<Variable> variables, Variable query,
            ArrayList<ObsVar> observed, String heuristic, final List<Factor> factors) {
        // Implement your elimination order logic here
        // This method should return a list of variables in the order they should be
        // eliminated
        // You can use heuristics or other strategies to determine the elimination order
        // For now, a simple order based on the variable index is used as an example
        final List<Variable> eliminationOrder = new ArrayList<>(variables);
        eliminationOrder.remove(query);
        List<Variable> observedVariables = new ArrayList<>();

        for (ObsVar obsVar : observed) {
            observedVariables.add(obsVar.getVar());
        }

        eliminationOrder.removeAll(observedVariables);

        switch (heuristic) {
            case "least-incoming":
                System.out.println("Before sorting (least-incoming): " + eliminationOrder);
                eliminationOrder.sort(new Comparator<Variable>() {
                    @Override
                    public int compare(final Variable v1, final Variable v2) {
                        int parentsV1 = v1.getNrOfParents();
                        int parentsV2 = v2.getNrOfParents();
                        System.out.println(v1.getName() + " parents: " + parentsV1);
                        System.out.println(v2.getName() + " parents: " + parentsV2);
                        return Integer.compare(parentsV1, parentsV2);
                    }
                });
                System.out.println("After sorting (least-incoming): " + eliminationOrder);
                break;

            case "fewest-factors":
                System.out.println("All factors: " + factors);
                System.out.println("Before sorting (fewest-factors): " + eliminationOrder);
                eliminationOrder.sort(new Comparator<Variable>() {
                    @Override
                    public int compare(final Variable v1, final Variable v2) {
                        int factorsV1 = v1.getParents().size()
                                + v1.getNrOfChildren(eliminationOrder);
                        int factorsV2 = v2.getParents().size()
                                + v2.getNrOfChildren(eliminationOrder);
                        System.out.println(v1.getName() + " factors: " + factorsV1);
                        System.out.println(v2.getName() + " factors: " + factorsV2);
                        return Integer.compare(factorsV1, factorsV2);
                    }
                });
                System.out.println("After sorting (fewest-factors): " + eliminationOrder);
                break;
            // Add cases for other heuristics as needed
            default:
                System.out.println("Default sorting (no heurstic): " + eliminationOrder);
                // For empty heuristic or unknown, keep the default order
                break;
        }

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
