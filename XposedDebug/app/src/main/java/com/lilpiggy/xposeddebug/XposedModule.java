package com.lilpiggy.xposeddebug;

import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposedModule implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // 如果需要对所有包 Hook，可以去掉这行
//        if (!lpparam.packageName.equals("com.kmxs.reader")) {
//            return;
//        }

        // Hook Toast 的构造函数
        XposedHelpers.findAndHookConstructor(
                Toast.class, // 目标类
                android.content.Context.class, // 构造函数参数类型
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("Toast constructor called (before)");
                        Log.d("xposed", "Toast constructor called (before)");
                        Object p1 = param.args[0];
                        if (p1.getClass().getSimpleName().contains("CloseAd")) {
                            for(StackTraceElement element: Looper.getMainLooper().getThread().getStackTrace()) {
                                Log.d("xposed", "line: " + element.getLineNumber() + element.getClassName() + "." + element.getMethodName() + "()");
                                XposedBridge.log("line: " + element.getLineNumber() + element.getClassName() + "." + element.getMethodName() + "()");
                            }
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("Toast constructor called (after)");
                        Log.d("xposed", "Toast constructor called (after)");
                    }
                }
        );

        // 也可以 Hook Toast.show() 方法
        XposedHelpers.findAndHookMethod(
                Toast.class, // 目标类
                "show",      // 目标方法
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("Toast about to be shown");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("Toast has been shown");
                    }
                }
        );
    }
}