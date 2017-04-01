package com.example.adm1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Random;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class down extends Activity {
    ProgressBar pb;
    Dialog dialog;
    TextView cur_val;
    public boolean FirstLoad = true;
    Button open;
    Button cancel;
    Downloader3 dow;
    boolean
    var = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.down);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        spinner.setSelection(4);

        Button download = (Button) findViewById(R.id.b1);

        if (getIntent().getExtras().getInt("args") == 100) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String SpinnerChoice = spinner.getSelectedItem().toString();
            final int con = Integer.parseInt(SpinnerChoice);
            EditText editText = (EditText) findViewById(R.id.editText1);

            final String path = getIntent().getExtras().getString("path");
            editText.setText(path);
            //Toast.makeText(spinner.getContext(), "You selected " +con+" Parallel Connections", Toast.LENGTH_SHORT).show();
            //Toast.makeText(spinner.getContext(), "Downoad Path: "+path, Toast.LENGTH_SHORT).show();
            showProgress(path, con);
            new Thread(new Runnable() {
                public void run() {
                    downloadFile(con, path);
                }
            }).start();
        }
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String SpinnerChoice = spinner.getSelectedItem().toString();
                final int con = Integer.parseInt(SpinnerChoice);
                EditText editText = (EditText) findViewById(R.id.editText1);
                String Download_path = editText.getEditableText().toString();
                final String path = Download_path;
                //Toast.makeText(spinner.getContext(), "You selected " +con+" Parallel Connections", Toast.LENGTH_SHORT).show();
                //Toast.makeText(spinner.getContext(), "Downoad Path: "+path, Toast.LENGTH_SHORT).show();
                showProgress(path, con);
                new Thread(new Runnable() {
                    public void run() {
                        downloadFile(con, path);
                    }
                }).start();

            }
        });
        open = (Button) findViewById(R.id.button1);
        open.setVisibility(View.GONE);
    }
    void downloadFile(int con, String path) {
        File SDCardRoot = Environment.getExternalStorageDirectory();
        //showError(SDCardRoot.getName());
        dow = new Downloader3();
        dow.go(SDCardRoot, con, path);
        /*	runOnUiThread(new Runnable() 
   	 {
   		    public void run() {
   		    	Toast.makeText(down.this, "done", Toast.LENGTH_SHORT).show();
   		    }
   		});*/
    }

    void showError(final String err) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(down.this, err, Toast.LENGTH_SHORT).show();
            }
        });
    }
    void showProgress(String file_path, int con) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.myprogressdialog);
        dialog.setTitle("Download Progress");

        TextView text = (TextView) dialog.findViewById(R.id.tv1);
        text.setText("Downloading the file");
        cur_val = (TextView) dialog.findViewById(R.id.cur_pg_tv);
        cur_val.setText("Starting download...");

        dialog.show();

        pb = (ProgressBar) dialog.findViewById(R.id.progress_bar);
        pb.setProgress(0);
        pb.setProgressDrawable(getResources().getDrawable(R.drawable.green_progress));
        final Button pause = (Button) dialog.findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (var) {
                    var = false;
                    pause.setText("Resume");

                    dow.int_th();

                } else {
                    var = true;
                    pause.setText("Pause");
                    synchronized(dow) {
                        dow.notifyAll();
                    }
                }
            }
        });
        cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dow.root = null;
                dow.int_th();
                dialog.dismiss();
            }
        });
    }
    void openFile(final String Name) {
        Toast.makeText(down.this, "Download Finished", Toast.LENGTH_SHORT).show();
        open.setVisibility(View.VISIBLE);
        final File SDCardRooT = Environment.getExternalStorageDirectory();
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                File file = new File(SDCardRooT + "/ADM/" + Name);

                String ext = null;
                String s = file.getName();
                int i = s.lastIndexOf('.');

                if (i > 0 && i < s.length() - 1) {
                    ext = s.substring(i + 1).toLowerCase();
                    //Toast.makeText(getApplicationContext(), "Extension is "+ext, Toast.LENGTH_SHORT).show();
                }

                if (ext.equals("mp4") || ext.equals("3gp") || ext.equals("avi") || (ext.equals("flv"))) {
                    intent.setDataAndType(Uri.fromFile(file), "video/*");
                } else if (ext.equals("pdf")) {
                    intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                } else if (ext.equals("mp3")) {
                    intent.setDataAndType(Uri.fromFile(file), "audio/*");
                } else if (ext.equals("txt")) {
                    intent.setDataAndType(Uri.fromFile(file), "text/*");
                } else {
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    String type = mime.getMimeTypeFromExtension(ext);

                    intent.setDataAndType(Uri.fromFile(file), type);
                }
                startActivity(intent);

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    class Downloader3 implements Runnable {
        File root;
        private URL url;
        private int step, len, no_t;
        int l_bounds, u_bounds = -1;
        private String[] fileArray;
        private double speed = 0;
        String fileName;
        Thread[] th;
        public void run() {
            Socket con = null;
            try {
                /*down d = new down();*/
                //showError(Thread.currentThread().getName());
                DataInputStream dis; // input stream that will read data from the file.
                FileOutputStream fos; //used to write data from input stream to file.
                byte[] fileData; //byte array used to hold data from downloaded file.
                int l_index = getLowerIndex();
                int u_index = getUpperIndex();
                if ((u_index + step) > len) {
                    u_index += len - ((len / no_t) * no_t);
                    System.out.println("Modified upper index for the thread" + Thread.currentThread().getName() + " is - " + u_index);
                }
                con = new Socket(url.getHost(), 80);
                OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
                out.write("GET " + url.getFile() + " HTTP/1.1\r\n");
                out.write("Host: " + url.getHost() + "\r\n");
                out.write("Range: bytes=" + Integer.toString(l_index) + "-" + Integer.toString(u_index) + "\r\n\r\n");
                out.flush();
                dis = new DataInputStream(con.getInputStream()); // get a data stream from the url connection.
                File file = new File(root + "/ADM/file" + Thread.currentThread().getName());
                fos = new FileOutputStream(file);
                fileData = new byte[8192]; // determine how many byes the file size is and make array big enough to hold the data
                int size = u_index - l_index + 1;
                int s = size;
                int x = 0;
                while (true) {
                    try {
                        convert(dis);
                        System.out.println("Entering Value for " + Thread.currentThread().getName() + " x value - " + x);
                        //long start = System.currentTimeMillis();
                        long time = 0;
                        double prev = 0, curr = 0;
                        while (size > 0) {
                            long t1 = System.nanoTime();
                            int y = dis.read(fileData);
                            long t2 = System.nanoTime();
                            fos.write(fileData, 0, y);
                            time = time + t2 - t1;
                            size = size - y;
                            curr = (((s - size) * Math.pow(10, 9)) / time) / 1024;
                            // showError(String.valueOf(y));
                            if (Thread.currentThread().interrupted()) {
                                //  showError("Awesome");
                                if (root == null) {
                                    call(con, fos, file);
                                    return;
                                } else {
                                    con.close();
                                    threadWait();
                                    con = new Socket(url.getHost(), 80);
                                    out = new OutputStreamWriter(con.getOutputStream());
                                    out.write("GET " + url.getFile() + " HTTP/1.1\r\n");
                                    out.write("Host: " + url.getHost() + "\r\n");
                                    out.write("Range: bytes=" + Integer.toString(l_index + s - size) + "-" + Integer.toString(u_index) + "\r\n\r\n");
                                    out.flush();
                                    dis = new DataInputStream(con.getInputStream());
                                    convert(dis);
                                    time = 0;
                                    curr = 0;
                                }
                            }
                            up_progress(y, curr, prev);
                            prev = curr;
                        }
                        break;
                    } catch (EOFException io) {
                        try {
                            out.write("H");
                        } catch (SocketException e) {
                            con.close();
                            con = new Socket(url.getHost(), 80);
                            out = new OutputStreamWriter(con.getOutputStream());
                            out.write("GET " + url.getFile() + " HTTP/1.1\r\n");
                            out.write("Host: " + url.getHost() + "\r\n");
                            out.write("Range: bytes=" + Integer.toString(u_index - size - 1) + "-" + Integer.toString(u_index) + "\r\n\r\n");
                            out.flush();
                            dis = new DataInputStream(con.getInputStream());
                            System.out.println("\n\n\n\n\n\n\nReconnecting " + Thread.currentThread().getName());
                        }
                    }
                }
                dis.close(); // close the data input stream
                con.close();
                //create an object representing the file we want to save
                // showError(Thread.currentThread().getName()+"File created");
                fileArray[l_index / step] = "file" + Thread.currentThread().getName();
                // up_progress();
                fos.close(); // close the output stream writer
            } catch (IOException io) {
                showError("io" + io);
                //   System.out.println(Thread.currentThread().getName() +" - "+ con.connected)

            } catch (Exception e) {
                showError("E" + e);
                showError("Error" + e);
            }
        }
        void call(Socket con, FileOutputStream fos, File file) throws IOException {
            if (con != null)
                con.close();
            if (fos != null)
                fos.close();
            if (file != null)
                file.delete();
        }
        void threadWait() {
            try {
                synchronized(this) {
                    wait();
                }
            } catch (InterruptedException e) {
                showError("" + e);
            }
        }
        synchronized void up_progress(final int up, double current, double previous) {
            speed = speed + current - previous;
            runOnUiThread(new Runnable() {
                public void run() {
                    int d = pb.getProgress();
                    int x = d + up;
                    double p = (((double) x / len) * 100);
                    pb.setProgress(x);
                    cur_val.setText("Download speed: " + (String.format("%.2f", speed)) + "KB/sec  Remaining:" + (pb.getMax() - x) + " Bytes  Downloaded:" + x + "  Bytes (" + (String.format("%.2f", p)) + "%)");
                    if (x == pb.getMax()) {
                        down.this.openFile(fileName);
                        dialog.dismiss();
                    } //  Estimated time Remaining:" +((x/1024)/(speed*60))
                }
            });
        }
        void convert(DataInputStream dis) {
            byte b;
            while (true) {
                try {
                    b = dis.readByte();
                    if (b == 13) {
                        b = dis.readByte();
                        if (b == 10) {
                            b = dis.readByte();
                            if (b == 13) {
                                b = dis.readByte();
                                if (b == 10) {
                                    break;
                                }
                            }
                        }
                    }
                } catch (IOException e) {}
            }
        }
        public void go(File d, int con, String path) {
            try {
                root = d;
                url = new URL(path);
                /*
                 * To get the filename without extension.
                 */
                int h1 = path.lastIndexOf('/');
                int h2 = path.lastIndexOf('.');
                String name_without_extension = path.substring(h1 + 1, h2);

                /*
                 * Creating HTTP URL Connection
                 */
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);

                //connect
                urlConnection.connect();
                /*
                 * Getting content Length
                 */
                len = urlConnection.getContentLength();
                speed = 0;
                if (len > 0)
                    pb.setMax(len);
                else {
                    showError("Problem in downloading the file. Please check your URL or Internet Connection");
                    urlConnection.disconnect();
                    dialog.dismiss();
                    return;
                }
                String type = urlConnection.getContentType();
                //showError("Content type= "+type);
                String file_extension = null;
                int h = type.lastIndexOf('/');
                if (h > 0 && h < type.length() - 1) {
                    file_extension = type.substring(h + 1).toLowerCase();
                    //showError("Extension is "+file_extension);
                    /*
                     * This is to provide name for video file
                     */
                    if ((file_extension.equals("mp4")) || (file_extension.equals("flv")) || (file_extension.equals("avi")) || (file_extension.equals("webm"))) {
                        Random t = new Random();
                        int ran = t.nextInt(100);

                        name_without_extension = "videoplayback".concat(Integer.toString(ran));
                    }
                    if (file_extension.equals("mpeg")) {
                        file_extension = "mp3";
                    }
                    /*
                     * This is to handle text files.
                     */
                    if (file_extension.equals("plain")) {
                        file_extension = "txt";
                    }
                    /*
                     * This piece of code gives name to the file to be downloaded.
                     */
                    fileName = name_without_extension.concat(".").concat(file_extension);
                    // showError("File name "+fileName);
                }


                // showError("Content length= "+urlConnection.getContentLength());
                no_t = con;
                step = len / no_t;
                th = new Thread[no_t];
                fileArray = new String[no_t];
                for (int i = 1; i <= no_t; i++) {
                    Thread t = new Thread(this);
                    th[i - 1] = t;
                    t.start();
                }
                for (int j = 0; j < no_t; j++) {
                    th[j].join();
                }
                if (root == null) {
                    return;
                }
                DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(root + "/ADM/" + fileName)));
                for (int i = 0; i < no_t; i++) {
                    DataInputStream reader = new DataInputStream(new BufferedInputStream(new FileInputStream(root + "/ADM/" + fileArray[i])));
                    byte[] data = new byte[10240];
                    int len = (int) new File(root + "/ADM/" + fileArray[i]).length();
                    //   showError("len"+len);
                    while (len > 10240) {
                        len -= reader.read(data);
                        writer.write(data);
                    }
                    if (len != 0) {
                        reader.read(data);
                        writer.write(data, 0, len);
                    }
                    reader.close();
                    new File(root + "/ADM/" + fileArray[i]).delete();
                }
                writer.close();

            } catch (MalformedURLException m) {
                showError("Please enter valid URL");
                dialog.dismiss();
            } catch (Exception e) {
                showError("Con ex" + e);
            }
        }
        public void int_th() {
            // showError("Here1");
            //  showError(""+no_t);
            for (int i = 0; i < no_t; i++) {
                //  showError(""+i);
                th[i].interrupt();
            }
        }
        public int getLowerIndex() {
            l_bounds = u_bounds + 1;
            return l_bounds;
        }
        public int getUpperIndex() {
            u_bounds += step;
            return u_bounds;
        }
    }

}
