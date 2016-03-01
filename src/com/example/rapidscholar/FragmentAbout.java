package com.example.rapidscholar;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class FragmentAbout extends Fragment {

		WebView about;
		String url = "http://dining.queensu.ca/the-lazy-scholar/";

		public FragmentAbout() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View view = inflater.inflate(R.layout.fragment_layout_myfunds, container,
					false);
	
			about = (WebView) view.findViewById(R.id.webview);
			about.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
			about.loadUrl(url);
			
			return view;
		}
	}
