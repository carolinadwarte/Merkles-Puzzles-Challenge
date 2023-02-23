import javax.crypto.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class generates an Arraylist of 4096 puzzles and encrypts each one onto a file.
 */
public class PuzzleCreator {
    private ArrayList<Puzzle> puzzleArrayList;

    /**
     * Constructor for a PuzzleCreator instance.
     */
    public PuzzleCreator() {
    }

    /**
     * Generates an ArrayList of 4096 valid puzzle instances.
     *
     * @return an ArrayList of 4096 puzzle items.
     */
    public ArrayList<Puzzle> createPuzzles() {
        puzzleArrayList = new ArrayList<>();
        for (int i = 1; i <= 4096; i++) {
            try {
                puzzleArrayList.add(new Puzzle(i, CryptoLib.createAESKey(createRandomKeyAES())));
            } catch (InvalidKeySpecException | NoSuchAlgorithmException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        }
        return puzzleArrayList;
    }

    /**
     * Generates a byte array in the required format (last 6bytes are zeros) that can be used to form a DES key.
     *
     * @return a byte array in the above specified format.
     */
    public byte[] createRandomKey() {
        byte[] randomKey = new byte[8];

        Random random = new Random();
        byte[] start = new byte[2];
        random.nextBytes(start);

        System.arraycopy(start, 0, randomKey, 0, 2);
        return randomKey;
    }

    /**
     * Generates a byte array that can be used to form a AES key.
     *
     * @return byte array of size 16byte
     */
    public byte[] createRandomKeyAES() {
        Random random = new Random();
        byte[] randomKey = new byte[16];
        random.nextBytes(randomKey);
        return randomKey;
    }

    /*encrypts the puzzles byte representation
    into a byte array representing the encrypted puzzle*/

    /**
     * This method encrypts the byte representation of a puzzle.
     *
     * @param key       byte array representing a key
     * @param newPuzzle a puzzle instance
     * @return a byte array representing an encrypted puzzle
     */
    public byte[] encryptPuzzle(byte[] key, Puzzle newPuzzle) {
        byte[] puzzleAsBytes = newPuzzle.getPuzzleAsBytes();
        SecretKey keyDES;

        try {
            keyDES = CryptoLib.createDESKey(key);
            Cipher c = Cipher.getInstance("DES");
            c.init(Cipher.ENCRYPT_MODE, keyDES);
            return c.doFinal(puzzleAsBytes);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidKeySpecException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method encrypts a puzzle ArrayList of size 4096 into a binary file.
     *
     * @param fileName a string representing a file name
     */
    public void encryptPuzzlesToFile(String fileName) {
        byte[] encryptedPuzzles = new byte[163840];
        for (int i = 0; i < puzzleArrayList.size(); i++) {
            Puzzle p = puzzleArrayList.get(i);
            byte[] encryptedP = encryptPuzzle(createRandomKey(), p);
            System.arraycopy(encryptedP, 0, encryptedPuzzles, i * 40, 40);
        }
        FileOutputStream myFile = null;
        try {
            myFile = new FileOutputStream(fileName);
            myFile.write(encryptedPuzzles, 0, encryptedPuzzles.length);
            myFile.flush();
            myFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds the corresponding AES key for an input puzzle number.
     *
     * @param puzzleNumber an int representing a puzzle number
     * @return a SecretKey object representing an AES key
     */
    public SecretKey findKey(int puzzleNumber) {
        return puzzleArrayList.get(puzzleNumber-1).getKey();
    }
}
