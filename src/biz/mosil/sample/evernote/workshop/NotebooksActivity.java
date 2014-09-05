package biz.mosil.sample.evernote.workshop;

import java.util.ArrayList;
import java.util.List;

import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.transport.TTransportException;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

public class NotebooksActivity extends ParentActivity {
    private ListView mLsvNotebooks;
    private ProgressBar mPgbLoading;
    
    private boolean mIsCreateMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebooks);

        mLsvNotebooks = (ListView) findViewById(R.id.lsv_notes);
        mPgbLoading = (ProgressBar) findViewById(R.id.pgb_loading);
        
        mIsCreateMode = getIntent().getBooleanExtra(INTENT_CREATE_MODE, false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotebooks();
    }

    private void getNotebooks() {
        mPgbLoading.setVisibility(View.VISIBLE);
        try {
            mEvernoteSession.getClientFactory().createNoteStoreClient()
                    .listNotebooks(new OnClientCallback<List<Notebook>>() {

                        @Override
                        public void onSuccess(final List<Notebook> data) {
                            mPgbLoading.setVisibility(View.GONE);
                            
                            List<String> notebooks = new ArrayList<String>();
                            
                            for(int i = 0; i < data.size(); i++) {
                                notebooks.add(data.get(i).getName());
                            }
                            
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                    getApplicationContext(),
                                    R.layout.listitem_text,
                                    R.id.txt_note,
                                    notebooks);
                            mLsvNotebooks.setAdapter(adapter);
                            mLsvNotebooks.setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent,
                                        View view, int position, long id) {
                                    Intent intent = new Intent(getApplicationContext(), NotesActivity.class);
                                    intent.putExtra(INTENT_GUID, data.get(position).getGuid());
                                    intent.putExtra(INTENT_NAME, data.get(position).getName());
                                    if(mIsCreateMode) {
                                        intent.putExtra(INTENT_CREATE_MODE, true);
                                    }
                                    startActivity(intent);
                                }
                            });
                        }

                        @Override
                        public void onException(Exception exception) {
                            mPgbLoading.setVisibility(View.GONE);
                            exception.printStackTrace();
                        }

                    });
        } catch (TTransportException e) {
            mPgbLoading.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mIsCreateMode) {
            getMenuInflater().inflate(R.menu.notebooks, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }
    
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case  R.id.menu_action_new:
                Intent intent = new Intent(this, CreateNotebookActivity.class);
                startActivity(intent);
//              startActivityForResult(intent, REQUEST_CREATE_NOTEBOOK);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
