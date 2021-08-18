package top.kwseeker.spring.config.introduction;

import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * FactoryBean测试
 */
public class OkHttpClientFactory implements FactoryBean<OkHttpClient>, DisposableBean {

    //这些应该在配置文件中配置, 不过在这里不是重点，直接写死
    private String host = "";
    private int port = 0;
    private String username = "";
    private String password = "";

    private int connectTimeout = 2000;
    private int readTimeout = 2000;
    private int writeTimeout = 2000;

    private OkHttpClient client;

    @Override
    public OkHttpClient getObject() {
        ConnectionPool pool = new ConnectionPool(5, 10, TimeUnit.SECONDS);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                .connectionPool(pool);

        if (StringUtils.isNotBlank(host) && port > 0) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
            builder.proxy(proxy);
        }

        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            Authenticator proxyAuthenticator = new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    String credential = Credentials.basic(username, password);
                    return response.request().newBuilder()
                            .header("Proxy-Authorization", credential)
                            .build();
                }
            };
            builder.proxyAuthenticator(proxyAuthenticator);
        }

        client = builder.build();
        return client;
    }

    @Override
    public Class<?> getObjectType() {
        Type[] types = this.getClass().getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Class<?> clazz = (Class<?>) parameterizedType.getRawType();
                if (clazz.equals(FactoryBean.class)) {
                    return (Class<?>) parameterizedType.getActualTypeArguments()[0];
                }
            }
        }
        return OkHttpClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void destroy() throws Exception {
        if (client != null) {
            client.connectionPool().evictAll();
            client.dispatcher().executorService().shutdown();
            client.cache().close();
            client = null;
        }
    }
}
