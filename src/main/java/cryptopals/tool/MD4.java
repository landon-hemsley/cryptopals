package cryptopals.tool;

import org.bouncycastle.crypto.digests.MD4Digest;

/**
 * a wrapping class for basic features of SHA-1, as implemented in the bouncycastle library
 * @see MD4Digest
 */
public class MD4 extends AbstractDigestWrapper<MD4Digest> {
    private final MD4Digest d = new MD4Digest();

    @Override
    protected MD4Digest getDigest() {
        return d;
    }

    @Override
    protected int getBlockSize() {
        return 64;
    }
}
