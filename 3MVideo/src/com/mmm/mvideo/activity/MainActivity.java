package com.mmm.mvideo.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;

import com.mmm.mvideo.R;
import com.mmm.mvideo.widget.ListPreferenceMultiSelect;

// TODO: Auto-generated Javadoc
/**
 * The Class MainActivity.
 * @author a37wczz
 */
public class MainActivity extends PreferenceActivity {
	Dialog dialog;
    /*
     * (non-Javadoc)
     * 
     * @see com.mmm.mvideo.activity.MMMActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        XmlResourceParser xmlResourceParser =  getResources().getXml(R.xml.plist);
//        MMMPlayListXmlParser playListParser = new MMMPlayListXmlParser(xmlResourceParser);
////        playListParser.doParse();
//        List<MMMVideoItem> playList = SimulateService.getInstance().getAllVideoGroups();
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                Intent intent = new Intent(context, HomeActivity.class);
//                context.startActivity(intent);
//                context.finish();
//            }
//
//        }, 1000);
        addPreferencesFromResource(R.xml.preferences);
        dialog=new Dialog(this);
		//自己写的用于执行相应操作的类
        ListPreferenceMultiSelect pre=(ListPreferenceMultiSelect)findPreference("mvideo.tabs");
		pre.setEntries(new CharSequence[]{"Home","IMB","IBM"});
		pre.setEntryValues(new CharSequence[]{"Home","IMB","IBM"});

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}
