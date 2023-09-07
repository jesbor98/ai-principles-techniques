import sys

INPUT_FILENAME = 'example.csv'
OUTPUT_FILENAME = 'output.txt'

COMMANDS = ['i', 'o', 'I', 'S', 'x', 'h', 'v']

HELP_MESSAGE = 'i, input\t\tcsv file with arrays to sort, uses input.csv\n' + \
               'o, output\t\toutput text file with results\n' + \
               'I, insertion\trun insertion sort\n' + \
               'S, selection\trun selection sort\n' + \
               'x, example\t\trun on example array\n' + \
               'h, help\t\t\tdisplay this help and exit\n' + \
               'v, version\t\toutput version information and exit'


def selection_sort(array):
    """
    Implementation of the selection sort algorithm

    Parameters
    ----------
    array : list
        Unsorted list of numbers

    Returns
    -------
    sorted_list : list
        Sorted list of numbers
    comparisons : int
        Number of comparisons made
    assignments : int
        Number of list changes

    """
    sorted_list = array.copy()
    comparisons = 0
    assignments = 0

    for i in range(len(sorted_list)):
        min_ind = i  # Storing the index of the smallest number

        for j in range(i + 1, len(sorted_list)):  # Looping through the unsorted part of the list
            if sorted_list[j] < sorted_list[min_ind]:
                min_ind = j  # Updating the index of the smallest number if necessary
            comparisons += 1

        if i != min_ind:  # Swapping the smallest number with the first elements of the unsorted part if necessary
            sorted_list[i], sorted_list[min_ind] = sorted_list[min_ind], sorted_list[i]
            assignments += 2

    return sorted_list, comparisons, assignments


def insertion_sort(array):
    """
    Implementation of the insertion sort algorithm

    Parameters
    ----------
    array : list
        Unsorted list of numbers

    Returns
    -------
    sorted_list : list
        Sorted list of numbers
    comparisons : int
        Number of comparisons made
    assignments : int
        Number of list changes

    """
    sorted_list = array.copy()
    comparisons = 0
    assignments = 0

    for i in range(1, len(sorted_list)):
        num = sorted_list[i]  # To be inserted number
        j = i - 1
        # Loops through all prior numbers in the list (in reverse) and puts num in the correct place
        while j >= 0 and sorted_list[j] > num:
            sorted_list[j + 1] = sorted_list[j]
            j -= 1
            comparisons += 1
            assignments += 1
        sorted_list[j + 1] = num
        assignments += 1

    return sorted_list, comparisons, assignments


def sort(algorithm, name, array):
    """
        Sorts a list making use of a given sorting algorithm

        Parameters
        ----------
        algorithm : function
            Implementation of a sorting algorithm
        name : str
            Name of the sorting algorithm
        array : list
            To be sorted list
            
        Returns
        -------
        str
            string containing the results of the sorting

        """
    sorted_list, com, ass = algorithm(array)  # Call to the sorting algorithm
    return f'Sorting using {name}:\n' + \
           f'Before sorting: {array}\nAfter sorting: {sorted_list}\n' + \
           f'Sorting took {com} list comparisons and {ass} list assignments.\n\n'


def ask_input():
    """
    Asks the user for their commands

    Returns
    -------
    inp : list
        list of the user's commands

    """
    while True:  # Asks for input until a valid command is given
        try:
            inp = input('>> ').split(' ')
            for symbol in inp:
                assert symbol in COMMANDS  # Checks whether the given command(s) is/are valid
            break
        except AssertionError:
            print('Invalid command, please try again')
    return inp


def version():
    """
    Prints the version.

    """
    print('Simple sorting simulation version 1.0')
    print('Implements insertion sort and selection sort and')
    print('keeps track of array comparisons and assignments')
    print('The programme and its source code are governed by a BSD-style license')
    print('that can be found in the LICENSE file.')


def example():
    """
    Prints the results of insertion ans selection sort on three different lists

    """
    sorted_list = [_ for _ in range(1, 21)]
    unsorted_list = [15, 3, 12, 7, 10, 14, 16, 13, 18, 9, 2, 17, 1, 8, 4, 11, 5, 6, 20, 19]
    reversed_sorted_list = [_ for _ in range(20, 0, -1)]

    list_of_lists = [sorted_list, unsorted_list, reversed_sorted_list]  # List of the three lists above
    algorithms = [insertion_sort, selection_sort]  # List of the two sorting functions
    names = ['insertion sort', 'selection sort']  # List of the sorting functions' names

    for array in list_of_lists:  # Each list is sorted with both insertion and selection sort
        for algorithm, name in zip(algorithms, names):
            print(sort(algorithm, name, array))  # Results of the sorting are printed


def input_file(insertion, selection, output_file_required):
    """
    Gathers arrays from an input file and performs insertion or selection sort on them
    If no output file is required, the results are printed
    Otherwise an output file with the results will be created

    Parameters
    ----------
    insertion : bool
        Whether insertion sort should be performed
    selection : bool
        Whether selection sort should be performed
    output_file_required : bool
        Whether an output file is required

    """
    try:
        f = open(INPUT_FILENAME, 'r')

        results = ''
        for line in f:  # Each line is read and the results of sorting it are stored
            array = line.split(',')
            for i in range(len(array)):
                array[i] = int(array[i])

            if insertion:
                results += sort(insertion_sort, 'insertion sort', array)
            if selection:
                results += sort(selection_sort, 'selection sort', array)
        f.close()

        if output_file_required:  # The results are stored in a .txt file if required
            g = open(OUTPUT_FILENAME, 'x')
            g.write(results)
            g.close()
        else:  # Otherwise they are printed
            print(results)

    except FileNotFoundError:
        print(f'The file {INPUT_FILENAME} was not found.')
        print('You might want to change the path of the INPUT_FILENAME variable. (line 3)')
        print('You could also just move the \'example.csv\' file to the same folder as this script.')
    except FileExistsError:
        print(f'A file with the name {OUTPUT_FILENAME} already exists.')
        print('You might want to change the path of the OUTPUT_FILENAME variable. (line 4)')
    except Exception as exc:
        print(f'An error occurred when trying to open the file\n{type(exc)}: {exc}')
        print('Execution interrupted')


if __name__ == '__main__':
    print(HELP_MESSAGE)
    inp = ask_input()
    if 'h' in inp:
        print(HELP_MESSAGE)
    elif 'v' in inp:
        version()
    elif 'x' in inp:
        example()
    elif 'i' in inp:
        insertion = 'I' in inp
        selection = 'S' in inp
        output_file_required = 'o' in inp
        input_file(insertion, selection, output_file_required)
    sys.exit(0)
