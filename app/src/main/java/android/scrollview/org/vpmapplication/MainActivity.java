package android.scrollview.org.vpmapplication;

import android.content.Intent;
import android.os.Bundle;
import android.security.KeyChain;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/***
 * .p12证书安装可以采用在webView中加载assets中证书的路径
 * .der证书需要KeyChain.createInstallIntent启动安装Intent
 */
public class MainActivity extends AppCompatActivity {
    private static int REQUST_CODE = 1;
    private static int RESULT_CODE = 2;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);
        Constant.isInstall();
        if (!Constant.isLoadCaer) {
            try {
                // 加载本地证书
                LoadCar();
            } catch (CertificateException e) {
                e.printStackTrace();
            }
        }

    }

    public void LoadCar() throws CertificateException {
        // assets目录下的.der文件路径
        String fileName = "www/strongswanCert.der";
        String NAME = "StrongsWanCert";
        CertificateFactory certFactory = null;
        InputStream inStream = null;
        try {
            certFactory = CertificateFactory.getInstance("X.509");
            inStream = getResources().getAssets().open(fileName);
            X509Certificate cer = (X509Certificate) certFactory.generateCertificate(inStream);
            String name = cer.getSigAlgName();
            Log.i("TAG", "name->" + name);
            Intent installIntent = KeyChain.createInstallIntent();
            installIntent.putExtra(KeyChain.EXTRA_CERTIFICATE, cer.getEncoded());
            installIntent.putExtra(KeyChain.EXTRA_NAME, NAME);
            startActivityForResult(installIntent, REQUST_CODE);
            inStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                textView.setText("安装成功");
                Constant.isLoadCaer = true;
            }
        }
    }
}
