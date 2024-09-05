package asia.fourtitude.interviewq.jumble.service;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;

import java.io.IOException;
import java.io.BufferedReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data


public class WordService {

    @Autowired
    private ResourceLoader resourceLoader;
    private Map<String, Integer> wordMap;
    private Set<String> wordSet;
    private Set<Character> invalidCharacter;
    private List<String> wordList;

    public WordService() throws IOException{
        this.resourceLoader=new DefaultResourceLoader();
        initializeCollection();
        validateResourceLoader();
        loadWordsFromFile();

    }
    private void initializeCollection(){
        wordMap= new HashMap<>();
        wordSet= new HashSet<>();
        wordList= new ArrayList<>();
        invalidCharacter = new HashSet<>();
    }

    private void validateResourceLoader(){
        if (resourceLoader == null){
            throw new IllegalArgumentException("ResourceLoader is not properly initialised.");
        }
    }

    private void processWord(String word){
        wordMap.put(word, word.length());
        wordList.add(word);
        wordSet.add(word);
    }

    private void loadWordsFromFile() throws IOException{
        Resource resource = resourceLoader.getResource("classpath:/words.txt");

        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()))){

            String line;
            while((line = bufferedReader.readLine()) !=null){
                processWord(line.trim());
            }
        }catch (IOException e){
            System.out.println("Error reading words from file" + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private void populateInvalidCharacters(){
        String invalidChars = ",./<>?;':\"[]{}\\|1234567890!@#$%^&*()`~";
        for( char w : invalidChars.toCharArray()){
            invalidCharacter.add(w);
        }
    }
    public boolean isValidWord(String word){
        return wordSet.contains(word);
    }
}
