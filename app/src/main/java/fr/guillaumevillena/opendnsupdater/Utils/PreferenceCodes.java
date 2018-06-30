package fr.guillaumevillena.opendnsupdater.Utils;

/**
 * Created by guill on 28/06/2018.
 */

public class PreferenceCodes {

    /*


            Name                Value               Desc
        ------------------ OpenDns related prefs -------------------
        OpenDns_Username    String              The username to contact to update
        OpenDns_Password    String              The password that correspond to that username
        OpenDns_Network     String              The Network name to be updated
        -------------------- App related prefs ---------------------
        App_AutoUpdate      Boolean             Listen to connectivity changes and update automaticaly
        App_Notify          Boolean             Display a notification when updated
        App_ForceDns        Boolean             Force usage of openDns when the connectivity change



     */

    public static final String OPENDNS_USERNAME = "service.opendns.username";
    public static final String OPENDNS_PASSWORD = "service.opendns.password";
    public static final String OPENDNS_NETWORK = "service.opendns.network";
    public static final String OPENDNS_CONFIG_ERRORS  = "service.opendns.nbErrors";

    public static final String APP_AUTO_UPDATE = "app.preferences.autoupdate";
    public static final String APP_NOTIFY = "app.preferences.sendNotifications";
    public static final String APP_DNS = "app.preferences.forceDns";

    public static final String APP_SECURITY_PASSWORD = "app.security.password";
    public static final String APP_SECURITY_USE_PASSWORD = "app.security.use.password";


}