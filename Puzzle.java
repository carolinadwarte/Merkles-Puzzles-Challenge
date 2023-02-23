import javax.crypto.SecretKey;

/**
 * This class implements and stores a Puzzle. Puzzles have a distinct puzzle number and a 16byte AES key.
 */
public class Puzzle {
    private int puzzleNumber;
    private SecretKey key;

    /**
     * Create a puzzle object.
     *
     * @param puzzleNumber The number of the puzzle (in this case 0-4096).
     * @param key          A secret AES key.
     */
    public Puzzle(int puzzleNumber, SecretKey key) {
        this.puzzleNumber = puzzleNumber;
        this.key = key;
    }

    /**
     * Sets a puzzle number.
     *
     * @param puzzleNumber The number of the puzzle (in this case 0-4096).
     */
    public void setPuzzleNumber(int puzzleNumber) {
        this.puzzleNumber = puzzleNumber;
    }

    /**
     * Sets a AES key.
     *
     * @param key A secret AES key.
     */
    public void setKey(SecretKey key) {
        this.key = key;
    }

    /**
     * Gets a puzzle number.
     *
     * @return a puzzle number (int)
     */
    public int getPuzzleNumber() {
        return puzzleNumber;
    }

    /**
     * Gets a puzzles secret key.
     *
     * @return a secret AES key (SecretKey)
     */
    public SecretKey getKey() {
        return key;
    }

    /**
     * Gets a puzzle in the following format:
     * 16byte zero;
     * 2byte puzzle number in the range of 1-4096;
     * 16byte secret AES key.
     *
     * @return a byte array of a puzzle in the above format
     */
    public byte[] getPuzzleAsBytes() {
        byte[] start = new byte[16]; // starts with 128 zero bits (16-bytes)
        byte[] puzzleNumBytes = CryptoLib.smallIntToByteArray(puzzleNumber); // 16-bit (2-byte) puzzle number in the range 1 to 4096
        byte[] keyBytes = key.getEncoded(); // 128-bit (16-byte) AES key
        byte[] puzzleAsBytes = new byte[34];

        System.arraycopy(start, 0, puzzleAsBytes, 0, 16);
        System.arraycopy(puzzleNumBytes, 0, puzzleAsBytes, 16, 2);
        System.arraycopy(keyBytes, 0, puzzleAsBytes, 18, 16);

        return puzzleAsBytes;
    }
}
