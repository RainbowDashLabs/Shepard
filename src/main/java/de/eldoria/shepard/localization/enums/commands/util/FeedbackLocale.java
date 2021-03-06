package de.eldoria.shepard.localization.enums.commands.util;

public enum FeedbackLocale {
    /**
     * Localization key for description.
     */
    DESCRIPTION("command.feedback.description"),

    M_THANK_YOU("command.feedback.message.thankYou");

    /**
     * Get the escaped locale code for auto translation.
     */
    public final String tag;

    /**
     * Create a new locale object.
     *
     * @param localeCode locale code
     */
    FeedbackLocale(String localeCode) {
        this.tag = "$" + localeCode + "$";
    }

    @Override
    public String toString() {
        return tag;
    }

}
