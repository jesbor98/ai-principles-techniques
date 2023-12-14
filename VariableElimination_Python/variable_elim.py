import pandas as pd
import os

class VariableElimination:

    def __init__(self, network):
        """
        Initialize the variable elimination algorithm with the specified network.
        Add more initializations if necessary.
        """
        self.network = network
        self.log_file = "variable_elimination_log.txt"  # Log file path
        
        # Remove existing log file if exists
        if os.path.exists(self.log_file):
            os.remove(self.log_file)
        
    def log_to_file(self, message):
        """
        Helper method to log messages to the file.
        """
        with open(self.log_file, "a") as log:
            log.write(message + "\n")

    def run(self, query, observed, elim_order):
        """
        Use the variable elimination algorithm to find out the probability
        distribution of the query variable given the observed variables

        Input:
            query:      The query variable
            observed:   A dictionary of the observed variables {variable: value}
            elim_order: Either a list specifying the elimination ordering
                        or a function that will determine an elimination ordering
                        given the network during the run

        Output: A variable holding the probability distribution
                for the query variable
        """
        # Check if elim_order is a function (heuristic)
        elim_order = elim_order(self.network) if callable(elim_order) else elim_order
        
        # Logging elimination ordering
        self.log_to_file(f"\nElimination Order: {elim_order}")
        
        # Copy all probability distributions from the network
        factors = [prob.copy() for prob in self.network.probabilities.values()]

        # Eliminate variables in the specified order
        for elim_node in elim_order:
            # Logging current elimination node
            self.log_to_file(f"\nProcessing Elimination Node: {elim_node}")
            
            # Check which factors contain the current elimination node
            factors_contain_node, indices = self.contains_node(factors, elim_node)
            
            # Logging factors containing elimination node
            self.log_to_file(f"Factors Containing {elim_node}: {indices}")
            
            # Multiply the factors that have the elimination node
            computed_factor = self.product(factors_contain_node, elim_node) if len(factors_contain_node) >= 2 else factors_contain_node[0]

            # If the elimination node is observed, reduce the factor accordingly
            if factors_contain_node:
                if elim_node in observed:
                    computed_factor = self.reduction(observed[elim_node], elim_node, computed_factor)
                    # Logging reduction
                    self.log_to_file(f"Reduction for Observed Node {elim_node}: {observed[elim_node]}")
                
                # If the elimination node is not the query node, marginalize it
                elif elim_node != query:
                    computed_factor = self.marginalize(elim_node, computed_factor) if len(factors_contain_node) >= 2 else self.marginalize(elim_node, factors_contain_node[0])
                    # Logging marginalization
                    self.log_to_file(f"Marginalization for Node {elim_node}")

                # Remove the factors used in the computation
                indices.reverse()
                factors = [factors[i] for i in range(len(factors)) if i not in indices]
                # Append the computed factor
                factors.append(computed_factor)
                

        # Calculate the final probability distribution
        states = factors[0][query].unique()
        results = [1] * len(states)

        for factor in factors:
            for i, state in enumerate(states):
                results[i] *= factor[factor[query] == state]["prob"].values[0]

        total = sum(results)
        results = [result / total for result in results]

        # Create a DataFrame for the final result and print it
        dict_data = {query: states, "prob": results}
        result = pd.DataFrame(data=dict_data)
        print(f"Final result: \n{result}")
        
        # Logging final result
        self.log_to_file(f"\nFinal Result: \n{result}")
        
        return result
    
    @staticmethod
    def least_incoming_arcs_first(network):
        """
        Heuristic: Choose variables for elimination based on the least incoming arcs.
        """
        elimination_order = sorted(network.nodes, key=lambda node: len(network.parents.get(node, [])))
        print(f"Elimination Order (Least Incoming Arcs First): {elimination_order}")
        return elimination_order

    @staticmethod
    def contained_in_fewest_factors_first(network):
        """
        Heuristic: Choose variables for elimination based on being contained in the fewest factors.
        """
        factors_count = {node: sum(node in factor.columns for factor in network.probabilities.values()) for node in network.nodes}
        elimination_order = sorted(network.nodes, key=lambda node: factors_count.get(node, 0))
        print(f"Elimination Order (Contained in Fewest Factors First): {elimination_order}")
        return elimination_order

    def contains_node(self, factors, elim_node):
        """
        Helper function to identify factors that contain the specified node.

        Args:
            factors (list): List of factors (probability distributions).
            elim_node (str): The node to check for in the factors.

        Returns:
            Tuple: A list of factors that have the specified node, and a list of their indices.
        """
        factors_contain_node = [factor for factor in factors if elim_node in factor.columns]
        indices = [i for i, factor in enumerate(factors) if elim_node in factor.columns]
        return factors_contain_node, indices

    def reduction(self, fixed_state, node, factor):
        """
        Reduce the factor by fixing the specified node to a given state.

        Args:
            fixed_state: The state to fix the specified node to.
            node: The node to be fixed.
            factor: The factor to be reduced.

        Returns:
            DataFrame: Resultant factor after reduction.
        """
        return factor[factor[node] == fixed_state]
    
    def product(self, factors, elim_node=None):
        """
        Multiply the specified factors on the common elimination node.

        Args:
            factors (list): List of factors to be multiplied.
            elim_node (str): The common elimination node.

        Returns:
            DataFrame: Resultant factor after multiplication.
        """
        output = factors[0]

        for factor in factors[1:]:
            output = output.merge(factor, on=elim_node, how="left")
            prob_cols = ["prob_x", "prob_y"]
            output["prob"] = output[prob_cols[0]] * output[prob_cols[1]]
            output = output.drop(prob_cols, axis=1)

        return output

    def marginalize(self, node, factor):
        """
        Marginalize the specified node from the factor.

        Args:
            node: The node to be marginalized.
            factor: The factor to be marginalized.

        Returns:
            DataFrame: Resultant factor after marginalization.
        """
        columns = [column for column in factor.columns if column != node]
        marginalize_factor = pd.DataFrame(columns=columns)
        marginalize_factors = []

        def generate_marginal_combinations(factor, marginalize_factor, index, combination):
            """
            Recursively generate combinations of variable states for marginalization.

            Args:
                factor (DataFrame): The current factor being processed.
                marg_factor (DataFrame): The accumulated marginalization factor.
                index (int): Current index indicating the variable being considered.
                combination (list): The current combination of variable states.

            Returns:
                None: The function modifies the marg_factor in-place.
            """
            # Get the list of columns in the marg_factor
            columns = list(marginalize_factor.columns)
            columns.pop()

            # Check if we have processed all variables
            if index == len(columns):
                # If all variables are processed, calculate the marginal probability for the combination
                for i, col in enumerate(columns):
                    factor = factor[(factor[col] == combination[i])]
                marginalize_row = combination.copy()
                marginalize_row.append(factor["prob"].sum())
                columns.append("prob")
                # Create a dictionary representing a row in the marg_factor DataFrame
                marginalize_row = {columns[i]: [marg_el] for i, marg_el in enumerate(marginalize_row)}
                columns.pop()
                # Convert the dictionary to a DataFrame and concatenate it to marg_factor
                marginalize_row = pd.DataFrame(marginalize_row)
                marginalize_factor = pd.concat([marginalize_factor, marginalize_row], ignore_index=True, sort=False, axis=1).fillna(0)
                marginalize_factors.append(marginalize_row)
            else:
                # If there are still variables to process, recurse for each unique value of the variable
                for value in factor[columns[index]].unique():
                    combination.append(value)
                    generate_marginal_combinations(factor.copy(), marginalize_factor, index + 1, combination.copy())
                    combination.pop()

        generate_marginal_combinations(factor.copy(), marginalize_factor, 0, [])
        return pd.concat(marginalize_factors)