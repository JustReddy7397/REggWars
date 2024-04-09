package ga.justreddy.wiki.reggwars.utils.md5;

/**
 * @author Santeri Paavolainen <sjpaavol@cc.helsinki.fi>
 * @author Timothy W Macinta (twm@alum.mit.edu) (optimizations and bug fixes)
 */
public class MD5State {

    /**
     * 128-bit state
     */
    int	state[];

    /**
     * 64-bit character count
     */
    long count;

    /**
     * 64-byte buffer (512 bits) for storing to-be-hashed characters
     */
    byte	buffer[];

    public MD5State() {
        buffer = new byte[64];
        count = 0;
        state = new int[4];

        state[0] = 0x67452301;
        state[1] = 0xefcdab89;
        state[2] = 0x98badcfe;
        state[3] = 0x10325476;

    }

    /** Create this State as a copy of another state */
    public MD5State (MD5State from) {
        this();

        int i;

        for (i = 0; i < buffer.length; i++)
            this.buffer[i] = from.buffer[i];

        for (i = 0; i < state.length; i++)
            this.state[i] = from.state[i];

        this.count = from.count;
    }

}
