package url;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

public class HttpUrlConnectionService {
	static URL url;
	static final String TAG = "HtpUrlConnectionService";

	public synchronized static InputStream connectToUrlWithCookie(String url,
			String cookie) throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();

		// Create a local instance of cookie store
		CookieStore cookieStore = new BasicCookieStore();
		// Create local HTTP context
		HttpContext localContext = new BasicHttpContext();
		// Bind custom cookie store to the local context
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		// agent Client connection
		client.getParams()
				.setParameter(
						CoreProtocolPNames.USER_AGENT,
						"Mozilla/5.0 (X11; Linux i686) AppleWebKit/534.30 (KHTML, like Gecko) Ubuntu/11.04 Chromium/12.0.742.112 Chrome/12.0.742.112 Safari/534.30;CharSet=UTF-8");
		// encoding UTF-8
		client.getParams().setParameter(
				CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request, localContext);
		if (response != null)
			Log.d(TAG, "response HttpResponse not null ");

		return response.getEntity().getContent();
	}

	public synchronized static InputStream connectToUrl(String url)
			throws ClientProtocolException, IOException {
		DefaultHttpClient client = new DefaultHttpClient();

		HttpProtocolParams.setUseExpectContinue(client.getParams(), false);
		HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {

			public boolean retryRequest(IOException exception,
					int executionCount, HttpContext context) {
				// retry a max of 5 times
				if (executionCount >= 5) {
					return false;
				}
				if (exception instanceof NoHttpResponseException) {
					return true;
				} else if (exception instanceof ClientProtocolException) {
					return true;
				}
				return false;
			}
		};
		client.setHttpRequestRetryHandler(retryHandler);
		// agent Client connection
		client.getParams()
				.setParameter(
						CoreProtocolPNames.USER_AGENT,
						"Mozilla/5.0 (X11; Linux i686) AppleWebKit/534.30 (KHTML, like Gecko) Ubuntu/11.04 Chromium/12.0.742.112 Chrome/12.0.742.112 Safari/534.30;CharSet=UTF-8");
		// encoding UTF-8
		client.getParams().setParameter(
				CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
		HttpGet request = new HttpGet(url);

		HttpResponse response = client.execute(request);
		if (response != null)
			Log.d(TAG, "response HttpResponse not null ");

		return response.getEntity().getContent();
	}

	public synchronized static HttpURLConnection connect(String urlRequest)
			throws IOException {
		HttpURLConnection con = null;

		try {
			url = new URL(urlRequest);
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(10000);
			con.setConnectTimeout(15000);
			con.setRequestMethod("GET");
			con.connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return con;

	}

	/**
	 * <p>
	 * Convert Stream in UTF8
	 * </p>
	 * 
	 * @return BufferedReader converted in UTF-8
	 * @param con
	 * @return
	 */
	public synchronized static InputStreamReader convertToUtf8(
			HttpURLConnection con) {
		try {
			if (con != null)
				return new InputStreamReader(con.getInputStream(), "UTF-8");

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Connect to sudoc Records
	 * 
	 * @param urlRequest
	 * @return
	 */
	public synchronized static InputStreamReader connectRecords(
			String urlRequest) {
		HttpURLConnection con = null;
		URL url;
		try {
			url = new URL(urlRequest);
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(10000);
			con.setConnectTimeout(15000);
			con.setRequestMethod("GET");

			// Special Parameter.
			con.setRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (X11; Linux i686) AppleWebKit/534.30 (KHTML, like Gecko) Ubuntu/11.04 Chromium/12.0.742.112 Chrome/12.0.742.112 Safari/534.30;CharSet=UTF-8");
			con.setRequestProperty("Content-Type",
					"application/xml;charset=UTF-8");

			con.connect();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertToUtf8(con);

	}

}
