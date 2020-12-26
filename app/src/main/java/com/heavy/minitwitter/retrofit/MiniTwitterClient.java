package com.heavy.minitwitter.retrofit;

import android.util.Log;

import com.heavy.minitwitter.common.Constants;

import org.conscrypt.Conscrypt;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MiniTwitterClient {

    private static MiniTwitterClient obInstance = null;
    private static MiniTwitterService miniTwitterService;
    private Retrofit retrofit;

    public MiniTwitterClient(){

        Security.insertProviderAt(Conscrypt.newProvider(), 1);

        /*
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(loggingInterceptor)
                .retryOnConnectionFailure(true)
                .build();*/
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_MINITWITTER_URL_BASE)
                .client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        miniTwitterService = retrofit.create(MiniTwitterService.class);
    }

    public static MiniTwitterClient getInstance(){
        if(obInstance == null){
            obInstance = new MiniTwitterClient();
        }
        return obInstance;
    }

    public MiniTwitterService getMiniTwitterService(){
        return miniTwitterService;
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
            //SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[] { trustManager }, null);
            //SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


            ///Aumento esto paa ver logging del response
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //fin

            /* Asi esta antes */
            //OkHttpClient.Builder builder = new OkHttpClient.Builder();

            //Mofique yo
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                    .addInterceptor(loggingInterceptor)
                    .retryOnConnectionFailure(true);
            //Fin

            builder.sslSocketFactory(sslSocketFactory, trustManager);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
