package biz.mosil.sample.evernote.workshop;

import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.type.Note;
import com.evernote.thrift.transport.TTransportException;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class CreateNoteActivity extends ParentActivity {

    private EditText mEdtName, mEdtContent;
    private Button mBtnOk;
    
    private String mNotebookGuid;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_create_note);
        
        mNotebookGuid = getIntent().getStringExtra(INTENT_GUID);
        if(mNotebookGuid == null) {
            //TODO: 沒有傳 Notebook Guid 過來的狀況
        } else {
            mEdtName = (EditText) findViewById(R.id.edt_create_note_name);
            mEdtContent = (EditText) findViewById(R.id.edt_create_note_content);
            mBtnOk = (Button) findViewById(R.id.btn_create_note);
            mBtnOk.setOnClickListener(createNoteListener);
        }
    }
    
    private OnClickListener createNoteListener = new OnClickListener() {
        
        @Override
        public void onClick(View v) {
            final String title = mEdtName.getText().toString().trim();
            final String content = mEdtContent.getText().toString().trim();
            
            Builder dialog = new Builder(CreateNoteActivity.this);
            dialog.setIcon(android.R.drawable.ic_dialog_alert);
            
            if(title.equals("") || content.equals("")) {
                dialog.setTitle("資料不完整");
                if(title.equals("")) {
                    dialog.setMessage("請輸入 Note Title");
                }
                if(content.equals("")) {
                    dialog.setMessage("請輸入 Note Content");
                }
                dialog.setNegativeButton("確定", new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            } else {
                dialog.setTitle("新增 Notebook");
                dialog.setMessage("確定新增");
                dialog.setPositiveButton("新增", new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        setProgressBarIndeterminateVisibility(true);
                        Note note = new Note();
                        note.setNotebookGuid(mNotebookGuid);
                        note.setTitle(title);
                        String noteContent = EvernoteUtil.NOTE_PREFIX + content + EvernoteUtil.NOTE_SUFFIX;
                        note.setContent(noteContent);
                        
                        try {
                            mEvernoteSession.getClientFactory().createNoteStoreClient()
                                .createNote(note, new OnClientCallback<Note>() {
                                    
                                    @Override
                                    public void onSuccess(Note data) {
                                        finish();
                                    }
                                    
                                    @Override
                                    public void onException(Exception exception) {
                                        exception.printStackTrace();
                                    }
                                });
                        } catch (TTransportException e) {
                            e.printStackTrace();
                        }
                    };
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        
                    }
                });
            }
            dialog.show();
        }
    };
}
