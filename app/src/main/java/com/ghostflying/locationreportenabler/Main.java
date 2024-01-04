package com.ghostflying.locationreportenabler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by ghostflying on 2/16/15.
 */
public class Main implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        try{
            findAndHookMethod("android.telephony.TelephonyManager", loadPackageParam.classLoader, "getSimOperator", mOperatorCodeHook);
            findAndHookMethod("android.telephony.TelephonyManager", loadPackageParam.classLoader, "getSimCountryIso", mCountryISOHook);
            findAndHookMethod("android.telephony.TelephonyManager", loadPackageParam.classLoader, "getNetworkCountryIso", mCountryISOHook);
            findAndHookMethod("android.telephony.TelephonyManager", loadPackageParam.classLoader, "getSimOperatorName", mOperatorNameHook);
        }
        catch (Throwable t){
            XposedBridge.log(t);
        }
    }

    private XC_MethodHook mOperatorCodeHook = new XC_MethodReplacement() {
        XSharedPreferences sharedPreferences = new XSharedPreferences(BuildConfig.APPLICATION_ID);

        @Override
        protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
            return sharedPreferences.getString("pref_operator_code", "310030");
        }
    };

    private XC_MethodHook mCountryISOHook = new XC_MethodReplacement() {
        XSharedPreferences sharedPreferences = new XSharedPreferences(BuildConfig.APPLICATION_ID);

        @Override
        protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
            return sharedPreferences.getString("pref_operator_country", "us");
        }
    };

    private XC_MethodHook mOperatorNameHook = new XC_MethodReplacement() {
        XSharedPreferences sharedPreferences = new XSharedPreferences(BuildConfig.APPLICATION_ID);

        @Override
        protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
            return sharedPreferences.getString("pref_operator_name", "Centennial");
        }
    };
}
