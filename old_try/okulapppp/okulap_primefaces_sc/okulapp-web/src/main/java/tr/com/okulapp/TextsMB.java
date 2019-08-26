/*
 * 
 * 
 * 
 */
package tr.com.okulapp;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
@Named(value = "texts")
@ApplicationScoped
public class TextsMB {

    private String[] languages = new String[]{"tr-TR", "en-US"};

    private Map<String, ResourceBundle> mapResourceBundle = null;

    public ResourceBundle getResourceBundle(String language) {
        if (mapResourceBundle == null) {
            mapResourceBundle = new HashMap();
            for (String lang : languages) {
                Locale browserLocale = Locale.forLanguageTag(lang);
                ResourceBundle resourceBundle = ResourceBundle.getBundle("tr.com.okulapp.texts.texts", browserLocale);
                if (resourceBundle != null) {
                    mapResourceBundle.put(lang, resourceBundle);
                }
            }
        }
        return mapResourceBundle.get(language);
    }

    /**
     * Creates a new instance of TextsMB
     */
    public TextsMB() {
    }

    public String getText(String key) {
        return getResourceBundle(languages[0]).getString(key);
    }
}
