package x00Hero.MyLogger.Constructors;

import java.util.HashMap;

public class ReplacementString {
    HashMap<String, String> replacements = new HashMap<>();

    public ReplacementString() {

    }

    public ReplacementString(String value, String replacement) {
        addReplacement(value, replacement);
    }

    public void addReplacement(String value, String replacement) {
        replacements.put(value, replacement);
    }

    public void removeReplacement(String value) {
        replacements.remove(value);
    }

    public void setReplacements(HashMap<String, String> replacements) {
        this.replacements = replacements;
    }

    public String format(String input) {
        for(String key : replacements.keySet()) {
            input = input.replace(key, replacements.get(key));
        }
        return input;
    }
}
