package com.kraftlabs.crm_new.Config;

/**
 * Created by ajith on 21/7/16.
 */
public class Config {
  //private static String SERVER = "http://excelearthings.net/";
  // private static String SERVER = "http://excelearthings.net/exceltest/";//test
  /*private static String SERVER = "http://192.168.0.11/ilajayur/";*/
    private static String SERVER = "http://192.168.0.11/amritalife/";
///*  private static String SERVER = "https://ayurwarecrm.com/ilajayur/";*/

  public static String LOGIN_URL = SERVER + "index.php/ajax/do_login_new";
  public static String ORDER_URL = SERVER + "index.php/ajax/order_new";
  public static String SET_TASK_STATUS = SERVER + "index.php/ajax/set_task_status";
  public static String GET_APP_DATA = SERVER + "index.php/ajax/get_app_data";
  public static String SAVE_CALL = SERVER + "index.php/ajax/save_call";
  public static String SAVE_EXPENSE = SERVER + "index.php/ajax/save_expense";
  public static String CHECK_USER_NAME = SERVER + "check_user_name";
  public static String UPLOAD_URL = SERVER + "index.php/ajax/upload";
  public static String RESET_PASSWORD = SERVER + "index.php/ajax/change_password";
  public static String SAVE_LEAD = SERVER + "index.php/ajax/save_lead";
  public static String SAVE_MESSAGE = SERVER + "index.php/ajax/save_message";
  public static String SAVE_LOCATION = SERVER + "index.php/ajax/save_location";
  public static String SAVE_COLLECTION = SERVER + "index.php/ajax/save_collection";
  public static String SAVE_CALL_COMMENT = SERVER + "index.php/ajax/save_call_comment";
  public static String SAVE_LEAD_COMMENT = SERVER + "index.php/ajax/save_lead_comment";
  public static String SAVE_TASK_COMMENT = SERVER + "index.php/ajax/save_task_comment";
  public static String SAVE_LOGIN = SERVER + "";
  public static String SAVE_CUSTOMER_IMAGE = SERVER + "index.php/ajax/save_customer_image";
  public static String SAVE_EXCEPTION = SERVER + "index.php/ajax/save_exception";
  public static String SAVE_ADDITIONAL_CUSTOMER_DETAILS =
      SERVER + "index.php/ajax/save_additional_customer_details";
  public static final String JuniorRep = "junior";
  public static final String Senior_Rep = "senior";
}
