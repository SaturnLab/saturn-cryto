
package io.saturnlab.crypto.cryptohash;

import org.spongycastle.util.encoders.Hex;

import static org.junit.Assert.*;

/**
 * Generic test utility class that gets extended from the digest test
 * classes.
 * @author Stephan Fuhrmann &lt;stephan@tynne.de&gt;
 */
public class AbstractCryptoTest {

    protected void testKatHex(Digest dig, String data, String ref) {
        testFrom(dig, Hex.decode(data), Hex.decode(ref));
    }

    /** Does the comparison using the digest and some calls on it.
     * @param digest the digest to operate on.
     * @param message the input data to pass to the digest.
     * @param expected the expected data out of the digest.
     */
    private static void testFrom(Digest digest, byte[] message, byte[] expected) {
        /*
         * First test the hashing itself.
         */
        byte[] out = digest.digest(message);
        assertArrayEquals(expected, out);

        /*
         * Now the update() API; this also exercises auto-reset.
         */
        for (int i = 0; i < message.length; i++) {
            digest.update(message[i]);
        }
        assertArrayEquals(expected, digest.digest());

        /*
         * The cloning API.
         */
        int blen = message.length;
        digest.update(message, 0, blen / 2);
        Digest dig2 = digest.copy();
        digest.update(message, blen / 2, blen - (blen / 2));
        assertArrayEquals(expected, digest.digest());
        dig2.update(message, blen / 2, blen - (blen / 2));
        assertArrayEquals(expected, dig2.digest());
    }
}