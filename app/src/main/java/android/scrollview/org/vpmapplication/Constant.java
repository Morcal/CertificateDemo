package android.scrollview.org.vpmapplication;

import android.util.Log;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

/**
 * Created by lyqdhgo on 2016/5/19.
 */
public class Constant {
    public static Boolean isLoadCaer = false;

    public static void isInstall() {
        try {
            KeyStore keyStore = null;
            try {
                keyStore = KeyStore.getInstance("AndroidCAStore");
                keyStore.load(null, null);
                Enumeration aliases = keyStore.aliases();
                while (aliases.hasMoreElements()) {
                    String alias = (String) aliases.nextElement();
                    X509Certificate cert = (X509Certificate) keyStore.getCertificate(alias);

                    boolean isLoad = cert.getIssuerDN().getName().contains("StrongsWanCert");
                    if (isLoad) {
                        Log.i("TAG", "证书已安装了");
                    }
                }
            } catch (KeyStoreException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }

    }
}
