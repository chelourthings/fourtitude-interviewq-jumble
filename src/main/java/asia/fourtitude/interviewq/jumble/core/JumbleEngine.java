package asia.fourtitude.interviewq.jumble.core;

import asia.fourtitude.interviewq.jumble.service.WordService;
import lombok.AllArgsConstructor;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class JumbleEngine {
    private WordService wordservice;
    private Set<Character> invalidCharacter = new HashSet<>();
    public JumbleEngine() throws IOException{
        this.wordservice = new WordService();
    }
    /**
     * From the input `word`, produces/generates a copy which has the same
     * letters, but in different ordering.
     *
     * Example: from "elephant" to "aeehlnpt".
     *
     * Evaluation/Grading:
     * a) pass unit test: JumbleEngineTest#scramble()
     * b) scrambled letters/output must not be the same as input
     *
     * @param word  The input word to scramble the letters.
     * @return  The scrambled output/letters.
     */
    public String scramble(String word) {

        if(word == null || word.length()<2){
            return word;
        }

        List<Character> characters = new ArrayList<>();

        for (char w : word.toCharArray()) {
            characters.add(w);
        }

        String scrambled;
        do{
        Collections.shuffle(characters);
        StringBuilder sb = new StringBuilder(characters.size());
        for (char w : characters) {
            sb.append(w);
        }
        scrambled = sb.toString();
        }while(scrambled.equals(word));

      return scrambled;
    }

//        char[] characters = word.toCharArray();
//
//        //shuffle the array
//        for (int i = characters.length -1; i>0; i--){
//            int j = (int) (Math.random() * (i+1));
//
//            char temp = characters[i];
//            characters[i]=characters[j];
//            characters[j] = temp;
//        }
//        return new String(characters);


    /**
     * Retrieves the palindrome words from the internal
     * word list/dictionary ("src/main/resources/words.txt").
     *
     * Word of single letter is not considered as valid palindrome word.
     *
     * Examples: "eye", "deed", "level".
     *
     * Evaluation/Grading:
     * a) able to access/use resource from classpath
     * b) using inbuilt Collections
     * c) using "try-with-resources" functionality/statement
     * d) pass unit test: JumbleEngineTest#palindrome()
     *
     * @return  The list of palindrome words found in system/engine.
     */
    public Collection<String> retrievePalindromeWords() throws IOException {
        List<String> palindromeWords = new ArrayList<>();
        for (String word : wordservice.getWordList()){
            if (word.length()>1 && isPalindrome(word)){
                palindromeWords.add(word);
            }
        }
        return palindromeWords;
    }

    static boolean isPalindrome(String word){
        String reversed = new StringBuilder(word).reverse().toString();
        return word.equals(reversed);
    }

    /**
     * Picks one word randomly from internal word list.
     *
     * Evaluation/Grading:
     * a) pass unit test: JumbleEngineTest#randomWord()
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param length  The word picked, must of length.
     * @return  One of the word (randomly) from word list.
     *          Or null if none matching.
     */
    static String getRandomWords(List<String> list){
        return list.get(new Random().nextInt(list.size()));
    }

    public String pickOneRandomWord(Integer length) throws IOException {
        if(length==null){
            return getRandomWords(wordservice.getWordList());
        }
        List<String> matchingWords = wordservice.getWordMap().entrySet().stream()
                .filter(entry -> entry.getValue().equals(length))
                .map(Map.Entry::getKey).collect(Collectors.toList());

        return matchingWords.isEmpty()? null : getRandomWords(matchingWords);

    }

    /**
     * Checks if the `word` exists in internal word list.
     * Matching is case insensitive.
     *
     * Evaluation/Grading:
     * a) pass related unit tests in "JumbleEngineTest"
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param word  The input word to check.
     * @return  true if `word` exists in internal word list.
     */
    public boolean exists(String word) throws IOException{

        if (word == null || word.isEmpty()){
            return false;
        }
        return wordservice.getWordSet().contains(word.trim().toLowerCase());
    }

    /**
     * Finds all the words from internal word list which begins with the
     * input `prefix`.
     * Matching is case insensitive.
     * <p>
     * Invalid `prefix` (null, empty string, blank string, non letter) will
     * return empty list.
     * <p>
     * Evaluation/Grading:
     * a) pass related unit tests in "JumbleEngineTest"
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param prefix The prefix to match.
     * @return The list of words matching the prefix.
     */
    private boolean isInvalidPrefix(String prefix){
        return prefix == null || prefix.trim().isEmpty() || wordservice.getInvalidCharacter().contains(prefix.charAt(0));
    }
    public Collection<String> wordsMatchingPrefix(String prefix) {
        if(isInvalidPrefix(prefix)){
            return new ArrayList<>();
        }
        String cleanedPrefix = prefix.trim().toLowerCase();

        List<String> matchingWords = new ArrayList<>();

        for (String words : wordservice.getWordList()){
            if (words.toLowerCase().trim().startsWith(prefix.toLowerCase().trim())){
                matchingWords.add(words);
            }
        }
        return matchingWords;

    }

    /**
     * Finds all the words from internal word list that is matching
     * the searching criteria.
     *
     * `startChar` and `endChar` must be 'a' to 'z' only. And case insensitive.
     * `length`, if have value, must be positive integer (>= 1).
     *
     * Words are filtered using `startChar` and `endChar` first.
     * Then apply `length` on the result, to produce the final output.
     *
     * Must have at least one valid value out of 3 inputs
     * (`startChar`, `endChar`, `length`) to proceed with searching.
     * Otherwise, return empty list.
     *
     * Evaluation/Grading:
     * a) pass related unit tests in "JumbleEngineTest"
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param startChar  The first character of the word to search for.
     * @param endChar    The last character of the word to match with.
     * @param length     The length of the word to match.
     * @return  The list of words matching the searching criteria.
     */
    public Collection<String> searchWords(Character startChar, Character endChar, Integer length) throws IOException {
        wordservice.populateInvalidCharacters();
        
        if (startChar == null && endChar == null && length == null) {
            return new ArrayList<>();
        }

        List<String> matchingWords = new ArrayList<>();

        for (String word : wordservice.getWordList()) {
            String cleanedWord = word.toLowerCase().trim();

            boolean matchesStart = (startChar == null || !invalidCharacter.contains(startChar) &&
                            cleanedWord.startsWith(startChar.toString().toLowerCase()));

            boolean matchesEnd = (endChar == null || !invalidCharacter.contains(endChar) &&
                            cleanedWord.endsWith(endChar.toString().toLowerCase()));

            boolean matchesLength = (length == null || length > 0 &&
                            cleanedWord.length() == length);

            if (matchesStart && matchesEnd && matchesLength) {
                matchingWords.add(word);
            }
        }

        return matchingWords;
    }


    /**
     * Generates all possible combinations of smaller/sub words using the
     * letters from input word.
     *
     * The `minLength` set the minimum length of sub word that is considered
     * as acceptable word.
     *
     * If length of input `word` is less than `minLength`, then return empty list.
     *
     * Example: From "yellow" and `minLength` = 3, the output sub words:
     *     low, lowly, lye, ole, owe, owl, well, welly, woe, yell, yeow, yew, yowl
     *
     * Evaluation/Grading:
     * a) pass related unit tests in "JumbleEngineTest"
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param word       The input word to use as base/seed.
     * @param minLength  The minimum length (inclusive) of sub words.
     *                   Expects positive integer.
     *                   Default is 3.
     * @return  The list of sub words constructed from input `word`.
     */

    private void generateSubWordsHelper(String remaining, Integer minLength, String current, Set<String> result) {
        if (current.length() >= (minLength != null ? minLength : 3)
                && wordservice.isValidWord(current)
                && !current.equalsIgnoreCase(remaining)) {
            result.add(current);
        }

        for (int i = 0; i < remaining.length(); i++) {
            String newRemaining = remaining.substring(0, i) + remaining.substring(i + 1);
            generateSubWordsHelper(newRemaining, minLength, current + remaining.charAt(i), result);
        }
    }
    public Collection<String> generateSubWords(String word, Integer minLength) throws IOException {

        Set<String> subWords = new HashSet<>();
        if (word == null || (minLength != null && minLength <= 0)) {
            return subWords;
        }

        word = word.trim();
        generateSubWordsHelper(word, minLength, "", subWords);

        subWords.remove(word);

        return subWords;
    }


    /**
     * Creates a game state with word to guess, scrambled letters, and
     * possible combinations of words.
     *
     * Word is of length 6 characters.
     * The minimum length of sub words is of length 3 characters.
     *
     * @param length     The length of selected word.
     *                   Expects >= 3.
     * @param minLength  The minimum length (inclusive) of sub words.
     *                   Expects positive integer.
     *                   Default is 3.
     * @return  The game state.
     */
    public GameState createGameState(Integer length, Integer minLength) throws Exception{
        Objects.requireNonNull(length, "length must not be null");
        if (minLength == null) {
            minLength = 3;
        } else if (minLength <= 0) {
            throw new IllegalArgumentException("Invalid minLength=[" + minLength + "], expect positive integer");
        }
        if (length < 3) {
            throw new IllegalArgumentException("Invalid length=[" + length + "], expect greater than or equals 3");
        }
        if (minLength > length) {
            throw new IllegalArgumentException("Expect minLength=[" + minLength + "] greater than length=[" + length + "]");
        }
        String original = this.pickOneRandomWord(length);
        if (original == null) {
            throw new IllegalArgumentException("Cannot find valid word to create game state");
        }
        String scramble = this.scramble(original);
        Map<String, Boolean> subWords = new TreeMap<>();
        for (String subWord : this.generateSubWords(original, minLength)) {
            subWords.put(subWord, Boolean.FALSE);
        }
        return new GameState(original, scramble, subWords);
    }

}
