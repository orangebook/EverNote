package com.liaobb.evernote.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.liaobb.evernote.R;
import com.liaobb.evernote.bean.Note;
import com.liaobb.evernote.bean.NoteType;
import com.liaobb.evernote.common.CacheUtils;
import com.liaobb.evernote.common.HtmlUtils;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import butterknife.InjectView;

public class ReadNoteActivity extends ToolbarActivity {

    @InjectView(R.id.readNote)
    WebView noteRead;

    private int editNoteID;
    private int currentNoteType = 0;
    private Note currentNote;
    private ProgressDialog progressDialog = null;
    private static String[] noteTypeArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_note);

        //得到WebView组件
        noteRead = (WebView) findViewById(R.id.readNote);
        //能够的调用JavaScript代码
        noteRead.getSettings().setJavaScriptEnabled(true);
        //优先使用缓存
        noteRead.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        noteRead.setWebChromeClient(new WebChromeClient());

        WebSettings settings = noteRead.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setSupportZoom(true); // 支持缩放

        Bundle bundle = getIntent().getExtras();
        currentNoteType = bundle.getInt(NoteListFragment.NOTE_INIT_TYPE, 0);
        editNoteID = bundle.getInt(NoteListFragment.EDIT_NOTE_ID, -1);

        Log.d("ReadNoteActivity", "当前NoteType为:" + currentNoteType);
        //获取类型列表
        List<NoteType> noteTypes = DataSupport.order("notetype").find(NoteType.class);
        noteTypeArray = new String[noteTypes.size()];
        for (int i = 0; i < noteTypes.size(); i++) {
            noteTypeArray[i] = noteTypes.get(i).getNoteTypeString();
            Log.d("ReadNoteActivity", noteTypeArray[i]);
        }

        if (editNoteID == -1) {
            currentNote = null;
        } else {

            // currentNote.getNoteType().setNoteTypeString(noteTypeArray[currentNoteType]);
            currentNote = DataSupport.where("noteid=?", String.valueOf(editNoteID)).find(Note.class).get(0);
            AsyncTask asyncTask = new LoadHtml();
            asyncTask.execute(new Note[]{currentNote});
        }

        //初始化FloadButton
        FloatingActionButton toEditNoteFloatingButton = (FloatingActionButton) findViewById(R.id.toEditNoteFloatingButton);

        toEditNoteFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReadNoteActivity.this, EditNoteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(NoteListFragment.NOTE_INIT_TYPE, currentNoteType);
                bundle.putInt(NoteListFragment.EDIT_NOTE_ID, currentNote.getNoteId());
                intent.putExtras(bundle);

                ReadNoteActivity.this.startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        currentNote = DataSupport.where("noteid=?", String.valueOf(editNoteID)).find(Note.class).get(0);
        AsyncTask asyncTask = new LoadHtml();
        asyncTask.execute(new Note[]{currentNote});

    }



    @Override
    protected String getToolbarTitle() {
        return "阅读";
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_read_note;
    }

    public class LoadHtml extends AsyncTask<Note, Void, String> {
        ProgressDialog progressDialog = null;

        public String getHtml(Note note) {
            StringBuffer stringBuffer = new StringBuffer(100);
            return stringBuffer.toString();
        }

        @Override
        protected String doInBackground(Note... params) {
            //获取缓存目录
            File file = CacheUtils.getFileDirectory(getApplicationContext());
            //生成缓存目录下的文件
            File fileContent = new File(file, params[0].getNoteTitle());
            String str = null;
            try {
                OutputStream outputStream = new FileOutputStream(fileContent);
                Writer writer = new OutputStreamWriter(outputStream);
                if ((currentNoteType >= 0) && (noteTypeArray.length >= currentNoteType)) {
                    str = HtmlUtils.getHtmlData(params[0], noteTypeArray[currentNoteType]);
                }
                if (TextUtils.isEmpty(str))
                    writer.write(str);
                Log.d("com.liaobb.evernote", str);
                writer.flush();
                outputStream.close();
                writer.close();
                return str;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        /***
         * 显示 一个对话框
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ReadNoteActivity.this);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            final String mimeType = "text/html";
            final String encoding = "UTF-8";
            noteRead.loadDataWithBaseURL("", str, mimeType, encoding, "");
        }
    }


}
