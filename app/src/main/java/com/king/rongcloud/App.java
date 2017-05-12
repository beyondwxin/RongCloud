package com.king.rongcloud;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.view.View;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.facebook.stetho.inspector.database.DefaultDatabaseConnectionProvider;
import com.facebook.stetho.inspector.protocol.ChromeDevtoolsDomain;
import com.king.rongcloud.db.Friend;
import com.king.rongcloud.message.TestMessage;
import com.king.rongcloud.message.provider.ContactNotificationMessageProvider;
import com.king.rongcloud.message.provider.TestMessageProvider;
import com.king.rongcloud.server.pinyin.CharacterParser;
import com.king.rongcloud.server.utils.NLog;
import com.king.rongcloud.server.utils.RongGenerate;
import com.king.rongcloud.stetho.RongDatabaseDriver;
import com.king.rongcloud.stetho.RongDatabaseFilesProvider;
import com.king.rongcloud.stetho.RongDbFilesDumperPlugin;
import com.king.rongcloud.view.UserDetailActivity;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.util.List;

import cn.rongcloud.contactcard.ContactCardExtensionModule;
import cn.rongcloud.contactcard.IContactCardClickListener;
import cn.rongcloud.contactcard.IContactCardInfoProvider;
import cn.rongcloud.contactcard.message.ContactMessage;
import io.rong.imageloader.core.DisplayImageOptions;
import io.rong.imageloader.core.display.FadeInBitmapDisplayer;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.RealTimeLocationMessageProvider;
import io.rong.imlib.ipc.RongExceptionHandler;
import io.rong.imlib.model.UserInfo;
import io.rong.push.RongPushClient;
import io.rong.push.common.RongException;

/**
 * Created by king on 2017/4/28.
 */


    public class App extends MultiDexApplication {

        private static DisplayImageOptions options;

        @Override
        public void onCreate() {

            super.onCreate();
            Stetho.initialize(new Stetho.Initializer(this) {
                @Override
                protected Iterable<DumperPlugin> getDumperPlugins() {
                    return new Stetho.DefaultDumperPluginsBuilder(App.this)
                            .provide(new RongDbFilesDumperPlugin(App.this, new RongDatabaseFilesProvider(App.this)))
                            .finish();
                }

                @Override
                protected Iterable<ChromeDevtoolsDomain> getInspectorModules() {
                    Stetho.DefaultInspectorModulesBuilder defaultInspectorModulesBuilder = new Stetho.DefaultInspectorModulesBuilder(App.this);
                    defaultInspectorModulesBuilder.provideDatabaseDriver(new RongDatabaseDriver(App.this, new RongDatabaseFilesProvider(App.this), new DefaultDatabaseConnectionProvider()));
                    return defaultInspectorModulesBuilder.finish();
                }
            });

            if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {

//            LeakCanary.install(this);//内存泄露检测
                RongPushClient.registerHWPush(this);
                RongPushClient.registerMiPush(this, "2882303761517573361", "5671757310361");
                try {
                    RongPushClient.registerGCM(this);
                } catch (RongException e) {
                    e.printStackTrace();
                }

                /**
                 * 注意：
                 *
                 * IMKit SDK调用第一步 初始化
                 *
                 * context上下文
                 *
                 * 只有两个进程需要初始化，主进程和  push 进程
                 */
                //RongIM.setServerInfo("nav.cn.ronghub.com", "img.cn.ronghub.com");
                RongIM.init(this);
                NLog.setDebug(true);//Seal Module Log 开关
                SealAppContext.init(this);
                SharedPreferencesContext.init(this);
                Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));

                try {
                    RongIM.registerMessageTemplate(new ContactNotificationMessageProvider());
                    RongIM.registerMessageTemplate(new RealTimeLocationMessageProvider());
                    RongIM.registerMessageType(TestMessage.class);
                    RongIM.registerMessageTemplate(new TestMessageProvider());


                } catch (Exception e) {
                    e.printStackTrace();
                }

                openSealDBIfHasCachedToken();

                options = new DisplayImageOptions.Builder()
                        .showImageForEmptyUri(R.drawable.de_default_portrait)
                        .showImageOnFail(R.drawable.de_default_portrait)
                        .showImageOnLoading(R.drawable.de_default_portrait)
                        .displayer(new FadeInBitmapDisplayer(300))
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .build();

//            RongExtensionManager.getInstance().registerExtensionModule(new PTTExtensionModule(this, true, 1000 * 60));
                RongExtensionManager.getInstance().registerExtensionModule(new ContactCardExtensionModule(new IContactCardInfoProvider() {
                    @Override
                    public void getContactCardInfoProvider(final IContactCardInfoCallback contactInfoCallback) {
                        SealUserInfoManager.getInstance().getFriends(new SealUserInfoManager.ResultCallback<List<Friend>>() {
                            @Override
                            public void onSuccess(List<Friend> friendList) {
                                contactInfoCallback.getContactCardInfoCallback(friendList);
                            }

                            @Override
                            public void onError(String errString) {
                                contactInfoCallback.getContactCardInfoCallback(null);
                            }
                        });
                    }
                }, new IContactCardClickListener() {
                    @Override
                    public void onContactCardClick(View view, ContactMessage content) {
                        Intent intent = new Intent(view.getContext(), UserDetailActivity.class);
                        Friend friend = SealUserInfoManager.getInstance().getFriendByID(content.getId());
                        if (friend == null) {
                            UserInfo userInfo = new UserInfo(content.getId(), content.getName(),
                                    Uri.parse(TextUtils.isEmpty(content.getImgUrl()) ? RongGenerate.generateDefaultAvatar(content.getName(), content.getId()) : content.getImgUrl()));
                            friend = CharacterParser.getInstance().generateFriendFromUserInfo(userInfo);
                        }
                        intent.putExtra("friend", friend);
                        view.getContext().startActivity(intent);
                    }
                }));
            }
        }

        public static DisplayImageOptions getOptions() {
            return options;
        }

        private void openSealDBIfHasCachedToken() {
            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            String cachedToken = sp.getString("loginToken", "");
            if (!TextUtils.isEmpty(cachedToken)) {
                String current = getCurProcessName(this);
                String mainProcessName = getPackageName();
                if (mainProcessName.equals(current)) {
                    SealUserInfoManager.getInstance().openDB();
                }
            }
        }

        public static String getCurProcessName(Context context) {
            int pid = android.os.Process.myPid();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
            return null;
        }

    }
