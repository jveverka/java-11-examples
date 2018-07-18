package itx.examples.jetty.client.http20;

import itx.examples.jetty.client.GetListener;
import itx.examples.jetty.client.HttpAccessException;
import itx.examples.jetty.client.RestClient20;
import itx.examples.jetty.common.dto.SystemInfo;
import itx.examples.jetty.common.services.SystemInfoService;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.http.MetaData;
import org.eclipse.jetty.http2.api.Session;
import org.eclipse.jetty.http2.frames.HeadersFrame;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.FuturePromise;

import java.security.KeyStore;

public class SystemInfoServiceClient extends RestClient20 implements SystemInfoService {

    private final HttpURI getSystemInfoURI;

    public SystemInfoServiceClient(String host, int port, KeyStore keyStore, String keyStorePassword) {
        super(host, port, keyStore, keyStorePassword);
        this.getSystemInfoURI = new HttpURI("https://" + host + ":" + port + "/data/system/info");
    }

    @Override
    public SystemInfo getSystemInfo() {
        try {
            Session session = createSession();
            HttpFields requestFields = new HttpFields();
            requestFields.put(USER_AGENT, USER_AGENT_VERSION);
            MetaData.Request request = new MetaData.Request("GET", getSystemInfoURI, HttpVersion.HTTP_2, requestFields);
            HeadersFrame headersFrame = new HeadersFrame(request, null, true);
            GetListener getListener = new GetListener();
            session.newStream(headersFrame, new FuturePromise<>(), getListener);
            SystemInfo response = getListener.get(SystemInfo.class);
            session.close(0, null, new Callback() {});
            return response;
        } catch (Exception e) {
            throw new HttpAccessException(e);
        }
    }

}
