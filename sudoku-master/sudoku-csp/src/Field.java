/*
 * Artificial Intelligence: Principles & Techniques
 * Assignment 2: Sudoku
 * 16/11-23
 * Amanda Enhörning, s1128126
 * Jessica Borg, s1129470
 */

import java.util.ArrayList;
import java.util.List;

public class Field {
  private int value = 0;
  private List<Integer> domain;
  private List<Field> neighbours;

  /*
   * ==============
   *  CONSTRUCTORS
   * ==============
   */

  // Constructor in case the field is unknown
  Field() {
    this.domain = new ArrayList<>(9);
    for (int i = 1; i < 10; i++)
        this.domain.add(i);

    this.neighbours = new ArrayList<>();
  }

  // Constructor in case the field is known, i.e., it contains a value
  Field(int initValue) {
    this.value = initValue;
    this.domain = new ArrayList<>();
    this.neighbours = new ArrayList<>();
  }

  /*
   * =================
   *  VALUE FUNCTIONS
   * =================
   */
  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  /*
   * =====================
   *  NEIGHBOUR FUNCTIONS
   * =====================
   */
  public void setNeighbours(List<Field> neighbours) {
    this.neighbours = neighbours;
  }

  public List<Field> getNeighbours() {
    return neighbours;
  }

  public List<Field> getOtherNeighbours(Field b) {
    List<Field> newNeighbours = new ArrayList<>(neighbours);
    newNeighbours.remove(b);
    return newNeighbours;
  }

  /*
   * ==================
   *  DOMAIN FUNCTIONS
   * ==================
   */

  public List<Integer> getDomain() {
    return domain;
  }

  public int getDomainSize() {
    return domain.size();
  }

   /**
   * Removes the given value from the domain, and possibly assigns the last value to the field.
   * 
   * @param value
   * @return true if the value was removed
   */
  public boolean removeFromDomain(int value) {
    boolean b = this.domain.remove(Integer.valueOf(value));

    // If there is only one value left in the domain, sets the value of the field to the last domain value.
    if (domain.size() == 1) {
      setValue(domain.get(0));
    }
    
    return b;
  }

  public void resetField() {
    value = 0;
    domain.clear();
    for (int i = 1; i <= 9; i++) {
        domain.add(i);
    }
}


  /*
   * ================
   *  MISC FUNCTIONS
   * ================
   */

  @Override
  public String toString() {
    return (value==0)? "." : String.valueOf(value);
  }
}
