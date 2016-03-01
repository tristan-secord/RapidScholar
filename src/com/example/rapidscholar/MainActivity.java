package com.example.rapidscholar;


import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import android.os.Bundle;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AlphabetIndexer;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.widget.ArrayAdapter;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;

public class MainActivity extends Activity {
	
	private ListView mListView;
	private Cursor mCursor;
	
	Calendar c = Calendar.getInstance();
	
	GlobalPrice cost;
	DBAdapter myDB;
	ArrayAdapter<Food> myadapter;
	ArrayAdapter<Food> mealoptions;
	double MEAL_EQUIVALENT = 8.75;
	
	int makeMeal;
	
	private List<Food> myFood = new ArrayList<Food>();
	private List<Food> myOptions = new ArrayList<Food>();
	
	
	//Navigation Drawer vars
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//startActivity(Splash);
		
		makeMeal = 0;
		
		mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
        
		openDB();
		openAdapters();
		initializePrice();
		clearAll();
		DBinitialize();
		foodObjectInitialize();
		//populateListViewFromDB();
		initializeListView();
		registerClickCallback();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
	
    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        case R.id.action_websearch:
            // create intent to perform web search for this planet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    
    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //selectItem(position);
        Fragment fragment = null;
        Bundle args = new Bundle();
        switch(position) {
	        case 0:
	        	Intent intent = new Intent (MainActivity.this, SplashScreen.class);
	        	startActivity(intent);
	        	mDrawerLayout.closeDrawer(mDrawerList);
	        	setTitle("New Meal");
				break;
			case 2:
				fragment = new FragmentAddMyFunds();
				FragmentManager frgManager = getFragmentManager();
				frgManager.beginTransaction().replace(R.id.content_frame, fragment)
						.commit();
		        mDrawerLayout.closeDrawer(mDrawerList);
		        Intent intent2 = new Intent (MainActivity.this, SplashScreen.class);
		        finish();
		        startActivity(intent2);
		        setTitle("New Meal");
				break;
			case 1:
				fragment = new FragmentCheckBalance();
				args.putString(FragmentCheckBalance.TITLE, "Your Account Summary");
				fragment.setArguments(args);
				FragmentManager frgManager2 = getFragmentManager();
				frgManager2.beginTransaction().replace(R.id.content_frame, fragment)
						.commit();
		        mDrawerLayout.closeDrawer(mDrawerList);
		        setTitle("My Account");
		        break;
			case 4:
				fragment = new FragmentAbout();
				FragmentManager frgManager3 = getFragmentManager();
				frgManager3.beginTransaction().replace(R.id.content_frame, fragment)
						.commit();
		        mDrawerLayout.closeDrawer(mDrawerList);
		        setTitle("About The Lazy Scholar");
		        break;
			case 3:
				Intent intent3 = new Intent (MainActivity.this, ScannerFragment.class);
				startActivity(intent3);
				mDrawerLayout.closeDrawer(mDrawerList);
				setTitle("Scan Student Card");
				break;
				
		}
    }
}

    private void selectItem(int position) {

        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeDB();
	}

	private void initializePrice() {
		cost = new GlobalPrice();
		cost.clearPrice();
		updateCostView();
	}
	private void openDB(){
		myDB = new DBAdapter(this);
		myDB.open();
	}
	 
	private void openAdapters(){
		myadapter = new MyListAdapter();
		mealoptions = new MealListOptionsAdapter();
	}
	
	private void closeDB(){
		myDB.close();
	}
	
	public void clearAll() {
		makeMeal = 0;
		myDB.deleteAll();
		myadapter.clear();
		
	}
	
	public void foodObjectInitialize() {
		Cursor cursor = myDB.getAllRows();
		startManagingCursor(cursor);
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false){
			myOptions.add(new Food(cursor.getString(DBAdapter.COL_NAME), cursor.getDouble(DBAdapter.COL_PRICE)));
			cursor.moveToNext();
		}
	}
	
	public void onClick_ClearAll(View v) {
		makeMeal = 0;
		myadapter.clear();
		cost.clearPrice();
		updateCostView();
		foodObjectInitialize();
		initializeListView();
		}
	
	public void onClick_Pay(View v) {
		Bundle args = new Bundle();
		Fragment fragment = new FragmentCheckBalance();
		args.putString(FragmentCheckBalance.TITLE, "After your purchase...");
		fragment.setArguments(args);
		FragmentManager frgManager2 = getFragmentManager();
		frgManager2.beginTransaction().replace(R.id.content_frame, fragment)
				.commit();
	}
	
	
	public void onClick_MakeItAMeal(View v) {
		makeMeal = 1;
		myOptions.clear();
		double amount_left = (MEAL_EQUIVALENT - (cost.getPrice() % MEAL_EQUIVALENT));
		
		if (cost.getPrice() == MEAL_EQUIVALENT)
			amount_left = 0;
		
		BigDecimal bd = new BigDecimal(amount_left);
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		int meals = (int)(cost.getPrice()/MEAL_EQUIVALENT);
		
		if (amount_left < 0.45)
			Toast.makeText(this, "You are at " + (meals + 1) + " meal(s)", Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(this, "You are $" + bd + " away from making " + (meals + 1) + " meal(s)", Toast.LENGTH_SHORT).show();
		
		Cursor cursor = myDB.getAllRows();
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false){
			if (cursor.getDouble(DBAdapter.COL_PRICE) <= amount_left) {
				myOptions.add(new Food(cursor.getString(DBAdapter.COL_NAME), cursor.getDouble(DBAdapter.COL_PRICE)));
			}
			cursor.moveToNext();
		}
		
		ListView mealoptionView = (ListView) findViewById(R.id.listView1);
		mealoptionView.setAdapter(mealoptions);
	}
	
	private void DBinitialize() {
		myDB.insertRow ("Egg", 1.25);
		myDB.insertRow ("Eggs (2)", 1.99);
		myDB.insertRow ("Four Chicken fingers & fries", 8.75);
		myDB.insertRow ("Acapulco Chicken Wrap", 6.50);
		myDB.insertRow ("Antipasto salad", 6.50);
		myDB.insertRow ("Cheeseburger", 5.85);
		myDB.insertRow ("Greek Chicken Wrap", 6.50);
		myDB.insertRow ("Scholar Club Wrap", 6.50);
		myDB.insertRow ("Ham, brie, & honey-mustard Wrap", 6.50);
		myDB.insertRow ("Taco Salad Wrap", 6.50);
		myDB.insertRow ("Hearty beefeater Wrap", 6.50);
		myDB.insertRow ("Tuna w sundried tomato & olive", 6.50);
		myDB.insertRow ("Southwest vegetarian", 6.50);
		myDB.insertRow ("Maple glazed turkey & swiss burger", 6.50);
		myDB.insertRow ("Lazy crispy onion Pepperjack groundbeef burger", 6.85);
		myDB.insertRow ("HoneyBBQ chicken sandwich", 7.25);
		myDB.insertRow ("Herbed grilled salmon w caper mayo", 7.25);
		myDB.insertRow ("French-onion steak & Provolone sandwich", 6.75);
		myDB.insertRow ("Bacon lovers cheddar groundbeef burger", 7.25);
		myDB.insertRow ("Lazy chicken snackers", 5.60);
		myDB.insertRow ("Fish & Chips", 6.99);
		myDB.insertRow ("Lazy Riser", 3.55);
		myDB.insertRow ("Hamburger", 5.30);
		myDB.insertRow ("Poutine", 5.15);
		myDB.insertRow ("Large Fries - curly or French", 3.49);
		myDB.insertRow ("Reg Fries - curly or French", 2.88);
		myDB.insertRow ("Onion rings", 3.25);
		myDB.insertRow ("Lazy breakfast special", 5.25);
		myDB.insertRow ("Western omlet", 4.99);
		myDB.insertRow ("Western sandwich", 3.99);
		myDB.insertRow ("Cheddar cheese omlet", 4.75);
		myDB.insertRow ("The bacon lovers", 5.25);
		myDB.insertRow ("Lazy riser", 3.55);
		myDB.insertRow ("Lazy grilled cheese sandwich", 3.30);
		myDB.insertRow ("Fried egg sandwich", 2.60);
		myDB.insertRow ("Pepperoni Pizza", 4.20);
		myDB.insertRow ("Cheese Pizza", 4.20);
		myDB.insertRow ("Feature Pizza", 4.50);
		myDB.insertRow ("Milk", 2.60);
		myDB.insertRow ("Cereal", 2.19);
		myDB.insertRow ("Cereal w Milk", 3.25);
		myDB.insertRow ("Coke 592 mL", 2.25);
		myDB.insertRow ("Vitamin Water", 3.25);
		myDB.insertRow ("Lactose Free Milk", 3.75);
		myDB.insertRow ("Milk/Chocolate 1L", 3.05);
		myDB.insertRow ("Cheese", 0.99);
		myDB.insertRow ("Salad Dressing", 0.45);
		myDB.insertRow ("Sandwich", 8.75);
		myDB.insertRow ("Yogurt", 1.99);
		myDB.insertRow ("Protein Shake", 2.40);
		myDB.insertRow ("Salad", 6.50);
		myDB.insertRow ("Orange Juice", 3.95);
		myDB.insertRow ("Chips", 4.75);
		myDB.insertRow ("Fruit", 1.25);
		myDB.insertRow ("Nuts", 1.75);
		myDB.insertRow ("Pasta", 6.50);
		myDB.insertRow ("Pasta Combo", 8.75);
		myDB.insertRow ("Dasani Drops", 4.95);
		myDB.insertRow ("Oreo Cookies", 0.75);
		myDB.insertRow ("Hot Rod Sausage", 1.50);
		myDB.insertRow ("Sunflower Seeds", 4.25);
		myDB.insertRow ("Quaker Granola Bars", 1.29);
		myDB.insertRow ("Peanut butter/Jam/Honey", 0.45);
		myDB.insertRow ("Latte Small", 3.40);
		myDB.insertRow ("Latte Large", 4.55);
		myDB.insertRow ("Cappuccino Small", 3.20);
		myDB.insertRow ("Cappuccino Large", 4.35);
		myDB.insertRow ("Americano Small", 2.65);
		myDB.insertRow ("Americano Large", 3.60);
		myDB.insertRow ("Caramel Machiato Small", 3.90);
		myDB.insertRow ("Caramel Machiato Large", 5.10);
		myDB.insertRow ("Chai latte Small", 3.90);
		myDB.insertRow ("Chai latte Large", 5.10);
		myDB.insertRow ("London fog Small", 3.30);
		myDB.insertRow ("London fog Large", 4.45);
		myDB.insertRow ("Syrup Creme Small", 3.15);
		myDB.insertRow ("Syrup Creme Large", 3.50);
		myDB.insertRow ("Single Espresso", 2.05);
		myDB.insertRow ("Double Espresso", 2.50);
		myDB.insertRow ("Flavour Shot", 0.95);


		int day = c.get(Calendar.DAY_OF_WEEK);
		if (day == 2)
		{
			myDB.insertRow ("SPECIAL\nHerb roast leg of lamb", 6.50);
			myDB.insertRow ("SPECIAL\nHummus & tabbouleh wrap", 6.50);
			myDB.insertRow ("SPECIAL\nGrilled herbed pork loin", 8.75);
		}
		if (day == 3)
		{
			myDB.insertRow ("SPECIAL\nCalifornia Turkey", 6.50);
			myDB.insertRow ("SPECIAL\nTuscan vege wrap", 6.50);
			myDB.insertRow ("SPECIAL\nItialian meatball sandwich", 8.75);
		}
		if (day == 4)
		{
			myDB.insertRow ("SPECIAL\nCuban panini sandwich", 6.50);
			myDB.insertRow ("SPECIAL\nMediterranean vege wrap", 6.50);
			myDB.insertRow ("SPECIAL\nBuffalo chicken meltdown", 8.75);
		}
		if (day == 5)
		{
			myDB.insertRow ("SPECIAL\nClassic Nicoise wrap", 6.50);
			myDB.insertRow ("SPECIAL\nTandoori vege wrap", 6.50);
			myDB.insertRow ("SPECIAL\nStrip loin steak", 8.75);
		}
		if (day == 6)
		{
			myDB.insertRow ("SPECIAL\nChicken Caesar wrap", 6.50);
			myDB.insertRow ("SPECIAL\nGrilled portabello mushroom caesar wrap", 6.50);
			myDB.insertRow ("SPECIAL\nMediterranean lamb & Feta burger", 8.75);
		}
		if (day == 7)
		{
			myDB.insertRow ("SPECIAL\nSouthwest beef wrap", 6.50);
			myDB.insertRow ("SPECIAL\nRoasted red pepper & hummus wrap", 6.50);
			myDB.insertRow ("SPECIAL\nChef's choice", 8.75);
		}
		if (day == 1)
		{
			myDB.insertRow ("SPECIAL\nPopcorn chicken wrap", 6.50);
			myDB.insertRow ("SPECIAL\nMexican vege wrap", 6.50);
			myDB.insertRow ("SPECIAL\nChef's choice", 8.75);

		}

	}
	
	private void initializeListView() {
		mListView = (ListView) findViewById(R.id.listView1);
		mListView.setFastScrollEnabled(true);
		mCursor = myDB.getAllRows();
		
		MyCursorAdapter curAdap = new MyCursorAdapter(this,
				R.layout.menu,
				mCursor,
				new String[]{myDB.KEY_NAME, myDB.KEY_PRICE},
				new int[]{R.id.textView1, R.id.textView2});
		mListView.setAdapter(curAdap);
	}
	
	private void registerClickCallback() {
		ListView mealoptionView = (ListView) findViewById (R.id.listView1);
		mealoptionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
				
				Food clickedfood = myOptions.get(position);
				UpdatePurchasedFood(clickedfood.getFood(), clickedfood.getPrice());
				IncreasePrice(clickedfood.getPrice());
				if (makeMeal == 1)
				{
					View v = getWindow().getDecorView().getRootView();
					onClick_MakeItAMeal(v);
				}		
			}
		});	
	}
	
	private ActionMode.Callback modeCallBack = new ActionMode.Callback() {
		
		@Override
		public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {

			return false;
		}
		
		@Override
		public void onDestroyActionMode(ActionMode arg0) {

			arg0 = null;
		}
		
		@Override
		public boolean onCreateActionMode(ActionMode arg0, Menu arg1) {
			arg0.getMenuInflater().inflate(R.menu.removemenu, arg1);
			return true;
		}
		
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			{
				int position = ((MyListAdapter) myadapter).getSelection();
				subtractPrice(position);
				myadapter.remove(myadapter.getItem(position));
				View v = getWindow().getDecorView().getRootView();
				onClick_MakeItAMeal(v);
				mode.finish();
			}
			
			return false;
		}
	};
	
	public void subtractPrice(int position) {
		cost.subPrice((myadapter.getItem(position)).getPrice());
		updateCostView();
	}
	
	private void IncreasePrice(double price) {
		
		cost.addPrice(price);
		updateCostView();
		
	}
	
	private void UpdatePurchasedFood(String foodName, double foodPrice) {
		
		myFood.add(new Food(foodName, foodPrice));
		
		ListView myList = (ListView) findViewById(R.id.listView2);
		myList.setAdapter(myadapter);
		
		myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick (AdapterView parent, View view, int position, long id) {
				startActionMode(modeCallBack);
				view.setSelected(true);
				((MyListAdapter) myadapter).setSelection(position);
				return true;
			}
		});
		
	}
	
	
	
	private void updateCostView() {
		Button price = (Button) findViewById(R.id.cost);
		if (cost.getPrice() == 0)
			price.setText("Cost");
		else { 
			BigDecimal bd = new BigDecimal (cost.getPrice());
			bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
			price.setText("$" + bd);
		}
	}
	
	private class MyListAdapter extends ArrayAdapter<Food> {
		
		public MyListAdapter () {
			super(MainActivity.this, R.layout.added_food, myFood);
		}
		
		int currentSelection;
		
		public void setSelection(int position) {
			currentSelection = position;
		}
		
		public int getSelection() {
			return currentSelection;
		}
		
		@Override
		public View getView (int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.added_food, parent, false);
			}
			
			Food currentFood = myFood.get(position);
			
			TextView foodText = (TextView) itemView.findViewById(R.id.textView1);
			foodText.setText(currentFood.getFood());
			
			TextView priceText = (TextView) itemView.findViewById(R.id.textView2);
			BigDecimal bd = new BigDecimal(currentFood.getPrice());
			bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
			priceText.setText("$" + bd);
			
			return itemView;
			
		}
	}
	
	private class MealListOptionsAdapter extends ArrayAdapter<Food> {
		public MealListOptionsAdapter () {
			super(MainActivity.this, R.layout.menu, myOptions);
		}
		
		@Override
		public View getView (int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.menu, parent, false);
			}
			
			Food currentFood = myOptions.get(position);
			
			TextView foodText = (TextView) itemView.findViewById(R.id.textView1);
			foodText.setText(currentFood.getFood());
			
			TextView priceText = (TextView) itemView.findViewById(R.id.textView2);
			BigDecimal bd = new BigDecimal(currentFood.getPrice());
			bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
			priceText.setText("$" + bd);
			
			return itemView;
			
		}
	}
	
	public class MyCursorAdapter extends CursorAdapter  implements SectionIndexer
	{

	    AlphabetIndexer mAlphabetIndexer;

	    public MyCursorAdapter(Context context, int simpleListItem1,
	            Cursor cursor, String[] strings, int[] is)
	    {
	        super(context, cursor);

	        mAlphabetIndexer = new AlphabetIndexer(cursor,
	                cursor.getColumnIndex("name"),
	                " ABCDEFGHIJKLMNOPQRTSUVWXYZ");
	        mAlphabetIndexer.setCursor(cursor);//Sets a new cursor as the data set and resets the cache of indices.

	    }

	    /**
	     * Performs a binary search or cache lookup to find the first row that matches a given section's starting letter.
	     */
	    @Override
	    public int getPositionForSection(int sectionIndex)
	    {
	        return mAlphabetIndexer.getPositionForSection(sectionIndex);
	    }

	    /**
	     * Returns the section index for a given position in the list by querying the item and comparing it with all items
	     * in the section array.
	     */
	    @Override
	    public int getSectionForPosition(int position)
	    {
	        return mAlphabetIndexer.getSectionForPosition(position);
	    }

	    /**
	     * Returns the section array constructed from the alphabet provided in the constructor.
	     */
	    @Override
	    public Object[] getSections()
	    {
	        return mAlphabetIndexer.getSections();
	    }

	    /**
	     * Bind an existing view to the data pointed to by cursor
	     */
	    @Override
	    public void bindView(View view, Context context, Cursor cursor)
	    {
			BigDecimal bd = new BigDecimal(cursor.getString(cursor.getColumnIndex("price")));
			bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
			
	        TextView txtView = (TextView)view.findViewById(R.id.textView1);
	        TextView txtView2 = (TextView)view.findViewById(R.id.textView2);
	        txtView.setText(cursor.getString(
	                cursor.getColumnIndex("name")));
	        txtView2.setText("$" + bd);
	    }

	    /**
	     * Makes a new view to hold the data pointed to by cursor.
	     */
	    @Override
	    public View newView(Context context, Cursor cursor, ViewGroup parent)
	    {
	        LayoutInflater inflater = LayoutInflater.from(context);
	        View newView = inflater.inflate(
	                R.layout.menu, parent, false);
	        return newView;
	    }
	}
	
/*	public class Splash extends Activity {
		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			setContentView(R.layout.splash);
			
			Thread splashThread = new Thread() {
				@Override
				public void run() {
					try {
						super.run();
						sleep(500);
					} catch (Exception e) {
						
					} finally {
						Intent i = new Intent(Splash.this, MainActivity.class);
						startActivity(i);
						finish();
					}
				}
			};
			splashThread.start();
		}
	}*/
}



