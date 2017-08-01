import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class BloomFilterDictionary {
    MessageDigest md;
    byte[] dictionary;

    BloomFilterDictionary() throws NoSuchAlgorithmException {
        md = MessageDigest.getInstance("SHA-256");
        dictionary = new byte[8192];
    }

    boolean getBit(int bit) {
        return (dictionary[bit/8] & 0x1 << bit % 8) != 0;
    }

    void setBit(int bit) {
        dictionary[bit/8] |= 0x1 << (bit % 8);
    }

    void clearBit(int bit) {
        dictionary[bit/8] &= 0xff - (0x1 << (bit % 8));
    }

    byte[] getDigest(String word) {
        md.reset();
        md.update(word.getBytes());
        byte[] digest = md.digest();

        return digest;
    }

    void addWord(String word) {
        byte [] digest = getDigest(word);
        
        for (int i = 0; i < md.getDigestLength(); i += 2) {
            int bit = digest[i] & 0xff + (digest[i+1] << 4 & 0xff00);
            setBit(bit);
        }
    }

    boolean isWord(String word) {
        byte [] digest = getDigest(word);
        
        for (int i = 0; i < md.getDigestLength(); i += 2) {
            int bit = digest[i] & 0xff + (digest[i+1] << 4 & 0xff00);
            if (!getBit(bit)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String args[]) throws Exception {
        BloomFilterDictionary dict = new BloomFilterDictionary();
 
        dict.addWord("apple");
        System.out.println(dict.isWord("apple"));
        System.out.println(dict.isWord("bapple"));
        dict.addWord("banana");
        System.out.println(dict.isWord("banana"));
    }
}
