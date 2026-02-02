package models;

public abstract class Wagon {
    protected int id;               // some unique ID of a Wagon
    private Wagon nextWagon;        // another wagon that is appended at the tail of this wagon
    // a.k.a. the successor of this wagon in a sequence
    // set to null if no successor is connected
    private Wagon previousWagon;    // another wagon that is prepended at the front of this wagon
    // a.k.a. the predecessor of this wagon in a sequence
    // set to null if no predecessor is connected


    // representation invariant propositions:
    // tail-connection-invariant:   wagon.nextWagon == null or wagon == wagon.nextWagon.previousWagon
    // front-connection-invariant:  wagon.previousWagon == null or wagon = wagon.previousWagon.nextWagon

    public Wagon(int wagonId) {
        this.id = wagonId;
    }

    public int getId() {
        return id;
    }

    public Wagon getNextWagon() {
        return nextWagon;
    }

    public Wagon getPreviousWagon() {
        return previousWagon;
    }

    public void setNextWagon(Wagon nextWagon) {
        this.nextWagon = nextWagon;
    }

    public void setPreviousWagon(Wagon previousWagon) {
        this.previousWagon = previousWagon;
    }

    /**
     * @return whether this wagon has a wagon appended at the tail
     */
    public boolean hasNextWagon() {
        // TODO

        return nextWagon != null;
    }

    /**
     * @return whether this wagon has a wagon prepended at the front
     */
    public boolean hasPreviousWagon() {
        // TODO

        return previousWagon != null;
    }

    /**
     * Returns the last wagon attached to it,
     * if there are no wagons attached to it then this wagon is the last wagon.
     *
     * @return the last wagon
     */
    public Wagon getLastWagonAttached() {
        // TODO find the last wagon in the sequence
        Wagon currentWagon = this;
        while (currentWagon.hasNextWagon()) {
            currentWagon = currentWagon.getNextWagon();
            if (!currentWagon.hasNextWagon()) {
                return currentWagon;
            }
        }
        return currentWagon;
    }

    /**
     * @return the length of the sequence of wagons towards the end of its tail
     * including this wagon itself.
     */
    public int getSequenceLength() {
        // TODO traverse the sequence and find its length
        int i = 1;
        Wagon currentWagon = this;
        while (currentWagon.hasNextWagon()) {
            i++;
            currentWagon = currentWagon.getNextWagon();
        }

        return i;
    }

    /**
     * Attaches the tail wagon and its connected successors behind this wagon,
     * if and only if this wagon has no wagon attached at its tail
     * and if the tail wagon has no wagon attached in front of it.
     *
     * @param tail the wagon to attach behind this wagon.
     * @throws IllegalStateException if this wagon already has a wagon appended to it.
     * @throws IllegalStateException if tail is already attached to a wagon in front of it.
     *                               The exception should include a message that reports the conflicting connection,
     *                               e.g.: "%s is already pulling %s"
     *                               or:   "%s has already been attached to %s"
     */
    public void attachTail(Wagon tail) {
        // TODO verify the exceptions
        Wagon currentWagon = this;
        //if theres a wagon in front of current wagon, throw exception
        if (currentWagon.hasNextWagon()) {
            throw new IllegalStateException(String.format("%s is already pulling %s", currentWagon, currentWagon.getNextWagon()));
        }
        //if theres a wagon previous to this one, throw exception
        if (tail.hasPreviousWagon()) {
            throw new IllegalStateException(String.format("%s has already been attached to %s", tail, tail.getPreviousWagon()));
        }

        // TODO attach the tail wagon to this wagon (sustaining the invariant propositions).

        //attaches tail to wagon and wagon to tail
        currentWagon.setNextWagon(tail);
        tail.setPreviousWagon(currentWagon);
    }

