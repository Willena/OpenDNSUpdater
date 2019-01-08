package fr.guillaumevillena.opendnsupdater.activity.introSlide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import fr.guillaumevillena.opendnsupdater.R;

public class IntroMainActionAccountCreateFragment extends Fragment {

    public static IntroMainActionAccountCreateFragment newInstance() {
        return new IntroMainActionAccountCreateFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.intro_credentials_create_account_fragment, container, false);


        WebView wv = root.findViewById(R.id.webview);
        wv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wv.loadUrl("https://signup.opendns.com/homefree/");

        return root;
    }
}