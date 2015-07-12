package de.luh.hci.pcl.boxhandschuh.controller;

import java.util.HashMap;
import java.util.Map;

import de.luh.hci.pcl.boxhandschuh.model.Combination;

public class TrainingCombinations {
    
    private static String path = "data/combinations/";
    
    private Map combinations;
    
    private Combination activeCombination;
    
    private JSONFileWriter filewriter;
    
    public TrainingCombinations() {
        super();
        combinations = new HashMap<String, Combination>();
        
    }
    
    public void saveCombination(Combination combination) {
        filewriter = new JSONFileWriter(getFileName(combination));
        filewriter.write(combination);
    }

    public void loadCombination(String file){
        
        filewriter = new JSONFileWriter(file);
        Combination combination = (Combination) filewriter.read(Combination.class);
        combinations.put(combination.getName(), combination);
        
    }
    
    
    public Boolean selectActiveCombination(String name) {
        activeCombination = (Combination) combinations.get(name);
        return (activeCombination!= null);
    }
    
    private String getFileName(Combination combination) {
        return path + combination.getName() + ".json";
    }
    
}
