import javax.crypto.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * This class uses the PuzzleCracker class to perform Bob's actions in the communication and demonstrates that
 * Merkles puzzle communication is completed.
 */
public class Merkle {
    public static void main(String[] args) {
        PuzzleCreator puzzleCreator = new PuzzleCreator();
        puzzleCreator.createPuzzles();
        puzzleCreator.encryptPuzzlesToFile("puzzle.bin");

        PuzzleCracker puzzleCracker = new PuzzleCracker("puzzle.bin");
        Puzzle crackedP = puzzleCracker.crack(50);
        SecretKey aliceKey = puzzleCreator.findKey(50);
        String message = "Testing Merkles Puzzles!";
        byte[] encryptedM;

        try {
            Cipher newC = Cipher.getInstance("AES");
            newC.init(Cipher.ENCRYPT_MODE, aliceKey);
            encryptedM = newC.doFinal(message.getBytes());

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }

        puzzleCracker.decryptMessage(encryptedM);
    }
}
