package biz.mosil.sample.evernote.workshop;

import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.transport.TTransportException;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CreateNotebookActivity extends ParentActivity {
    
    private EditText mEdtName;
    private Button mBtnOk;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_create_notebook);
        
        mEdtName = (EditText) findViewById(R.id.edt_create_notebook_name);
        mBtnOk = (Button) findViewById(R.id.btn_create_notebook);
        mBtnOk.setOnClickListener(createNotebookListener);
        
    }
    
    private OnClickListener createNotebookListener = new OnClickListener() {
        
        @Override
        public void onClick(View v) {
            final String name = mEdtName.getText().toString().trim();
            
            Builder dialog = new Builder(CreateNotebookActivity.this);
            dialog.setIcon(android.R.drawable.ic_dialog_alert);
            
            if(name.equals("")) {
                dialog.setTitle("資料不完整");
                dialog.setMessage("請輸入 Notebook Name");
                dialog.setNegativeButton("確定", new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        
                    }
                });
            } else {
                dialog.setTitle("新增 Notebook");
                dialog.setMessage("確定新增");
                dialog.setPositiveButton("新增", new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        setProgressBarIndeterminateVisibility(true);
                        Notebook notebook = new Notebook();
                        notebook.setName(name);
                        try {
                            mEvernoteSession.getClientFactory().createNoteStoreClient()
                                .createNotebook(notebook, new OnClientCallback<Notebook>() {

                                    @Override
                                    public void onSuccess(Notebook data) {
                                        finish();
                                    }

                                    @Override
                                    public void onException(Exception exception) {
                                    }
                                });
                        } catch (TTransportException e) {
                            e.printStackTrace();
                        }
                    }
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
