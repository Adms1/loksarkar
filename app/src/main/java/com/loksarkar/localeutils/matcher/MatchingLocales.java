

package com.loksarkar.localeutils.matcher;

import com.loksarkar.localeutils.LocalePreference;

import java.util.Locale;

public final class MatchingLocales {
    private Locale supportedLocale;
    private Locale systemLocale;

    public MatchingLocales(Locale supportedLocale, Locale systemLocale) {
        this.supportedLocale = supportedLocale;
        this.systemLocale = systemLocale;
    }

    public Locale getSupportedLocale() {
        return supportedLocale;
    }

    public Locale getSystemLocale() {
        return systemLocale;
    }

    public Locale getPreferredLocale(LocalePreference preference) {
        return preference.equals(LocalePreference.PreferSupportedLocale) ?
                supportedLocale :
                systemLocale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MatchingLocales that = (MatchingLocales) o;

        return supportedLocale.equals(that.supportedLocale)
                && systemLocale.equals(that.systemLocale);
    }

    @Override
    public int hashCode() {
        int result = supportedLocale.hashCode();
        result = 31 * result + systemLocale.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return supportedLocale.toString() + ", " + systemLocale.toString();
    }
}
