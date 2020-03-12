package cryptopals.challenges;

import cryptopals.utils.Utils;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Six {

    public static String breakTheCipher(String input) {
        byte[] contentBytes = Base64.getDecoder().decode(input);

        String debugString = Base64.getEncoder().encodeToString(contentBytes);
        assert debugString.equals(input);

        HashMap<Integer, Double> hammingPairs = new HashMap<>();

        //find the hamming distance between blocks of the input
        for (int candidateKeySize = 2; candidateKeySize <= 40; candidateKeySize++) {
            byte[] firstNBytes = Utils.sliceByteArray(contentBytes, 0, candidateKeySize);
            byte[] secondNBytes = Utils.sliceByteArray(contentBytes, candidateKeySize, candidateKeySize);
            byte[] thirdNBytes = Utils.sliceByteArray(contentBytes, candidateKeySize * 2, candidateKeySize);
            byte[] fourthNBytes = Utils.sliceByteArray(contentBytes, candidateKeySize * 3, candidateKeySize);
            double hammingDist1 = (double) Utils.calculateHammingDistance(firstNBytes, secondNBytes) / candidateKeySize;
            double hammingDist2 = (double) Utils.calculateHammingDistance(secondNBytes, thirdNBytes) / candidateKeySize;
            double hammingDist3 = (double) Utils.calculateHammingDistance(thirdNBytes, fourthNBytes) / candidateKeySize;
            double averageHammingDistance = (hammingDist1 + hammingDist2 + hammingDist3) / 3;
            hammingPairs.put(candidateKeySize, averageHammingDistance);
        }

        //get the best three hamming distances
        Integer[] bestSizes = hammingPairs.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(3)
                .map(Map.Entry::getKey)
                .toArray(Integer[]::new);

        String best = null;
        double lowFullScore = Double.MAX_VALUE;
        for (int keysize : bestSizes) {
            //break the cipher text into blocks of length k
            //matrix
            int matrixHeight = (contentBytes.length % keysize == 0) ? contentBytes.length/keysize : contentBytes.length/keysize + 1;
            byte[][] matrix = new byte[matrixHeight][keysize];
            for (int i = 0; i<matrixHeight; i++){
                matrix[i] = Utils.sliceByteArray(contentBytes,i*keysize, keysize);
            }

            //transpose the blocks. group 1 is the first byte of each block, group 2 is the second, etc
            byte[][] transposed = new byte[keysize][matrixHeight];
            for (int y = 0; y < matrixHeight; y++) {
                for (int x = 0; x < keysize; x++) {
                    transposed[x][y] = matrix[y][x];
                }
            }

            //decrypt each block as if it was single char xor
            byte[] keybytes = new byte[keysize];
            for (int block = 0; block < keysize; block++) {
                HashMap<Integer, Double> resultMap = new HashMap<>();
                int bestKeyInt = -1;
                double lowSingleScore = Double.MAX_VALUE;
                for (int c = 0; c < 256; c++) {
                    char[] decrypted = Utils.singleKeyXOR(transposed[block], c);
                    double chiScore = Utils.chiSquaredScore(decrypted);
                    if (chiScore < lowSingleScore) {
                        lowSingleScore = chiScore;
                        bestKeyInt = c;
                    }
                }
                assert bestKeyInt != -1;
                keybytes[block] = (byte) bestKeyInt;
            }

            //decrypt the body
            String decryptedBody = new String(Utils.multiByteXOR(contentBytes, keybytes));

            //chi square score the body
            double fullChi = Utils.chiSquaredScore(decryptedBody.toCharArray());

            //check if better
            if(fullChi < lowFullScore) {
                best = decryptedBody;
            }
        }

        assert best != null;

        //return it
        return best;

    }

}
