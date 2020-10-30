import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.SubmissionKind;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.references.SubredditReference;

public class Reddit {
    public void submitToReddit(String Title, String Link) {
        UserAgent userAgent = new UserAgent("bot", "APP_ID", "v0.1", "REDDIT_NAME");
        // Create our credentials
        Credentials credentials = Credentials.script("REDDIT_NAME", "REDDIT_PASSWORD", "REDDIT_SECRET", "REDDIT_DEVELOPER_KEY");
        // This is what really sends HTTP requests
        NetworkAdapter networkAdapter = new OkHttpNetworkAdapter(userAgent);
        // Authenticate and get a RedditClient instance
        RedditClient reddit = OAuthHelper.automatic(networkAdapter, credentials);
        SubredditReference subreddit = reddit.subreddit("SUBREDDIT");
        try {
            subreddit.submit(SubmissionKind.LINK, Title, Link, true);
            System.out.println("Post submited");
        }
        catch (Exception e){
            System.out.println("Already submited");
        }
    }
}
