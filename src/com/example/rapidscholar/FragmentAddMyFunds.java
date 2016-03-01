package com.example.rapidscholar;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


public class FragmentAddMyFunds extends Fragment {

	WebView myFunds;
	String url = "https://devsso.housing.queensu.ca/top-ups/";

	public FragmentAddMyFunds() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_main, container,
				false);
		
		try {
			  Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			  startActivity(myIntent);
			} catch (ActivityNotFoundException e) {

			}

		return view;
	}
}

