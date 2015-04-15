package util;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <T> the type of objectList being permuted.
 */
public class PermutationEnumerator<T>
{
    private PermutationEnumerator<T> subPermutationEnumerator;
    private List<T> permutation;
    private List<T> subpermutation;
    private int nextIndex = 0;
    private T interleaveObject;

    final static Integer ONE = 1;
    final static Integer TWO = 2;

    /**
     *
     * @param objectList the objectList being permuted is unmodified.
     * @throws java.lang.IllegalArgumentException when passed a null object list.
     */
    public PermutationEnumerator( final List<T> objectList ) throws IllegalArgumentException
    {
        if ( objectList == null )
        {
            throw new IllegalArgumentException();
        }
        permutation = new ArrayList<T>( objectList );
        if ( permutation.isEmpty() )
        {
            return;
        }
        subpermutation = new ArrayList<T>( permutation );
        interleaveObject = subpermutation.remove( 0 );
        subPermutationEnumerator = new PermutationEnumerator<T>( subpermutation );
        subpermutation = subPermutationEnumerator.next();
    }

    /**
     * Produce the permutation permutation.
     * @return the permutation permutation as a List.
     * If none, returns null.
     * @throws java.lang.IllegalArgumentException  permutation() invoked when hasNext() is false.
     */
    public List<T> next() throws IllegalArgumentException
    {
        if ( permutation == null )
        {
            return null;
        }
        List<T> returnValue = new ArrayList<T>( permutation );
        if ( permutation.isEmpty() )
        {
            permutation = null;
        }
        else if ( nextIndex < permutation.size() - 1)
        {
            T temp = permutation.get( nextIndex + 1 );
            permutation.set( nextIndex + 1, permutation.get( nextIndex ) );
            permutation.set( nextIndex++, temp );
        }
        else
        {
            subpermutation = subPermutationEnumerator.next();
            if ( subpermutation == null || subpermutation.isEmpty() )
            {
                permutation = null;
            }
            else
            {
                permutation = new ArrayList<T>( subpermutation );
                permutation.add( 0, interleaveObject );
                nextIndex = 0;
            }
        }
        return returnValue;
    }
}