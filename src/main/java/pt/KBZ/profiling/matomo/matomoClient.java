package matomo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.piwik.java.tracking.PiwikRequest;
import org.piwik.java.tracking.PiwikTracker;

public class matomoClient {

    String actionUrl;
    String hostUrl;
    Integer siteId;

    public matomoClient() {
        actionUrl = "http://qualichain.epu.ntua.gr";
        hostUrl = "http://vpnknowledgebiz.ddns.net/matomo/matomo.php";
        siteId = 1;

    }

    public void send(String category, String action, String name, Integer value) {

        try {
            /*
             * <!-- Matomo --> <script type="text/javascript"> var _paq = window._paq =
             * window._paq || []; / tracker methods like "setCustomDimension" should be
             * called before "trackPageView" / _paq.push(['trackPageView']);
             * _paq.push(['enableLinkTracking']); (function() { //var
             * u="//localhost/matomo/matomo/"; var u="//knowledgebizvpn.ddns.net/matomo/";
             * _paq.push(['setTrackerUrl', u+'matomo.php']); _paq.push(['setSiteId', '1']);
             * var d=document, g=d.createElement('script'),
             * s=d.getElementsByTagName('script')[0]; g.type='text/javascript';
             * g.async=true; g.src=u+'matomo.js'; s.parentNode.insertBefore(g,s); })();
             * </script>
             */

            /*
             * <!-- Matomo Tag Manager --> <script type="text/javascript"> var _mtm =
             * window._mtm = window._mtm || []; _mtm.push({'mtm.startTime': (new
             * Date().getTime()), 'event': 'mtm.Start'}); var d=document,
             * g=d.createElement('script'), s=d.getElementsByTagName('script')[0];
             * g.type='text/javascript'; g.async=true;
             * g.src='http://knowledgebizvpn.ddns.net/matomo/js/container_4Y0Ogro1.js';
             * s.parentNode.insertBefore(g,s); </script> <!-- End Matomo Tag Manager -->
             */
            PiwikRequest request = new PiwikRequest(siteId, new URL(actionUrl));
            // request.setActionName("myAction");
            // request.addCustomTrackingParameter(key, value);

            request.setEventCategory(category);
            request.setEventAction(action);
            request.setEventName(name);
            request.setEventValue(value);

            /*
             * request.setEventCategory("counters"); request.setEventAction("count"); //
             * request.setEventName("job_applicants_event");
             * request.setEventName("matched_candidates_tag"); request.setEventValue(816);
             * 
             */
            PiwikTracker tracker = new PiwikTracker(hostUrl);

            HttpResponse response = tracker.sendRequest(request);
            System.out.println(response);

            // Future<HttpResponse> Future = tracker.sendRequestAsync(request);
            // System.out.println(Future);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
