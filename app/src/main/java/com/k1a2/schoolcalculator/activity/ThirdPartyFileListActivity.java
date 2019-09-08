package com.k1a2.schoolcalculator.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.k1a2.schoolcalculator.DriveServiceHelper;
import com.k1a2.schoolcalculator.GoogleDriveFileHolder;
import com.k1a2.schoolcalculator.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThirdPartyFileListActivity extends AppCompatActivity {
    // ...
    private String mPath;
    private String account;
    private DriveServiceHelper mDriveServiceHelper;
    private ProgressDialog mDialog;
    private CancellationTokenSource cts;
    private FileListAdapter mFilesAdapter;
    private String third_party_dir = "";
    Drive googleDriveService;

    public static String TYPE_AUDIO = "application/vnd.google-apps.audio";
    public static String TYPE_GOOGLE_DOCS = "application/vnd.google-apps.document";
    public static String TYPE_GOOGLE_DRAWING = "application/vnd.google-apps.drawing";
    public static String TYPE_GOOGLE_DRIVE_FILE = "application/vnd.google-apps.file";
    //public static String TYPE_GOOGLE_DRIVE_FOLDER = DriveFolder.MIME_TYPE;
    public static String TYPE_GOOGLE_FORMS = "application/vnd.google-apps.form";
    public static String TYPE_GOOGLE_FUSION_TABLES = "application/vnd.google-apps.fusiontable";
    public static String TYPE_GOOGLE_MY_MAPS = "application/vnd.google-apps.map";
    public static String TYPE_PHOTO = "application/vnd.google-apps.photo";
    public static String TYPE_GOOGLE_SLIDES = "application/vnd.google-apps.presentation";
    public static String TYPE_GOOGLE_APPS_SCRIPTS = "application/vnd.google-apps.script";
    public static String TYPE_GOOGLE_SITES = "application/vnd.google-apps.site";
    public static String TYPE_GOOGLE_SHEETS = "application/vnd.google-apps.spreadsheet";
    public static String TYPE_UNKNOWN = "application/vnd.google-apps.unknown";
    public static String TYPE_VIDEO = "application/vnd.google-apps.video";
    public static String TYPE_3_RD_PARTY_SHORTCUT = "application/vnd.google-apps.drive-sdk";

    private final Executor mExecutor = Executors.newFixedThreadPool(5);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thirdparty_filelist);

        mDialog = new ProgressDialog(this);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setCancelable(true);

        mPath = getIntent().getStringExtra("path");

        // 구글은 최상위 폴더는 root이다.
        if(mPath == null) {
            mPath = "root";
        }

        mFilesAdapter = new FileListAdapter();
        ListView fileLists = findViewById(R.id.fileLists);
        fileLists.setAdapter(mFilesAdapter);

        fileLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File item = (File)mFilesAdapter.getItem(position);
                // 폴더면 다음 액티비티로 넘기고 파일이면 다운받는다.
                if ("application/vnd.google-apps.folder".equals(item.getMimeType())) {
                    Intent intent = new Intent(ThirdPartyFileListActivity.this, ThirdPartyFileListActivity.class);
                    intent.putExtra("email", account);
                    intent.putExtra("path", item.getId());
                    startActivity(intent);

                }  else if("application/vnd.google-apps.document".equals(item.getMimeType()) ||
                        "application/vnd.google-apps.presentation".equals(item.getMimeType()) ||
                        "application/vnd.google-apps.spreadsheet".equals(item.getMimeType())) {

                    mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            mDialog.dismiss();
                            cts.cancel();
                        }
                    });
                    mDialog.setMessage("Downloading");
                    mDialog.show();
                    cts = new CancellationTokenSource();
                    mDriveServiceHelper.readDocsFile(item.getId(), cts.getToken())
                            .addOnSuccessListener(new OnSuccessListener<Pair<String,byte[]>>() {
                                @Override
                                public void onSuccess(Pair<String, byte[]> stringPair) {
                                    mDialog.dismiss();
                                    String path = third_party_dir + stringPair.first;
                                    java.io.File file = new java.io.File(path);
                                    if(writeBytesToFile(file, stringPair.second)) {
                                        viewFileInExternalApp(file);
                                    }
                                    else {

                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.printStackTrace();
                                }
                            })
                            .addOnCanceledListener(new OnCanceledListener() {
                                @Override
                                public void onCanceled() {
                                    Log.d("cancel", "canceld docs");
                                }
                            });
                } else {
                    cts = new CancellationTokenSource();
                    mDriveServiceHelper.readFile(item.getId(), cts.getToken())
                            .addOnSuccessListener(new OnSuccessListener<Pair<String, byte[]>>() {
                                @Override
                                public void onSuccess(Pair<String, byte[]> stringPair) {

                                    String path = third_party_dir + stringPair.first;
                                    Log.d("path", "path : " + path);
                                    java.io.File file = new java.io.File(path);
                                    if(writeBytesToFile(file, stringPair.second)) {
                                        viewFileInExternalApp(file);
                                    }
                                    else {

                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mDriveServiceHelper == null) {
            GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
            GoogleAccountCredential credential =
                    GoogleAccountCredential.usingOAuth2(
                            this, Collections.singleton(DriveScopes.DRIVE_FILE));
            credential.setSelectedAccount(googleSignInAccount.getAccount());
            googleDriveService =
                    new Drive.Builder(
                            AndroidHttp.newCompatibleTransport(),
                            new GsonFactory(),
                            credential)
                            .setApplicationName("Drive API Migration")
                            .build();

            // The DriveServiceHelper encapsulates all REST API and SAF functionality.
            // Its instantiation is required before handling any onClick actions.
            mDriveServiceHelper = new DriveServiceHelper(googleDriveService);
            account = googleSignInAccount.getEmail();
        }

        loadData();
    }

    private void loadData() {

        if(account != null || account.length() != 0) {
            ((TextView)findViewById(R.id.title_a)).setText(account);
        }

        if(mFilesAdapter.mFiles == null || mFilesAdapter.mFiles.size() == 0) {

            if (mDriveServiceHelper != null) {
                Log.d("qf", "Querying for files.");

                mDialog.setMessage("Loading");
                mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Log.d("cd", "cancel in dialog");
                        mDialog.dismiss();
                        cts.cancel();
                    }
                });
                mDialog.show();

                cts = new CancellationTokenSource();
                searchFile("수업준비자료.zip", TYPE_GOOGLE_DRIVE_FILE, cts.getToken())
                        .addOnSuccessListener(new OnSuccessListener<FileList>() {
                            @Override
                            public void onSuccess(FileList fileList) {
                                if (fileList != null) {
                                    mFilesAdapter.setFiles(fileList.getFiles());
                                    mDialog.dismiss();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("uqf", "Unable to query files.", e);
                                mDialog.dismiss();
                            }
                        });
//                mDriveServiceHelper.queryFiles(mPath, cts.getToken())
//                        .addOnSuccessListener(fileList -> {
//                            if(fileList != null) {
//                                mFilesAdapter.setFiles(fileList.getFiles());
//                            }
//
//                            mDialog.dismiss();
//                        })
//                        .addOnFailureListener(exception -> Log.e("uqf", "Unable to query files.", exception));
            }
        }
    }

//    public Task<GoogleDriveFileHolder> searchFolder(String folderName) {
//        return Tasks.call(mExecutor, () -> {
//
//            // Retrive the metadata as a File object.
//            FileList result = googleDriveService.files().list()
//                    .setQ("mimeType = '" + DriveFolder.MIME_TYPE + "' and name = '" + folderName + "' ")
//                    .setSpaces("drive")
//                    .execute();
//            GoogleDriveFileHolder googleDriveFileHolder = new GoogleDriveFileHolder();
//            if (result.getFiles().size() > 0) {
//                googleDriveFileHolder.setId(result.getFiles().get(0).getId());
//                googleDriveFileHolder.setName(result.getFiles().get(0).getName());
//
//            }
//            return googleDriveFileHolder;
//        });
//    }

    public Task<FileList> searchFile(String fileName, String mimeType, CancellationToken token) {
        token.onCanceledRequested(new OnTokenCanceledListener() {
            @Override
            public void onCanceled() {
                Log.d("DriveServiceHelper", "cancel requests.");
            }
        });

        final TaskCompletionSource<FileList> tcs = new TaskCompletionSource<>(token);

        return Tasks.call(mExecutor, new Callable<FileList>() {

            @Override
            public FileList call() throws Exception {
                FileList result = googleDriveService.files().list()
                        .setQ("name = '" + fileName + "' and mimeType ='" + mimeType + "'")
                        .setSpaces("drive")
                        .setFields("files(id, name,size,createdTime,modifiedTime,starred)")
                        .execute();
                GoogleDriveFileHolder googleDriveFileHolder = new GoogleDriveFileHolder();
                if (result.getFiles().size() > 0) {

                    googleDriveFileHolder.setId(result.getFiles().get(0).getId());
                    googleDriveFileHolder.setName(result.getFiles().get(0).getName());
                    googleDriveFileHolder.setModifiedTime(result.getFiles().get(0).getModifiedTime());
                    googleDriveFileHolder.setSize(result.getFiles().get(0).getSize());
                }


                return result;
            }
        });
    }

    public static boolean writeBytesToFile(java.io.File file, byte[] data) {
        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(data);
            stream.close();
            return true;
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

        return false;
    }

    class FileListAdapter extends BaseAdapter {
        private List<File> mFiles;

        public void setFiles(List<File> files) {
            mFiles = Collections.unmodifiableList(new ArrayList<>(files));
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mFiles == null ? 0 : mFiles.size();
        }

        @Override
        public Object getItem(int position) {
            return mFiles == null ? null : mFiles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ViewHolder holder = null;
            if(convertView == null) {
                holder = new ViewHolder();

                convertView = inflater.inflate(R.layout.licence_data_row, parent, false);

                holder.mainText = (TextView)convertView.findViewById(R.id.mainText);
                holder.subText = (TextView)convertView.findViewById(R.id.subText);

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder)convertView.getTag();
            }

            File item = (File)mFiles.get(position);
            String name = item.getName();
            holder.mainText.setText(name);

            return convertView;
        }
    }

    static class ViewHolder {
        TextView mainText, subText;
    }

    private void viewFileInExternalApp(java.io.File result) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext = result.getName().substring(result.getName().indexOf(".") + 1);
        String type = mime.getMimeTypeFromExtension(ext);

        Uri tempUri = FileProvider.getUriForFile(this,
                "com.example.android.fileprovider",
                result);
        intent.setDataAndType(tempUri, type);

        // 외부에서 해당 URI를 접근할 수 있도록 한다.
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Check for a handler first to avoid a crash
        Intent chooser = Intent.createChooser(intent, "Open File");
        try {
            startActivity(chooser);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }
    }
}

