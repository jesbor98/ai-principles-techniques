"""
@Author: Joris van Vugt, Moira Berens, Leonieke van den Bulk

Entry point for the creation of the variable elimination algorithm in Python 3.
Code to read in Bayesian Networks has been provided. We assume you have installed the pandas package.

"""
from read_bayesnet import BayesNet
from variable_elim import VariableElimination
import time

if __name__ == '__main__':
    # The class BayesNet represents a Bayesian network from a .bif file in several variables
    net = BayesNet('earthquake.bif') # Format and other networks can be found on http://www.bnlearn.com/bnrepository/
    
    # These are the variables read from the network that should be used for variable elimination
    print("Nodes:")
    print(net.nodes)
    print("Values:")
    print(net.values)
    print("Parents:")
    print(net.parents)
    print("Probabilities:")
    print(net.probabilities)

    # Make your variable elimination code in the seperate file: 'variable_elim'. 
    # You use this file as follows:
    ve = VariableElimination(net)

    # Set the node to be queried as follows:
    query = 'Alarm'

    # The evidence is represented in the following way (can also be empty when there is no evidence): 
    evidence = {'Burglary': 'True'}

    # Determine your elimination ordering before you call the run function. The elimination ordering   
    # is either specified by a list or a heuristic function that determines the elimination ordering
    # given the network. Experimentation with different heuristics will earn bonus points. The elimination
    # ordering can for example be set as follows:
    elim_order = net.nodes
    
    # Determine your elimination ordering using heuristics
    elim_order_least_incoming_arcs_first = VariableElimination.least_incoming_arcs_first
    elim_order_contained_in_fewest_factors_first = VariableElimination.contained_in_fewest_factors_first

    # Call the variable elimination function for the queried node given the evidence and the elimination ordering as follows:
    #ve.run(query, evidence, elim_order)
    #ve.run(query, evidence, elim_order_least_incoming_arcs_first)
    #ve.run(query, evidence, elim_order_contained_in_fewest_factors_first)
    
    # Measure time for the first run without heuristics
    start_time = time.time()
    ve.run(query, evidence, elim_order)
    end_time = time.time()
    print(f"Time taken without heuristics: {round(end_time - start_time, 4)} seconds")

    # Measure time for the second run with the first heuristic
    start_time = time.time()
    ve.run(query, evidence, elim_order_least_incoming_arcs_first)
    end_time = time.time()
    print(f"Time taken with least_incoming_arcs_first heuristic: {round(end_time - start_time, 4)} seconds")

    # Measure time for the third run with the second heuristic
    start_time = time.time()
    ve.run(query, evidence, elim_order_contained_in_fewest_factors_first)
    end_time = time.time()
    print(f"Time taken with contained_in_fewest_factors_first heuristic: {round(end_time - start_time, 4)} seconds")
