package spotifycharts;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class SorterImpl<E> implements Sorter<E> {

    /**
     * Sorts all items by selection or insertion sort using the provided comparator
     * for deciding relative ordening of two items
     * Items are sorted 'in place' without use of an auxiliary list or array
     *
     * @param items
     * @param comparator
     * @return the items sorted in place
     */
    public List<E> selInsBubSort(List<E> items, Comparator<E> comparator) {
        // TODO implement selection sort or insertion sort or bubble sort

        for (int i = 0; i < items.size(); i++) {
            E item1 = items.get(i);
            int j = i;
            while (j > 0) {
                E item2 = items.get(j - 1);
                if (comparator.compare(item1, item2) >= 0) {
                    break;
                }
                items.set(j, item2);
                j--;
            }
            items.set(j, item1);
        }


        return items;   // replace as you find appropriate
    }

    /**
     * Sorts all items by quick sort using the provided comparator
     * for deciding relative ordening of two items
     * Items are sorted 'in place' without use of an auxiliary list or array
     *
     * @param items
     * @param comparator
     * @return the items sorted in place
     */
    public List<E> quickSort(List<E> items, Comparator<E> comparator) {
        // TODO provide a recursive quickSort implementation,
        //  that is different from the example given in the lecture
        quickSort(items, 0,items.size()-1, comparator);

        return items;   // replace as you find appropriate
    }

    private void swap(List<E> items, int i, int j)
    {
        //swaps (exchanges) between two objects
        E temp = items.get(i);
        items.set(i,items.get(j));
        items.set(j,temp);
    }


    private int seperator(List<E> items, int low, int high, Comparator<E> comparator)
    {
        //segregates the list of objects to compare between them
        E pin = items.get(high);

        // indicates the right position
        // of pin found so far
        int i = (low - 1);


        for (int j = low; j <= high - 1; j++) {

            //if the element is smaller than the pin
            if (comparator.compare(items.get(j), pin) < 0) {
                //increment the smaller element
                i++;
                //swap the objects
                swap(items, i, j);
            }
        }
        swap(items, i + 1, high);
        return (i + 1);
    }


    private void quickSort(List<E> items, int low, int high, Comparator<E> comparator)
    {
        //sorts based on the start index and end index of the list of objects
        if (low < high) {

            //now items is at the right place
            int sp = seperator(items, low, high, comparator);

            //recursively and separately sorts the elements before and after separator
            quickSort(items, low, sp - 1, comparator);
            quickSort(items, sp + 1, high, comparator);
        }
    }


    /**
     * Identifies the lead collection of numTops items according to the ordening criteria of comparator
     * and organizes and sorts this lead collection into the first numTops positions of the list
     * with use of (zero-based) heapSwim and heapSink operations.
     * The remaining items are kept in the tail of the list, in arbitrary order.
     * Items are sorted 'in place' without use of an auxiliary list or array or other positions in items
     *
     * @param numTops    the size of the lead collection of items to be found and sorted
     * @param items
     * @param comparator
     * @return the items list with its first numTops items sorted according to comparator
     * all other items >= any item in the lead collection
     */
    public List<E> topsHeapSort(int numTops, List<E> items, Comparator<E> comparator) {

        // the lead collection of numTops items will be organised into a (zero-based) heap structure
        // in the first numTops list positions using the reverseComparator for the heap condition.
        // that way the root of the heap will contain the worst item of the lead collection
        // which can be compared easily against other candidates from the remainder of the list
        Comparator<E> reverseComparator = comparator.reversed();

        // initialise the lead collection with the first numTops items in the list
        for (int heapSize = 2; heapSize <= numTops; heapSize++) {
            // repair the heap condition of items[0..heapSize-2] to include new item items[heapSize-1]
            heapSwim(items, heapSize, reverseComparator);
        }

        // insert remaining items into the lead collection as appropriate
        for (int i = numTops; i < items.size(); i++) {
            // loop-invariant: items[0..numTops-1] represents the current lead collection in a heap data structure
            //  the root of the heap is the currently trailing item in the lead collection,
            //  which will lose its membership if a better item is found from position i onwards
            E item = items.get(i);
            E worstLeadItem = items.get(0);
            if (comparator.compare(item, worstLeadItem) < 0) {
                // item < worstLeadItem, so shall be included in the lead collection
                items.set(0, item);
                // demote worstLeadItem back to the tail collection, at the orginal position of item
                items.set(i, worstLeadItem);
                // repair the heap condition of the lead collection
                heapSink(items, numTops, reverseComparator);
            }
        }

        // the first numTops positions of the list now contain the lead collection
        // the reverseComparator heap condition applies to this lead collection
        // now use heapSort to realise full ordening of this collection
        for (int i = numTops - 1; i > 0; i--) {
            // loop-invariant: items[i+1..numTops-1] contains the tail part of the sorted lead collection
            // position 0 holds the root item of a heap of size i+1 organised by reverseComparator
            // this root item is the worst item of the remaining front part of the lead collection

            // TODO swap item[0] and item[i];
            //  this moves item[0] to its designated position

            E item = items.get(0);
            items.set(0, items.get(i));
            items.set(i, item);

            // TODO the new root may have violated the heap condition
            //  repair the heap condition on the remaining heap of size i

            heapSink(items, i, reverseComparator);

        }

        return items;
    }

    /**
     * Repairs the zero-based heap condition for items[heapSize-1] on the basis of the comparator
     * all items[0..heapSize-2] are assumed to satisfy the heap condition
     * The zero-bases heap condition says:
     * all items[i] <= items[2*i+1] and items[i] <= items[2*i+2], if any
     * or equivalently:     all items[i] >= items[(i-1)/2]
     *
     * @param items
     * @param heapSize
     * @param comparator
     */
    protected void heapSwim(List<E> items, int heapSize, Comparator<E> comparator) {
        // TODO swim items[heapSize-1] up the heap until
        //      i==0 || items[(i-1]/2] <= items[i]

        int child = heapSize - 1;
        E swimmer = items.get(child);
        int parent = (child - 1) / 2;

        while (comparator.compare(swimmer, items.get(parent)) < 0) {
            items.set(child, items.get(parent));
            child = parent;
            parent = child / 2;

            if (parent == 0) break;
        }
        items.set(child, swimmer);
    }

    /**
     * Repairs the zero-based heap condition for its root items[0] on the basis of the comparator
     * all items[1..heapSize-1] are assumed to satisfy the heap condition
     * The zero-bases heap condition says:
     * all items[i] <= items[2*i+1] and items[i] <= items[2*i+2], if any
     * or equivalently:     all items[i] >= items[(i-1)/2]
     *
     * @param items
     * @param heapSize
     * @param comparator
     */
    protected void heapSink(List<E> items, int heapSize, Comparator<E> comparator) {
        // TODO sink items[0] down the heap until
        //      2*i+1>=heapSize || (items[i] <= items[2*i+1] && items[i] <= items[2*i+2])

        int parentIndex = 0;
        int childIndex = 1;
        E sinker = items.get(parentIndex);

        while (childIndex < heapSize) {
            E child = items.get(childIndex);

            if (childIndex + 1 < heapSize && comparator.compare(items.get(childIndex + 1), child) < 0) {
                //right
                childIndex++;
                child = items.get(childIndex);
            }

            //end of tree
            if (comparator.compare(sinker, child) <= 0) break;

            //swap parent and child
            items.set(parentIndex, child);
            //go down
            parentIndex = childIndex;
            childIndex = 2 * parentIndex + 1;
        }
        items.set(parentIndex, sinker);
    }
}
