package com.example.webservertest01;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.util.TimeUtils;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.response.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.nanohttpd.protocols.http.response.Response.newFixedLengthResponse;

public class HTTPServer extends NanoHTTPD {

    Context mContext;



    private void playSound(){
        MediaPlayer mediaPlayer = MediaPlayer.create(mContext, R.raw.bell);
        mediaPlayer.start();

    }

    public HTTPServer(Context pContext) throws IOException {


        super(8080);
        mContext=pContext;
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:8080/ \n");

        MainActivity lMainActivity=(MainActivity)mContext;
        lMainActivity.addText("WebServer gestartet");
    }


    @Override
    public Response serve(IHTTPSession session) {

        MainActivity lMainActivity=(MainActivity)mContext;


        String msg = "<html><body><h1>Hello server</h1>\n";
        Map<String, String> parms = session.getParms();
        if (parms.get("alarm") == null) {
            msg += "<form action='?' method='get'>\n  <p>Your name: <input type='text' name='alarm'></p>\n" + "</form>\n";
            lMainActivity.addText("leere Response Anfrage");



        } else {
            msg += "<p>Alarm Parameter empfangen, Wert(" + parms.get("alarm") + ")</p>";

            lMainActivity.addText("Alarm Parameter empfangen,Wert="+parms.get("alarm")+"!");



            if (parms.get("alarm").equals("web")){
                playSound();
                lMainActivity.addText("Starte Browser 2");


                //https://developer.android.com/reference/android/os/PowerManager
                /*
                PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
                @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock screenWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
                        "My Tag");
                screenWakeLock.acquire();
    */

                /*
                PowerManager pm = (PowerManager) lMainActivity.getSystemService(Context.POWER_SERVICE);
                @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
                wl.acquire();
                */

                PowerManager TempPowerManager = (PowerManager) lMainActivity.getSystemService(Context.POWER_SERVICE);
                @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock TempWakeLock = TempPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "TempWakeLock");
                TempWakeLock.acquire();


                lMainActivity.addText("Starte Browser 3");
                //Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://guest:guest@172.16.0.21:80/tmpfs/auto.jpg"));
                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://172.16.0.21:80/cgi-bin/hi3510/mjpegstream.cgi?-chn=11&-usr=guest&-pwd=guest"));
        
                lMainActivity.startActivity(viewIntent);


                try
                {
                    Thread.sleep(1000);
                }
                catch(InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }

                //screenWakeLock.release();

//Do whatever you need right here
               // wl.release();
                TempWakeLock.release();

            }
            //lockscreen Flags abschalten
            //lMainActivity.clearFlags();


        }




        return newFixedLengthResponse(msg + "</body></html>\n");
    }

}
