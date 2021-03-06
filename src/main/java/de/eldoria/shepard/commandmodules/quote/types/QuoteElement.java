package de.eldoria.shepard.commandmodules.quote.types;

public class QuoteElement {
    private final String quote;
    private final int quoteId;

    /**
     * Creates new Quote object.
     *
     * @param quote   quote
     * @param quoteId id of quote
     */
    public QuoteElement(String quote, int quoteId) {
        this.quote = quote;
        this.quoteId = quoteId;
    }

    /**
     * Get the Quote text.
     *
     * @return String not null.
     */
    public String getQuote() {
        return quote;
    }

    /**
     * Get the Quote id.
     *
     * @return String not null. -1 if not set
     */
    public int getQuoteId() {
        return quoteId;
    }
}
