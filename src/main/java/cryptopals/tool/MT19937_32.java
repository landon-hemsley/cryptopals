package cryptopals.tool;

public class MT19937_32 {

    // word length
    private static final int W = 32;

    // degree of recurrence
    private static final int N = 624;

    // middle word, an offset used in the recurrence relation defining series x
    private static final int M = 397;

    // number of bits in LMASK (separation point of one word)
    private static final int R = 31;

    // a constant seemingly chosen at random
    private static final int F = 1812433253;

    //coefficients of the rational normal form twist matrix
    private static final int A = 0x9908B0DF;

    //shifting constants
    private static final int U = 11;
    private static final int S = 7;
    private static final int T = 15;
    private static final int L = 18;

    //masks
    private static final int LMASK = Integer.MAX_VALUE; //0x7fffffff
    private static final int UMASK = Integer.MIN_VALUE; //0x80000000
    private static final int D = 0xFFFFFFFF;
    private static final int B = 0x9D2C5680;
    private static final int C = 0xEFC60000;


    private int index = N;
    private final int[] MT = new int[N];

    public MT19937_32() {
        this(5489);
    }

    /**
     * the constructor seeds the state array
     * @param seed the integer seed value
     */
    public MT19937_32(final int seed) {
        MT[0] = seed;
        for (int i = 1; i < N; i++) {
            MT[i] = F * (MT[i-1] ^ (MT[i-1] >>> (W-2))) + i;
        }
    }

    /**
     * extract_number ... gets the next number
     * @return the next psuedo-random int
     */
    public int nextInt() {
        if (index >= N) {
            twist();
        }

        int y = MT[index++];
        y = y ^ ((y >>> U) & D);
        y = y ^ ((y << S) & B);
        y = y ^ ((y << T) & C);
        y = y ^ (y >>> L);

        return y;
    }

    /**
     * re-populates the state array with a new set of random numbers
     * based on math that I once could understand but probably can't
     * anymore
     */
    private void twist() {
        for (int i = 0; i < N; i++) {
            //index in second half is i + 1 % N to protect against out of bounds
            int x = (MT[i] & UMASK) | ((MT[(i + 1) % N]) & LMASK);

            //the second half is only something if the lsb of x is 1
            // because an xor against 0 is the same as doing nothing
            int xA = (x >>> 1) ^ ((x & 1) * A);

            //index is, again, 1+M mod N to protect against out of bounds
            MT[i] = MT[(i + M) % N] ^ xA;
        }
        index = 0;
    }
}