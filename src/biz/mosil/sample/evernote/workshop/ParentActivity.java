package biz.mosil.sample.evernote.workshop;

import com.evernote.client.android.EvernoteSession;
import android.app.Activity;
import android.os.Bundle;

public class ParentActivity extends Activity {

    private static final String CONSUMER_KEY = "wyfisgood";
    private static final String CONSUMER_SECRET = "9452ac6666f87b1c";
    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;

    // Set this to true if you want to allow linked notebooks for accounts that can only access a single
    // notebook.
    private static final boolean SUPPORT_APP_LINKED_NOTEBOOKS = true;
    
    protected EvernoteSession mEvernoteSession;
    
    protected static final String INTENT_GUID = "guid";
    protected static final String INTENT_NAME = "name";
    protected static final String INTENT_CREATE_MODE = "create_mode";
    
    protected static final int REQUEST_CREATE_NOTEBOOK = 101;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mEvernoteSession = EvernoteSession.getInstance(this, CONSUMER_KEY, CONSUMER_SECRET, EVERNOTE_SERVICE, SUPPORT_APP_LINKED_NOTEBOOKS);
    }

}
