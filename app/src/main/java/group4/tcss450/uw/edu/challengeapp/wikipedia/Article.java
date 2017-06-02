package group4.tcss450.uw.edu.challengeapp.wikipedia;

/**
 * A helper class to contain a short description of Wikipedia article.
 */
public class Article {

    private String title;
    private String header;
    private String url;
    private String content;


    public Article() {
    }

    /**
     * Creates new wikipedia article with parameters
     * @param title - wikipedia title
     * @param header - short description
     * @param url - urt to the article
     */
    public Article(String title, String header, String url) {
        this.title = title;
        this.header = header;
        this.url = url;
    }

    // Setters and Getters below
    public void setTitle(String title) {
        this.title = title;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getTitle() {
        return title;
    }

    public String getHeader() {
        return header;
    }

    public String getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }
}
