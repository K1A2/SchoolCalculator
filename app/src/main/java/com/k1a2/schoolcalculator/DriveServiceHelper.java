package com.k1a2.schoolcalculator;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DriveServiceHelper {
    private final Executor mExecutor = Executors.newFixedThreadPool(5);
    private final Drive mDriveService;

    public DriveServiceHelper(Drive driveService) {
        mDriveService = driveService;
    }

    /**
     * Creates a text file in the user's My Drive folder and returns its file ID.
     */
    public Task<String> createFile() {
        return Tasks.call(mExecutor, new Callable<String>() {
            @Override
            public String call() throws Exception {
                File metadata = new File()
                        .setParents(Collections.singletonList("root"))
                        .setMimeType("text/plain")
                        .setName("Untitled file");

                File googleFile = mDriveService.files().create(metadata).execute();
                if (googleFile == null) {
                    throw new IOException("Null result when requesting file creation.");
                }

                return googleFile.getId();

            }
        });
    }

    public Task<Pair<String, byte[]>> readDocsFile(final String fileId, CancellationToken token) {
        final TaskCompletionSource<Pair<String, byte[]>> tcs = new TaskCompletionSource<>(token);

        mExecutor.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    File metadata = mDriveService.files().get(fileId).execute();
                    String name = metadata.getName();
                    Drive.Files.Export export = mDriveService.files().export(fileId, "application/pdf");

                    HttpRequest request = export.buildHttpRequest();
                    request.setConnectTimeout(6*30000);
                    request.setReadTimeout(6*30000);
                    HttpResponse response = request.execute();

                    if(!tcs.getTask().isComplete()) {
                        try(InputStream is = response.getContent();
                            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream()) {

                            int bufferSize = 1024;
                            byte[] buffer = new byte[bufferSize];

                            // we need to know how may bytes were read to write them to the byteBuffer
                            int len = 0;
                            while ((len = is.read(buffer)) != -1 && !tcs.getTask().isComplete()) {
                                byteBuffer.write(buffer, 0, len);
                            }


                            Log.d("DriveServiceHelper", "here readdocs??");
                            if(!tcs.getTask().isComplete()) {
                                tcs.setResult(Pair.create(name + ".pdf", byteBuffer.toByteArray()));
                            }
                        }
                    }
                    else {
                        Log.d("DriveServiceHelper", "here disconnect??");
                        response.disconnect();
                    }
                } catch (IOException e) {
                    tcs.setException(e);
                }
            }
        });

        return tcs.getTask();
    }

    /**
     * Opens the file identified by {@code fileId} and returns a {@link Pair} of its name and
     * contents.
     */
    public Task<Pair<String, byte[]>> readFile(final String fileId, CancellationToken token) {

        final TaskCompletionSource<Pair<String, byte[]>> tcs = new TaskCompletionSource<>(token);

        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    File metadata = mDriveService.files().get(fileId).execute();
                    String name = metadata.getName();

                    // Stream the file contents to a String.
                    try (InputStream is = mDriveService.files().get(fileId).executeMediaAsInputStream();
                         ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream()) {
                        int bufferSize = 1024;
                        byte[] buffer = new byte[bufferSize];

                        // we need to know how may bytes were read to write them to the byteBuffer
                        int len = 0;
                        while ((len = is.read(buffer)) != -1 && !tcs.getTask().isComplete()) {
                            byteBuffer.write(buffer, 0, len);
                        }

                        if(!tcs.getTask().isComplete()) {
                            tcs.setResult(Pair.create(name, byteBuffer.toByteArray()));
                        }
                    }
                } catch (IOException e) {
                    tcs.setException(e);
                    e.printStackTrace();
                }

            }
        });

        return tcs.getTask();
    }

    /**
     * Updates the file identified by {@code fileId} with the given {@code name} and {@code
     * content}.
     */
    public Task<Void> saveFile(final String fileId, final String name, final String content) {
        return Tasks.call(mExecutor, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                // Create a File containing any metadata changes.
                File metadata = new File().setName(name);

                // Convert content to an AbstractInputStreamContent instance.
                ByteArrayContent contentStream = ByteArrayContent.fromString("text/plain", content);

                // Update the metadata and contents.
                mDriveService.files().update(fileId, metadata, contentStream).execute();
                return null;
            }
        });
    }

    /**
     * Returns a {@link com.google.api.services.drive.model.FileList} containing all the visible files in the user's My Drive.
     *
     * <p>The returned list will only contain files visible to this app, i.e. those which were
     * created by this app. To perform operations on files not created by the app, the project must
     * request Drive Full Scope in the <a href="https://play.google.com/apps/publish">Google
     * Developer's Console</a> and be submitted to Google for verification.</p>
     */
    public Task<FileList> queryFiles(final String path, CancellationToken token) {
        token.onCanceledRequested(new OnTokenCanceledListener() {
            @Override
            public void onCanceled() {
                Log.d("DriveServiceHelper", "cancel requests.");
            }
        });
        final TaskCompletionSource<FileList> tcs = new TaskCompletionSource<>(token);

        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    FileList list = mDriveService.files().list()
                            .setFields("kind,nextPageToken,files(mimeType,id,kind,name,webViewLink,thumbnailLink,trashed)")
                            .setQ("trashed = false and '" + path + "' IN parents").execute();
                    if(!tcs.getTask().isComplete()) {
                        tcs.setResult(list);
                    }
                } catch (IOException e) {
                    tcs.setResult(null);
                    e.printStackTrace();
                }
            }
        });



        return tcs.getTask();
    }

    /**
     * Returns an {@link android.content.Intent} for opening the Storage Access Framework file picker.
     */
    public Intent createFilePickerIntent() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");

        return intent;
    }

    /**
     * Opens the file at the {@code uri} returned by a Storage Access Framework {@link Intent}
     * created by {@link #createFilePickerIntent()} using the given {@code contentResolver}.
     */
    public Task<Pair<String, String>> openFileUsingStorageAccessFramework(
            final ContentResolver contentResolver, final Uri uri) {
        return Tasks.call(mExecutor, new Callable<Pair<String, String>>() {
            @Override
            public Pair<String, String> call() throws Exception {
                String name;
                try (Cursor cursor = contentResolver.query(uri, null, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        name = cursor.getString(nameIndex);
                    } else {
                        throw new IOException("Empty cursor returned for file.");
                    }
                }

                // Read the document's contents as a String.
                String content;
                try (InputStream is = contentResolver.openInputStream(uri);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    content = stringBuilder.toString();
                }

                return Pair.create(name, content);
            }
        });
    }
}
