package models;

public class Train {
  private final String origin;
  private final String destination;
  private final Locomotive engine;
  private Wagon firstWagon;

    /* Representation invariants:
        firstWagon == null || firstWagon.previousWagon == null
        engine != null
     */

  public Train(Locomotive engine, String origin, String destination) {
    this.engine = engine;
    this.destination = destination;
    this.origin = origin;
  }

  /* three helper methods that are usefull in other methods */
  public boolean hasWagons() {
    // TODO
    return (this.firstWagon != null);
  }

  public boolean isPassengerTrain() {
    // TODO

    return this.firstWagon instanceof PassengerWagon;
  }

  public boolean isFreightTrain() {
    // TODO

    return this.firstWagon instanceof FreightWagon;
  }

  public Locomotive getEngine() {
    return engine;
  }

  public Wagon getFirstWagon() {
    return firstWagon;
  }

  /**
   * Replaces the current sequence of wagons (if any) in the train
   * by the given new sequence of wagons (if any)
   * (sustaining all representation invariants)
   *
   * @param wagon the first wagon of a sequence of wagons to be attached
   *              (can be null)
   */
  public void setFirstWagon(Wagon wagon) {
    // TODO
    this.firstWagon = wagon;
  }

  /**
   * @return the number of Wagons connected to the train
   */
  public int getNumberOfWagons() {
    // TODO
    int wagons = 0;
    Wagon wagon = this.getFirstWagon();

    //if train has wagons, count in the first wagon
    if (hasWagons()) {
      wagons++;

      //while the wagon has next wagon, add it
      while (wagon.hasNextWagon()) {
        wagon = wagon.getNextWagon();
        wagons++;
      }

    }
    return wagons;
//        if (this.firstWagon == null) return 0;
//        return this.firstWagon.getSequenceLength();

  }

  /**
   * @return the last wagon attached to the train
   */
  public Wagon getLastWagonAttached() {
    // TODO
    //if firstwagon doesnt exist, return null
    if (this.firstWagon == null) return null;
    //gets last wagon attached
    return this.firstWagon.getLastWagonAttached();
  }

  /**
   * @return the total number of seats on a passenger train
   * (return 0 for a freight train)
   */
  public int getTotalNumberOfSeats() {
    // TODO
    int passenger = 0;
    Wagon currentWagon = getFirstWagon();

    //if train is passengertrain, while the currentwagon has a last wagon, add number of seats of current wagon to passengers
    if (isPassengerTrain()) {
      while (currentWagon.hasNextWagon()) {
        passenger += ((PassengerWagon) currentWagon).getNumberOfSeats();
        //gets next wagon from the currentwagon
        currentWagon = currentWagon.getNextWagon();
      }
      passenger += ((PassengerWagon) currentWagon).getNumberOfSeats();
    }
    return passenger;
  }

  /**
   * calculates the total maximum weight of a freight train
   *
   * @return the total maximum weight of a freight train
   * (return 0 for a passenger train)
   */
  public int getTotalMaxWeight() {
    // TODO
    int weight = 0;
    Wagon wagon = getFirstWagon();
    //if train is a freight train, while wagon has a next wagon, get and add the max weight from wagon
    if (isFreightTrain()) {
      while (wagon.hasNextWagon()) {
        weight += ((FreightWagon) wagon).getMaxWeight();
        //get next wagon
        wagon = wagon.getNextWagon();
      }
      weight += ((FreightWagon) wagon).getMaxWeight();
    }
    return weight;
  }

  /**
   * Finds the wagon at the given position (starting at 1 for the first wagon of the train)
   *
   * @param position
   * @return the wagon found at the given position
   * (return null if the position is not valid for this train)
   */
  public Wagon findWagonAtPosition(int position) {
    // TODO
    Wagon currentWagon = getFirstWagon();

    //check if position is valid or if train has wagons
    if (position <= 0 || currentWagon == null) return null;

    //get position of the next wagon by looping
    for (int i = 1; i < position; i++) {
      if (!currentWagon.hasNextWagon()) return null;
      currentWagon = currentWagon.getNextWagon();
    }

    return currentWagon;
  }

