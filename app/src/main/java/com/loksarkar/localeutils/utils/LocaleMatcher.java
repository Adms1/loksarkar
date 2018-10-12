

package com.loksarkar.localeutils.utils;

import java.util.Locale;


public class LocaleMatcher {

    /**
     * Enum representing the level of matching of two Locales.
     */
    public enum MatchLevel {
        NoMatch,
        LanguageMatch,
        LanguageAndCountryMatch,
        CompleteMatch
    }

    private LocaleMatcher() {
    }

    /**
     * Method to determine the level of matching of two Locales.
     * @param l1
     * @param l2
     * @return
     */
    public static MatchLevel match(Locale l1, Locale l2) {
        MatchLevel matchLevel = MatchLevel.NoMatch;
        if (l1.equals(l2)) {
            matchLevel = MatchLevel.CompleteMatch;
        } else if (l1.getLanguage().equals(l2.getLanguage()) && l1.getCountry().equals(l2.getCountry())) {
            return MatchLevel.LanguageAndCountryMatch;
        } else if (l1.getLanguage().equals(l2.getLanguage())) {
            return MatchLevel.LanguageMatch;
        }
        return matchLevel;
    }
}
