package fr.guillaumevillena.opendnsupdater.utils;

/**
 * Created by guill on 28/06/2018.
 */

public class PreferenceCodes {

    /*


            Name                Value               Desc
        ------------------ OpenDns related prefs -------------------
        OpenDns_Username        String              The username to contact to update
        OpenDns_Password        String              The password that correspond to that username
        OpenDns_Network         String              The Network name to be updated
        OpenDns_LastUpdate      String              Date and time of the latest update
        -------------------- App related prefs ---------------------
        App_AutoUpdate          Boolean             Listen to connectivity changes and update automaticaly
        App_Notify              Boolean             Display a notification when updated
        App_ForceDns            Boolean             Force usage of openDns when the connectivity change
        APP_USE_BLACK_LIST      Boolean             Filter update and service execution on specifed networks
        APP_BLACKLIST           StringList          LIst of filtered networks
        APP_BLACKLIST_ENTRIES   StringList          LIst of available nets for filter



     */

    public static final String OPENDNS_USERNAME = "service.opendns.username";
    public static final String OPENDNS_PASSWORD = "service.opendns.password";
    public static final String OPENDNS_NETWORK = "service.opendns.network";
    public static final String OPENDNS_LAST_UPDATE = "service.opendns.lastupdate";

    public static final String APP_AUTO_UPDATE = "app.preferences.autoupdate";
    public static final String APP_NOTIFY = "app.preferences.sendNotifications";
    public static final String APP_DNS = "app.preferences.forceDns";
    public static final String APP_USE_BLACK_LIST = "app.preferences.useblacklist";
    public static final String APP_BLACKLIST = "app.preferences.blacklist";
    public static final String APP_BLACKLIST_ENTRIES = "app.preferences.blacklist_entries";

    public static final String APP_SECURITY_PASSWORD = "app.security.password";
    public static final String APP_SECURITY_USE_PASSWORD = "app.security.use.password";

    public static final String FIRST_TIME = "app.firsttime";
    public static final String FIRST_TIME_CONFIG_FINISHED = "app.firsttimeconfigfinished";

    //Spotlight related
    public static final String SPOTLIGHT_SHOWN = "app.spotlight.shown";
    public static final String SPOTLIGHT_ID_SWITCH_AUTO_UPDATED = "app.spotlight.switch.auto_update";
    public static final String SPOTLIGHT_ID_SWITCH_VPN_SERVER = "app.spotlight.switch.vpn_server";
    public static final String SPOTLIGHT_ID_SWITCH_NOTIFICATION = "app.spotlight.switch.notification";
    public static final String SPOTLIGHT_ID_STATUS_IP_UPDATED = "app.spotlight.status.ip_updated";
    public static final String SPOTLIGHT_ID_STATUS_USING_OPENDNS = "app.spotlight.status.using_opendns";
    public static final String SPOTLIGHT_ID_STATUS_USING_VPN = "app.spotlight.status.using_vpn";
    public static final String SPOTLIGHT_ID_STATUS_FILTERING_WEBSITES = "app.spotlight.status.filtering_websites";
    public static final String SPOTLIGHT_ID_TOP_BUTTON_REFRESH = "app.spotlight.status.refresh";
    public static final String SPOTLIGHT_ID_TOP_BUTTON_SETTINGS = "app.spotlight.status.settings";


    public static final String BUGSNAG_ACTIVATED = "app.crashreport.consent.bugsnag";
}
