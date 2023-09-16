package com.mypackage.bookloop;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LocalSession {
    Context context;    //context
    SharedPreferences sharedPref;   // to manage the session locally related to an app
    SharedPreferences.Editor prefEditor;    //editor to write or append data into shared preference

    public LocalSession(Context context) {
        this.context = context;
        /**
         * DEFINE OTHER VARIABLES HERE*/

        sharedPref= PreferenceManager.getDefaultSharedPreferences(this.context);
        prefEditor=sharedPref.edit();
    }

    //STORING DATA INTO SHARED PREFERENCES
    public void saveInfo(String uid){
        prefEditor.putBoolean(ConstantKeys.KEY_IS_LOGGED,true); //STORING BOOLEAN VALUES
        prefEditor.putString(ConstantKeys.KEY_UID,uid); //STORING STRING VALUES
        prefEditor.commit(); //FINAL SAVE
    }

    /**
     * TO RETRIEVE THE DATAS
     */
    public boolean checkLog()
    {
        return sharedPref.getBoolean(ConstantKeys.KEY_IS_LOGGED,false);
    }

    public String getInfo(String key)
    {
        return sharedPref.getString(key,null);
    }

    /**
     * DELETE SPECIFIC DATA
     */

    public void deleteMe(String key)
    {
        prefEditor.remove(key);
        prefEditor.commit();
    }

    /**
     * TO CLEAR ALL INFORMATION
     */
    public void clearAll()
    {
        prefEditor.clear();
        prefEditor.commit();
    }
}
