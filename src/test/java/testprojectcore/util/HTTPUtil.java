package testprojectcore.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import testprojectcore.core.DriverManager;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import testprojectcore.dataprovider.TestConfigProvider;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Eren Demirel
 *
 */
public class HTTPUtil {

    /**
     * Method to capture network traffic. It can be used for a lots of purposes
     * including extracting request/response body and HTTP status code.
     * Captures the info from browser performance logs. Performance logs should be enabled beforehand
     *
     * @param returnOrNull Since performance logs output a huge amount of data, do not return if not needed
     * @param enableLog    Logs the traffic when enabled
     * @return LogEntries or null
     */
    public LogEntries captureBrowserNetworkTrafficChrome(boolean returnOrNull, boolean enableLog) throws Exception {
        final Logger logger = Logger.getLogger("Browser Network Traffic Logger");
        LogEntries logEntries = null;
        try {
            if (TestConfigProvider.WEBTESTPROPERTIES.getPropertyValue("performanceLogs").equals("enabled") ||
                    TestConfigProvider.WEBTESTPROPERTIES.getPropertyValue("performanceLogs").equals("true")) {
                logEntries = DriverManager.getDriver().manage().logs().get(LogType.PERFORMANCE);
                if (enableLog) {
                    for (LogEntry le : logEntries) {
                        logger.info(le.getMessage());
                    }
                }
            }
            return returnOrNull ? logEntries : null;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<LogEntry> captureRequestAndResponseHeadersChrome() throws Exception {
        final Logger logger = Logger.getLogger("Browser Network Traffic Request-Response Logger");
        List<LogEntry> reqResLogEntries = new ArrayList<LogEntry>();
        LogEntries logEntries = captureBrowserNetworkTrafficChrome(true, false);
        for (LogEntry le : logEntries) {
            if (le.getMessage().contains("Network.requestWillBeSent") || le.getMessage().contains("Network.requestWillBeSentExtraInfo") ||
                    le.getMessage().contains("Network.responseReceived") || le.getMessage().contains("Network.responseReceivedExtraInfo")) {
                reqResLogEntries.add(le);
            }
        }
        Stream.of(reqResLogEntries.toString())
                .forEach(logger::info);
        return reqResLogEntries;
    }

    public void returnHTTPstatusCodeAndCaptureRequestResponseHeadersChrome() throws Exception {
        final Logger logger = Logger.getLogger("Browser Network Traffic Request-Response Logger");
        List<LogEntry> reqResLogEntries = new ArrayList<LogEntry>();
        LogEntries logEntries = captureBrowserNetworkTrafficChrome(true, false);
        for (LogEntry le : logEntries) {
            if (le.getMessage().contains("Network.requestWillBeSent") || le.getMessage().contains("Network.requestWillBeSentExtraInfo") ||
                    le.getMessage().contains("Network.responseReceived") || le.getMessage().contains("Network.responseReceivedExtraInfo")) {
                reqResLogEntries.add(le);
            }
        }
        Stream.of(reqResLogEntries.toString())
                .forEach(logger::info);
        final Logger logger2 = Logger.getLogger("Browser Network Traffic Response HTTP Status Code Logger");
        String currentURL = DriverManager.getDriver().getCurrentUrl();
        int status = -1;
        for (Iterator<LogEntry> it = logEntries.iterator(); it.hasNext(); ) {
            LogEntry entry = it.next();
            try {
                JSONObject json = new JSONObject(entry.getMessage());
                JSONObject message = json.getJSONObject("message");
                String method = message.getString("method");
                if (method != null
                        && "Network.responseReceived".equals(method)) {
                    JSONObject params = message.getJSONObject("params");
                    JSONObject response = params.getJSONObject("response");
                    String messageUrl = response.getString("url");
                    if (currentURL.equals(messageUrl)) {
                        status = response.getInt("status");
                        logger2.info("HTTP response code returned for " + messageUrl + ": " + status);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void downloadAFileFromaUrl(String url) throws IOException {
        int CONNECTION_TIMEOUT = 10 * 1000;     //10 seconds
        int READ_TIMEOUT = 15 * 1000;       //15 seconds
        String TARGET_PARENT_DIRECTORY = "src/test/resources/contractfiles/";
        URL uri = new URL(url);
        System.out.println("Extention of file to download is: " + FilenameUtils.getExtension(url));
        File file = new File(TARGET_PARENT_DIRECTORY, System.currentTimeMillis() + "_" + StringUtils.substringAfterLast(url, "/"));
        try {
            FileUtils.copyURLToFile(
                    new URL(uri.toString()),
                    file,
                    CONNECTION_TIMEOUT,
                    READ_TIMEOUT);
        } catch (IOException io) {
            System.out.println(("Error while downloading file " + io));
        }
    }
}


