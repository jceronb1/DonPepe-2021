package com.example.donpepe.helpers;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class AskPermission {
    public static void ask(Activity context, String permiso, String justificacion, int idCode) {

        // Revisar si tengo permisos
        int permission = ContextCompat.checkSelfPermission(context, permiso);
        //Validar si ya tengo el permiso
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //Validar si el ususario denego el permiso y reintento solicitarlo nuevamente
            if (!ActivityCompat.shouldShowRequestPermissionRationale(context, permiso)) {
                Toast.makeText(context, "Se requiere habilitar los permisos", Toast.LENGTH_SHORT).show();
            }
            // request the permission.
            ActivityCompat.requestPermissions(context, new String[]{permiso}, idCode);
        }
    }

    public static void asKMultiple(Activity context, String[] permissions, int idCode ){
        List<String> missingPermissions = new ArrayList<String>();

        for(int i = 0; i < permissions.length; i++){
            Integer grantedCode = ContextCompat.checkSelfPermission(context, permissions[i]);
            if(grantedCode != PackageManager.PERMISSION_GRANTED){
                missingPermissions.add(permissions[i]);
                if (!ActivityCompat.shouldShowRequestPermissionRationale(context, permissions[i])) {
                    Toast.makeText(context, "Se requiere habilitar los permisos", Toast.LENGTH_SHORT).show();
                }
            }
        }

        String[] misssingPermissionsArr = new String[missingPermissions.size()];
        for(int i = 0; i < missingPermissions.size(); i++){
            misssingPermissionsArr[i] = missingPermissions.get(i);
        }

        if(!missingPermissions.isEmpty()){
            ActivityCompat.requestPermissions(context, misssingPermissionsArr, idCode);
        }

    }
}