  /**
   * Finds the wagon with a given wagonId
   *
   * @param wagonId
   * @return the wagon found
   * (return null if no wagon was found with the given wagonId)
   */
  public Wagon findWagonById(int wagonId) {
    Wagon currentWagon = getFirstWagon();
    //check if theres a wagon
    if (currentWagon == null) {
      return null;
    }

    //loop until last wagon
    while (currentWagon.getNextWagon() != null) {
      //return the wagon that matches the wagonid
      if (currentWagon.getId() == wagonId) {
        return currentWagon;
      }
      currentWagon = currentWagon.getNextWagon();
    }

    //check last wagon
    if (currentWagon.getId() == wagonId) {
      return currentWagon;
    }
    return null;
  }

  /**
   * Determines if the given sequence of wagons can be attached to this train
   * Verifies if the type of wagons match the type of train (Passenger or Freight)
   * Verifies that the capacity of the engine is sufficient to also pull the additional wagons
   * Verifies that the wagon is not part of the train already
   * Ignores the predecessors before the head wagon, if any
   *
   * @param wagon the head wagon of a sequence of wagons to consider for attachment
   * @return whether type and capacity of this train can accommodate attachment of the sequence
   */
  public boolean canAttach(Wagon wagon) {
    // TODO

    //checks if the wagons are the correct type for the train
    if (wagon instanceof FreightWagon && this.isPassengerTrain()) return false;
    if (wagon instanceof PassengerWagon && this.isFreightTrain()) return false;

    //checks if the wagon is attached to a train
    if (this.findWagonById(wagon.getId()) != null) return false;


    //checks if the engine has the capacity to pull the wagons
    return (engine.getMaxWagons() >= (this.getNumberOfWagons() + (wagon.getSequenceLength())));
  }

  /**
   * Tries to attach the given sequence of wagons to the rear of the train
   * No change is made if the attachment cannot be made.
   * (when the sequence is not compatible or the engine has insufficient capacity)
   * if attachment is possible, the head wagon is first detached from its predecessors, if any
   *
   * @param wagon the head wagon of a sequence of wagons to be attached
   * @return whether the attachment could be completed successfully
   */
  public boolean attachToRear(Wagon wagon) {
    // TODO
    Train currentTrain = this;
    this.firstWagon = currentTrain.getFirstWagon();
    //check if can attach the wagon
    if (!canAttach(wagon)) return false;

    //detach front of wagon
    wagon.detachFront();
    //if theres no first wagon, set wagon as first wagon.
    if (firstWagon == null) {
      setFirstWagon(wagon);
      return true;
    }
    //if firstwagon has wagons, detach the tail and set the nextwagon as wagon and set previous wagon of wagon as firstwagon
    if (firstWagon.hasPreviousWagon()) {
      firstWagon.detachTail();
      firstWagon.setNextWagon(wagon);
      wagon.setPreviousWagon(firstWagon);
      return true;
    }

    //gets last wagon attached
    Wagon lastWagon = firstWagon.getLastWagonAttached();
    //sets wagon as next wagon
    lastWagon.setNextWagon(wagon);
    //sets lastwagon as previous wagon
    wagon.setPreviousWagon(lastWagon);


    return true;
  }

  /**
   * Tries to insert the given sequence of wagons at the front of the train
   * (the front is at position one, before the current first wagon, if any)
   * No change is made if the insertion cannot be made.
   * (when the sequence is not compatible or the engine has insufficient capacity)
   * if insertion is possible, the head wagon is first detached from its predecessors, if any
   *
   * @param wagon the head wagon of a sequence of wagons to be inserted
   * @return whether the insertion could be completed successfully
   */
  public boolean insertAtFront(Wagon wagon) {
    // TODO
    Wagon wagonLast = wagon.getLastWagonAttached();


    //check if can attach
    if (!canAttach(wagon)) return false;

    wagon.detachFront();

    //if theres no first wagon, set wagon as first wagon
    if (this.firstWagon == null) {
      setFirstWagon(wagon);
      return true;
    }


    wagonLast.attachTail(this.firstWagon);
    setFirstWagon(wagon);

    return true;
  }