    /**
     * Detaches the tail from this wagon and returns the first wagon of this tail.
     *
     * @return the first wagon of the tail that has been detached
     * or <code>null</code> if it had no wagons attached to its tail.
     */
    public Wagon detachTail() {
        // TODO detach the tail from this wagon (sustaining the invariant propositions).
        //  and return the head wagon of that tail
        Wagon tail = this.getNextWagon();
        if (tail != null) {
            //detaches the wagon and its front wagon
            this.getNextWagon().setPreviousWagon(null);
            //set next wagon as null
            this.setNextWagon(null);
        }

        return tail;
    }

    /**
     * Detaches this wagon from the wagon in front of it.
     * No action if this wagon has no previous wagon attached.
     *
     * @return the former previousWagon that has been detached from,
     * or <code>null</code> if it had no previousWagon.
     */
    public Wagon detachFront() {
        // TODO detach this wagon from its predecessor (sustaining the invariant propositions).
        //   and return that predecessor
        Wagon frontWagon = this.getPreviousWagon();
        if (frontWagon != null) {
            //detach the wagon and its front wagon
            this.getPreviousWagon().setNextWagon(null);
            this.setPreviousWagon(null);
        }
        return frontWagon;

    }

    /**
     * Replaces the tail of the <code>front</code> wagon by this wagon and its connected successors
     * Before such reconfiguration can be made,
     * the method first disconnects this wagon form its predecessor,
     * and the <code>front</code> wagon from its current tail.
     *
     * @param front the wagon to which this wagon must be attached to.
     */
    public void reAttachTo(Wagon front) {
        // TODO detach any existing connections that will be rearranged
        if (front != null) {
            //detach front of the wagon and tail
            this.detachFront();
            front.detachTail();
            //attach this wagon to front wagon
            front.attachTail(this);
        }
    }

    /**
     * Removes this wagon from the sequence that it is part of,
     * and reconnects its tail to the wagon in front of it, if any.
     */
    public void removeFromSequence() {
        // TODO
        if (hasPreviousWagon() && hasNextWagon()) {
            Wagon previousWagon = getPreviousWagon();
            Wagon nextWagon = getNextWagon();
            //detach the current wagon from the sequence
            this.getNextWagon().detachFront();
            this.getPreviousWagon().detachTail();
            //reattach wagon to previous wagon
            nextWagon.reAttachTo(previousWagon);

        } else {
            //remove wagon from sequence if theres no wagon in front of or behind this wagon
            this.detachFront();
            this.detachTail();
        }

    }


    /**
     * Reverses the order in the sequence of wagons from this Wagon until its final successor.
     * The reversed sequence is attached again to the wagon in front of this Wagon, if any.
     * No action if this Wagon has no succeeding next wagon attached.
     *
     * @return the new start Wagon of the reversed sequence (with is the former last Wagon of the original sequence)
     */
    public Wagon reverseSequence() {
        // TODO provide an iterative implementation,
        //   using attach- and detach methods of this class


        //doesnt work
        Wagon currentWagon = this;
        int wagonlenght = currentWagon.getSequenceLength();
        Wagon lastWagon;
        Wagon newFirstWagon = null;
        Wagon lastAttached;
        Wagon oldPrev = currentWagon.getPreviousWagon();


        for (int i = 0; i < wagonlenght; i++) {
            if (newFirstWagon == null) {
                lastWagon = currentWagon.getLastWagonAttached();
                lastWagon.detachFront();
                lastWagon.detachTail();
                newFirstWagon = lastWagon;
                if (currentWagon.hasPreviousWagon()) {
                    newFirstWagon.setPreviousWagon(oldPrev);
                    newFirstWagon.getPreviousWagon().setNextWagon(newFirstWagon);
                }
            } else {

                lastWagon = currentWagon.getLastWagonAttached();
                lastWagon.detachFront();
                lastWagon.detachTail();

                lastAttached = newFirstWagon.getLastWagonAttached();
                lastAttached.setNextWagon(lastWagon);
                lastAttached.getNextWagon().setPreviousWagon(lastAttached);

            }

        }

        return newFirstWagon;
    }

    // TODO string representation of a Wagon

    @Override
    public String toString() {
        return "[Wagon-" + getId() + "]";
    }
}
