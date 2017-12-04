package com.jozufozu.motio.api.cap;

public interface IMotio
{
    /**
     * @return How much motio this bank can hold
     */
    long capacity();
    
    /**
     * @return How much motio is available in this bank
     */
    long available();
    
    default long unusedCapacity()
    {
        return capacity() - available();
    }
    
    /**
     * Adds {@param amount} to this bank
     * @param amount How much should be added to this bank
     * @param simulate If true, does not actually add anything to the bank
     * @return The amount of motio actually added
     */
    long fill(long amount, boolean simulate);
    
    /**
     * Removes {@param amount} from this bank
     * Should not allow for more than is available to be removed
     * @param amount How much should be drained from this bank
     * @param simulate If true, does not actually add anything to the bank
     * @return The amount of motio removed, 0 if it could not be
     */
    long tap(long amount, boolean simulate);
}
