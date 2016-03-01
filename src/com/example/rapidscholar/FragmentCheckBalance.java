package com.example.rapidscholar;

import android.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FragmentCheckBalance extends Fragment {
	
	TextView title;

	public static final String TITLE = "title";
	
	public FragmentCheckBalance() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.user_account, container,
				false);
		
		title = (TextView) view.findViewById(R.id.title);
		
		title.setText(getArguments().getString(TITLE));

		

		return view;
	}
}

