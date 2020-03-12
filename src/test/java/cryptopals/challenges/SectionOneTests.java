package cryptopals.challenges;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cryptopals.utils.Utils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class SectionOneTests {

    @Test
    public void oneTest() throws DecoderException {
        String input = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
        String result = One.convertHexToBase64(input);
        assertEquals("SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t", result);
        String reconverted = Hex.encodeHexString(Base64.getDecoder().decode(result));
        assertEquals(input, reconverted);
    }

    @Test
    public void twoTest() throws DecoderException {
        String input1 = "1c0111001f010100061a024b53535009181c";
        String input2 = "686974207468652062756c6c277320657965";
        String result = Two.fixedXOR(input1, input2);
        assertEquals("746865206b696420646f6e277420706c6179", result);
    }

    @Test
    public void threeTest() throws DecoderException {
        String value = Three.decrypt(Hex.decodeHex("1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736"));
        assertNotNull(value);
        assertEquals("Cooking MC's like a pound of bacon", value);
    }

    @Test
    public void fourTest() throws DecoderException, IOException {
        String filePath = "src/test/resources/4.txt";
        List<String> contents = Utils.readFileAsListOfLines(filePath);
        String value = Four.seekAndDestroy(contents);
        assertEquals("Now that the party is jumping", value);
    }

    @Test
    public void fiveTest() throws IOException, DecoderException {
        String toEncrypt = "Burning 'em, if you ain't quick and nimble\n" +
                "I go crazy when I hear a cymbal";
        var result = Five.repeatingKeyEncrypt(toEncrypt);
        var expected = "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f";
        assertEquals(expected, result);

        var bloodContents = Utils.readFileAsWhole("src/test/resources/blood");
        var singleStringBloodContents = String.join("\n", bloodContents);

        encryptAndOutputAndDecryptAndOutput(singleStringBloodContents, false);
        encryptAndOutputAndDecryptAndOutput(Utils.readFileAsWhole("src/test/resources/enid.jok"), false);
        encryptAndOutputAndDecryptAndOutput(Utils.readFileAsWhole("src/test/resources/einstein"), false);
        encryptAndOutputAndDecryptAndOutput(Utils.readFileAsWhole("src/test/resources/spock.txt"), true);
    }

    private void encryptAndOutputAndDecryptAndOutput(String original, boolean print) throws DecoderException {
        var encrypted = Five.repeatingKeyEncrypt(original);
        var decrypted = Five.repeatingKeyDecrypt(encrypted);

        assertEquals(original, decrypted);

        if(print) {
            System.out.println(encrypted);
            System.out.println(decrypted);
        }
    }


    @Test
    public void sixTest() throws IOException {
        String first = "this is a test";
        String second = "wokka wokka!!!";
        int hamming = Utils.calculateHammingDistance(first.getBytes(), second.getBytes());
        assertEquals(37, hamming);

        //read the file contents into a single string
        var fileContents = Utils.readFileAsListOfLines("src/test/resources/6.txt");
        var joinedContents = String.join("", fileContents);

        String decrypted = Six.breakTheCipher(joinedContents);
        System.out.println(decrypted);
        assertTrue(decrypted.contains("I'm back and I'm ringin' the bell"));
    }

}
