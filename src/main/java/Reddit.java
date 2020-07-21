import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.SubmissionKind;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.references.SubredditReference;
import org.apache.http.client.ClientProtocolException;

import java.io.IOException;

public class Reddit {
    public void submitToReddit(String Title, String Link) throws IOException {
        UserAgent userAgent = new UserAgent("bot", "", "v0.1", "");
        // Create our credentials
        Credentials credentials = Credentials.script("", "", "", "");
        // This is what really sends HTTP requests
        NetworkAdapter networkAdapter = new OkHttpNetworkAdapter(userAgent);
        // Authenticate and get a RedditClient instance
        RedditClient reddit = OAuthHelper.automatic(networkAdapter, credentials);
        SubredditReference subreddit = reddit.subreddit("");
        try {
            subreddit.submit(SubmissionKind.LINK, Title, Link, true);
            System.out.println("Post Submited");
        }
        catch (Exception e){
            System.out.println("Already submited");
        }
    }
}
