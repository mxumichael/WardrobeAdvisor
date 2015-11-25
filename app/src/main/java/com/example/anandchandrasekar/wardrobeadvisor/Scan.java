package com.example.anandchandrasekar.wardrobeadvisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class Scan extends AppCompatActivity {

    private NfcAdapter mNfcAdapter;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    TextView mTextView;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_view);

        dbHelper=new DBHelper(this);

        nfcTest();

        RadioButton radio_button_dirty = (RadioButton) findViewById(R.id.radio_dirty);
        RadioButton radio_button_clean = (RadioButton) findViewById(R.id.radio_clean);
        RadioButton radio_button_in_wash = (RadioButton) findViewById(R.id.radio_in_wash);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String destination = sharedPref.getString("destination", "dirty");

        if (destination.equals("dirty")) {
            radio_button_dirty.setChecked(true);
        } else if (destination.equals("clean")) {
            radio_button_clean.setChecked(true);
        } else if (destination.equals("in_wash")) {
            radio_button_in_wash.setChecked(true);
        } else {
            Toast.makeText(getApplicationContext(), "un-anticipated destination:" + destination, Toast.LENGTH_LONG).show();
        }


    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_dirty:
                if (checked) {
                    //Toast.makeText(getApplicationContext(), "radio_dirty checked", Toast.LENGTH_SHORT).show();
                    editor.putString("destination", "dirty");
                    editor.commit();
                }
                break;
            case R.id.radio_clean:
                if (checked) {
                    //Toast.makeText(getApplicationContext(), "radio_clean checked", Toast.LENGTH_SHORT).show();
                    editor.putString("destination", "clean");
                    editor.commit();
                }
                break;
            case R.id.radio_in_wash:
                if (checked) {
                    //Toast.makeText(getApplicationContext(), "radio_in_wash checked", Toast.LENGTH_SHORT).show();
                    editor.putString("destination", "in_wash");
                    editor.commit();
                }
                break;
        }
    }

    public void bulk_update_helper(View view, String confirmation_message, final String notification_message, final int original_state, final int new_state ) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage(confirmation_message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int affected = dbHelper.updateItemsFromStateToState(original_state, new_state);
                        Toast.makeText(getApplicationContext(), notification_message + " clothes updated:"+affected, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
    public void button_dirty_to_in_wash_clicked(View view) {
        bulk_update_helper(view, "Do you really want move all Dirty items to In Wash state?", "All Dirty items moved to In Wash", Item.STATE_DIRTY, Item.STATE_INWASH);
    }
    public void button_in_wash_to_clean_clicked(View view) {
        bulk_update_helper(view, "Do you really want move all In Wash items to Clean state?", "All In Wash items moved to Clean", Item.STATE_INWASH, Item.STATE_CLEAN);
    }
    public void button_clean_to_dirty_clicked(View view) {
        bulk_update_helper(view,"Do you really want move all Clean items to Dirty state?","All Clean items moved to Dirty",Item.STATE_CLEAN,Item.STATE_DIRTY);
    }
    private void nfcTest() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        String is_nfc_ok = nfcSupportCheck();
        if (is_nfc_ok.equals("FAIL")) {
            return;
        }
        setContentView(R.layout.scan_view);
        getIntent();

    }

    private String nfcSupportCheck() {

        //copied from http://code.tutsplus.com/tutorials/reading-nfc-tags-with-android--mobile-17278
        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            //finish();
            return "FAIL";
        }

        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC is disabled.", Toast.LENGTH_LONG).show();
            return "FAIL";
        } else {
            //Toast.makeText(this, "NFC is OK.", Toast.LENGTH_SHORT).show();
        }
        return "OK";
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        String is_nfc_ok = nfcSupportCheck();
        if (is_nfc_ok.equals("FAIL")) {
            //do nothing
        } else {
            setupForegroundDispatch(this, mNfcAdapter);
        }
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        String is_nfc_ok = nfcSupportCheck();
        if (is_nfc_ok.equals("FAIL")) {
            //do nothing
        } else {
            setupForegroundDispatch(this, mNfcAdapter);
        }

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        /*this part gets called whenever an NFC is detected. I think this calls the Reader to actually read the information*/
        //Toast.makeText(this, "handleIntent has been called", Toast.LENGTH_LONG).show();

        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                // Log.d(TAG, "Wrong mime type: " + type);
                CharSequence temp = "Wrong mime type: " + type;
                Toast.makeText(this, temp, Toast.LENGTH_LONG).show();

            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }

    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    /**
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), SettingsView.class);
            i.putExtra("option", R.id.action_settings);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void button_done_scanning(View view) {
        //Starting a new Intent
        Intent nextScreen = new Intent(getApplicationContext(), Home.class);
        startActivity(nextScreen);

    }

    /**
     * Background task for reading the data. Do not block the UI thread while reading.
     *
     * @author Ralf Wondratschek
     */
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {
        TextView mTextView;

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(ndefRecord.toString(), "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            mTextView = (TextView) findViewById(R.id.database_call);
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            String destination = sharedPref.getString("destination", "dirty");
            int parseint = Integer.parseInt(result);
            Item item = dbHelper.getItemById((int) parseint);//getting the scanned item;
            int oldStateId = item.getState();
            String oldState = item.getStateName();
            int newStateId = 0;
            String newState = oldState;

            if (result != null) {
                mTextView.setText("Itemid:"+parseint+" name:"+item.getName()+" changed from state:"+item.getStateName()+" to state:" + destination +". waiting for next NFC scan.");
                switch (destination) {
                    case "clean":
                        dbHelper.updateItemState(parseint, Item.STATE_CLEAN);
                        newStateId = Item.STATE_CLEAN;
                        newState = "Clean";
                        break;
                    case "dirty":
                        dbHelper.updateItemState(parseint, Item.STATE_DIRTY);
                        newStateId = Item.STATE_DIRTY;
                        newState = "Dirty";
                        break;
                    case "in_wash":
                        dbHelper.updateItemState(parseint, Item.STATE_INWASH);
                        newStateId = Item.STATE_INWASH;
                        newState = "In Wash";
                        break;
                }
            }


            ImageView img  = (ImageView)findViewById(R.id.item_image_scan);
            int imgId = getResources().getIdentifier(item.getImagePath(), "drawable", getApplicationContext().getPackageName());
            img.setImageResource(imgId);

            LinearLayout scanHome = (LinearLayout)findViewById(R.id.scanHomeImage);
            scanHome.setVisibility(View.GONE);

            LinearLayout scannedView = (LinearLayout)findViewById(R.id.scannedItemView);
            scannedView.setVisibility(View.VISIBLE);

            ImageView scannedItemImage = (ImageView)findViewById(R.id.scannedItemImage);
            scannedItemImage.setImageResource(imgId);

            TextView scannedItemName = (TextView)findViewById(R.id.scannedItemName);
            scannedItemName.setText(item.getName());

            TextView scannedItemState = (TextView)findViewById(R.id.scannedItemWas);
            scannedItemState.setText("Was:    " + oldState);

            scannedItemState = (TextView)findViewById(R.id.scannedItemIs);
            scannedItemState.setText("Now:    " + newState);

            ImageView stateImage = (ImageView)findViewById(R.id.scannedItemStateOld);
            imgId = getResources().getIdentifier(item.getStateImage(oldStateId), "drawable", getPackageName());
            stateImage.setImageResource(imgId);

            stateImage = (ImageView)findViewById(R.id.scannedItemStateNew);
            imgId = getResources().getIdentifier(item.getStateImage(newStateId), "drawable", getPackageName());
            stateImage.setImageResource(imgId);

            TextView tview  = (TextView)findViewById(R.id.database_call);
            tview.setText("Please scan the next item");

            //Button bulk_button = (Button)findViewById(R.id.button_clean_to_dirty);
            //bulk_button.setVisibility(View.GONE);
            //bulk_button = (Button)findViewById(R.id.button_dirty_to_in_wash);
            //bulk_button.setVisibility(View.GONE);
            //bulk_button = (Button)findViewById(R.id.button_in_wash_to_clean);
            //bulk_button.setVisibility(View.GONE);
            //bulk_button = (Button)findViewById(R.id.button_done_scanning);
            //bulk_button.setVisibility(View.VISIBLE);


        }
    }
}
