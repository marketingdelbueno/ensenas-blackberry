package com.rim.samples.device.EnSenas;

import com.blackberry.facebook;
import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.Facebook.Permissions;
import com.blackberry.facebook.FacebookException;
import com.blackberry.facebook.inf.Profile;
import com.blackberry.facebook.inf.User;
 
import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.system.Application;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;
 
/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class post extends MainScreen {
         
        public static User user;
     
        public static Facebook facebook_ ;
   
        User facebook_user;
   
        public static Profile[] friends;
   
        private final static String NEXT_URL = "http://www.facebook.com/connect/login_success.html";
     
        private final static String APPLICATION_ID = "Your Id here ";
     
        private final static String APPLICATION_SECRET = "Your secret here ";
     
        private Facebook fb4u;
     
        public static final String[] USER_PERMISSIONS = new String[] {                    
                Permissions.OFFLINE_ACCESS,
                Permissions.EMAIL,                    
                Permissions.USER_ABOUT_ME,
                Permissions.USER_BIRTHDAY,                    
                Permissions.PUBLISH_STREAM
                /*Permissions.OFFLINE_ACCESS,
                Permissions.USER_ABOUT_ME,
                Permissions.USER_BIRTHDAY,
                Permissions.PUBLISH_STREAM*/
        };
       
        public post(){
                final ButtonField btn=new ButtonField("Post");
                add(btn);      
                FieldChangeListener listener=new FieldChangeListener() {
                        public void fieldChanged(Field field, int context) {
                                if(field==btn){
                                        checkPermissions();
                                        ApplicationSettings settings = new ApplicationSettings(NEXT_URL, APPLICATION_ID, APPLICATION_SECRET, USER_PERMISSIONS);
                                        facebook_=Facebook.getInstance(settings);
                                        synchronized (Application.getEventLock())
                                        {
                                                try {
                                                        long l = System.currentTimeMillis();
                                                        String time = String.valueOf(l);
                                                        user = facebook_.getCurrentUser();
                                                        user.publishPost("posting from my application "+"  "+ time , "", "", "", "", "", "");
                                                        Dialog.inform("Message posted");
                                                } catch (FacebookException e) {
                                                 // TODO Auto-generated catch block
                                                        e.printStackTrace();
                                                } catch (Exception e) {
                                                        // TODO: handle exception
                                                        System.out.println("Exception is coming from "+e.toString());
                                                }
                                        }
                                }
                        }};
                        btn.setChangeListener(listener);
        }
   
     private static void checkPermissions() {
             
         ApplicationPermissionsManager apm = ApplicationPermissionsManager.getInstance();
             
         ApplicationPermissions original = apm.getApplicationPermissions();
             
         if ((original.getPermission(ApplicationPermissions.PERMISSION_MEDIA) == ApplicationPermissions.VALUE_ALLOW) &&
            (original.getPermission(ApplicationPermissions.PERMISSION_LOCATION_DATA) == ApplicationPermissions.VALUE_ALLOW) &&
            (original.getPermission(ApplicationPermissions.PERMISSION_RECORDING) == ApplicationPermissions.VALUE_ALLOW) &&
            (original.getPermission(ApplicationPermissions.PERMISSION_INPUT_SIMULATION) == ApplicationPermissions.VALUE_ALLOW) &&
            (original.getPermission(ApplicationPermissions.PERMISSION_DEVICE_SETTINGS) == ApplicationPermissions.VALUE_ALLOW) &&
            (original.getPermission(ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION) == ApplicationPermissions.VALUE_ALLOW)&&
            (original.getPermission(ApplicationPermissions.PERMISSION_INTERNET) == ApplicationPermissions.VALUE_ALLOW) &&
            (original.getPermission(ApplicationPermissions.PERMISSION_SERVER_NETWORK) == ApplicationPermissions.VALUE_ALLOW) &&
            (original.getPermission(ApplicationPermissions.PERMISSION_EMAIL) == ApplicationPermissions.VALUE_ALLOW)) {
                     
                 return;
             
         }
           
         ApplicationPermissions permRequest = new ApplicationPermissions();            
         permRequest.addPermission(ApplicationPermissions.PERMISSION_MEDIA);            
         permRequest.addPermission(ApplicationPermissions.PERMISSION_LOCATION_DATA);
         permRequest.addPermission(ApplicationPermissions.PERMISSION_RECORDING);
         permRequest.addPermission(ApplicationPermissions.PERMISSION_INPUT_SIMULATION);
         permRequest.addPermission(ApplicationPermissions.PERMISSION_DEVICE_SETTINGS);
         permRequest.addPermission(ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION);
         permRequest.addPermission(ApplicationPermissions.PERMISSION_INTERNET);
         permRequest.addPermission(ApplicationPermissions.PERMISSION_SERVER_NETWORK);
         permRequest.addPermission(ApplicationPermissions.PERMISSION_EMAIL);
         boolean acceptance = ApplicationPermissionsManager.getInstance().invokePermissionsRequest(permRequest);
         
         if (acceptance) {
                 return;
         } else {
                 
         }
             
     
     }
 
}