  /**
   * Tries to insert the given sequence of wagons at/before the given position in the train.
   * (The current wagon at given position including all its successors shall then be reattached
   * after the last wagon of the given sequence.)
   * No change is made if the insertion cannot be made.
   * (when the sequence is not compatible or the engine has insufficient capacity
   * or the given position is not valid for insertion into this train)
   * if insertion is possible, the head wagon of the sequence is first detached from its predecessors, if any
   *
   * @param position the position where the head wagon and its successors shall be inserted
   *                 1 <= position <= numWagons + 1
   *                 (i.e. insertion immediately after the last wagon is also possible)
   * @param wagon    the head wagon of a sequence of wagons to be inserted
   * @return whether the insertion could be completed successfully
   */
  public boolean insertAtPosition(int position, Wagon wagon) {
    Train currentTrain = this;

    //We check if the wagon can attach
    if (!canAttach(wagon)) return false;

    //We check if the position is valid
    if (position < 1 || position > getNumberOfWagons() + 1) {
      return false;
    }

    wagon.detachFront();

    //if the train has no wagons we insert wagon at the front
    if (!currentTrain.hasWagons()) {
      currentTrain.setFirstWagon(wagon);
      return true;
    }

    //if the position is 1 then we insert it at the front using the insertAtFront() method
    if (position == 1) {
      return this.insertAtFront(wagon);
    }

    //Check if the wagon should be attached to the back of the train
    //if true attach it
    if (firstWagon.getSequenceLength() + 1 == position) {
      return this.attachToRear(wagon);
    }


    if (findWagonAtPosition(position) == null) return false;

    //insert the wagon at a given position
    Wagon inPosition = findWagonAtPosition(position);
    inPosition.detachFront();
    this.getLastWagonAttached().attachTail(wagon);
    this.getLastWagonAttached().attachTail(inPosition);


    return true;
  }

  /**
   * Tries to remove one Wagon with the given wagonId from this train
   * and attach it at the rear of the given toTrain
   * No change is made if the removal or attachment cannot be made
   * (when the wagon cannot be found, or the trains are not compatible
   * or the engine of toTrain has insufficient capacity)
   *
   * @param wagonId the id of the wagon to be removed
   * @param toTrain the train to which the wagon shall be attached
   *                toTrain shall be different from this train
   * @return whether the move could be completed successfully
   */
  public boolean moveOneWagon(int wagonId, Train toTrain) {
    //We check if the wagon exists and if the wagon can attach to the new train
    if (findWagonById(wagonId) == null) return false;
    Wagon currentWagon = findWagonById(wagonId);
    if (!toTrain.canAttach(currentWagon)) return false;

    if (currentWagon.hasNextWagon() && currentWagon == firstWagon) {
      this.setFirstWagon(currentWagon.getNextWagon());
    }

    //Here we remove the train from the sequence and attach it to the new train
    currentWagon.removeFromSequence();
    toTrain.attachToRear(currentWagon);

    return true;
  }

  /**
   * Tries to split this train before the wagon at given position and move the complete sequence
   * of wagons from the given position to the rear of toTrain.
   * No change is made if the split or re-attachment cannot be made
   * (when the position is not valid for this train, or the trains are not compatible
   * or the engine of toTrain has insufficient capacity)
   *
   *
   *
   *
   * @param position 1 <= position <= numWagons
   * @param toTrain  the train to which the split sequence shall be attached
   *                 toTrain shall be different from this train
   * @return whether the move could be completed successfully
   */
  public boolean splitAtPosition(int position, Train toTrain) {
    //Check if position is valid
    if (findWagonAtPosition(position) == null || position > this.getNumberOfWagons() + 1) return false;

    Wagon currentWagon = findWagonAtPosition(position);

    //Check if the wagon can attach
    if (!toTrain.canAttach(currentWagon)) return false;

    //Check if it's the first wagon of the train
    if (firstWagon == currentWagon) {
      //remove wagons from train
      this.setFirstWagon(null);
    }

    //Checks if the wagon has previous wagons
    if (currentWagon.hasPreviousWagon()) {
      //detach it
      currentWagon.detachFront();
    } else {
      //remove from trian
      setFirstWagon(null);
    }

    //attach the wagon to the rear of the new train
    toTrain.attachToRear(currentWagon);

    return true;
  }

  /**
   * Reverses the sequence of wagons in this train (if any)
   * i.e. the last wagon becomes the first wagon
   * the previous wagon of the last wagon becomes the second wagon
   * etc.
   * (No change if the train has no wagons or only one wagon)
   */
  public void reverse() {
    // TODO
  }

  // TODO string representation of a train

  @Override
  public String toString() {
    StringBuilder train = new StringBuilder(engine.toString());
    Wagon wagon = firstWagon;

    while (wagon != null) {
      train.append(wagon);
      wagon = wagon.getNextWagon();
    }
    train.append(String.format(" with %d wagons from %s to %s", getNumberOfWagons(), origin, destination));
    return train.toString();
  }
}


