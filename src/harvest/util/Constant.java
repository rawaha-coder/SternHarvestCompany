package harvest.util;

public class Constant {

    //*************************************
    //Database table columns name for harvest hours UI and model
    //*************************************
    public static final String TABLE_HARVEST_HOURS = "harvest_hours";
    public static final String COLUMN_HARVEST_HOURS_ID = "id";
    public static final String COLUMN_HARVEST_HOURS_DATE = "date";
    public static final String COLUMN_HARVEST_HOURS_SM = "start_morning";
    public static final String COLUMN_HARVEST_HOURS_EM = "end_morning";
    public static final String COLUMN_HARVEST_HOURS_SN = "start_noon";
    public static final String COLUMN_HARVEST_HOURS_EN = "end_noon";
    public static final String COLUMN_HARVEST_HOURS_HARVEST_ID = "harvest_id";
    public static final String COLUMN_HARVEST_HOURS_TYPE = "employee_type";
    public static final String COLUMN_HARVEST_HOURS_EMPLOYEE_ID = "employee_id";
    public static final String COLUMN_HARVEST_HOURS_CREDIT_ID = "credit_id";
    public static final String COLUMN_HARVEST_HOURS_TRANSPORT_ID = "transport_id";
    public static final String COLUMN_HARVEST_REMARQUE = "remarque";

    //*************************************
    //Database table columns name for harvest work UI and model
    //*************************************
    public static final String TABLE_HARVEST_WORK = "harvest_individual";
    public static final String COLUMN_HARVEST_WORK_ID = "id";
    public static final String COLUMN_HARVEST_WORK_DATE = "date";
    public static final String COLUMN_HARVEST_WORK_AQ = "all_quantity";
    public static final String COLUMN_HARVEST_WORK_BQ = "bad_quantity";
    public static final String COLUMN_HARVEST_WORK_GQ = "good_quantity";
    public static final String COLUMN_HARVEST_WORK_PRICE = "price";
    public static final String COLUMN_HARVEST_WORK_NET_AMOUNT = "net_amount";
    public static final String COLUMN_HARVEST_WORK_HARVEST_ID = "harvest_id";
    public static final String COLUMN_HARVEST_WORK_TYPE = "harvest_type";
    public static final String COLUMN_HARVEST_WORK_EMPLOYEE_ID = "employee_id";
    public static final String COLUMN_HARVEST_WORK_CREDIT_ID = "credit_id";
    public static final String COLUMN_HARVEST_WORK_TRANSPORT_ID = "transport_id";
    public static final String COLUMN_HARVEST_WORK_REMARQUE = "remarque";


    //*************************************
    //Database table columns name for harvest production UI and model
    //*************************************
    public static final String TABLE_HARVEST_PRODUCTION = "harvest_production";
    public static final String COLUMN_HARVEST_PRODUCTION_ID = "id";
    public static final String COLUMN_HARVEST_PRODUCTION_DATE = "date";
    public static final String COLUMN_HARVEST_PRODUCTION_TYPE = "harvest_type";
    public static final String COLUMN_HARVEST_PRODUCTION_TE  = "total_employees";
    public static final String COLUMN_HARVEST_PRODUCTION_TH = "total_hours";
    public static final String COLUMN_HARVEST_PRODUCTION_TQ  = "total_quantity";
    public static final String COLUMN_HARVEST_PRODUCTION_TA  = "total_amount";
    public static final String COLUMN_HARVEST_PRODUCTION_TT = "total_transports";
    public static final String COLUMN_HARVEST_PRODUCTION_TC  = "total_credits";
    public static final String COLUMN_HARVEST_PRODUCTION_SUPPLIER_ID = "supplier_id";
    public static final String COLUMN_HARVEST_PRODUCTION_PRODUCT_ID = "product_id";
    public static final String COLUMN_HARVEST_PRODUCTION_PRODUCT_DETAIL_ID = "product_detail_id";
    public static final String COLUMN_HARVEST_PRODUCTION_FARM_ID = "farm_id";

    //*************************************
    //Database table columns name for employee UI and model
    //*************************************
    public static final String TABLE_EMPLOYEE = "employee";
    public static final String COLUMN_EMPLOYEE_ID = "id";
    public static final String COLUMN_EMPLOYEE_STATUS = "status";
    public static final String COLUMN_EMPLOYEE_FIRST_NAME = "first_name";
    public static final String COLUMN_EMPLOYEE_LAST_NAME = "last_name";
    public static final String COLUMN_EMPLOYEE_HIRE_DATE = "hire_date";
    public static final String COLUMN_EMPLOYEE_FIRE_DATE = "fire_date";
    public static final String COLUMN_EMPLOYEE_PERMISSION_DATE = "permission_date";

    //*************************************
    //Database table columns name for transport UI and model
    //*************************************
    public static final String TABLE_TRANSPORT = "transport";
    public static final String COLUMN_TRANSPORT_ID = "id";
    public static final String COLUMN_TRANSPORT_DATE = "date";
    public static final String COLUMN_TRANSPORT_AMOUNT = "amount";
    public static final String COLUMN_TRANSPORT_EMPLOYEE_ID = "employee_id";
    public static final String COLUMN_TRANSPORT_FARM_ID = "farm_id";

    //*************************************
    //Database table columns name for credit UI and model
    //*************************************
    public static final String TABLE_CREDIT = "credit";
    public static final String COLUMN_CREDIT_ID = "id";
    public static final String COLUMN_CREDIT_DATE = "date";
    public static final String COLUMN_CREDIT_AMOUNT = "amount";
    public static final String COLUMN_CREDIT_EMPLOYEE_ID = "employee_id";

    //*************************************
    //Database table columns name for supplier UI and model
    //*************************************
    public static final String TABLE_SUPPLIER = "supplier";
    public static final String COLUMN_SUPPLIER_ID = "id";
    public static final String COLUMN_SUPPLIER_NAME = "company_name";
    public static final String COLUMN_SUPPLIER_FIRSTNAME = "firstname";
    public static final String COLUMN_SUPPLIER_LASTNAME = "lastname";

    //*************************************
    //Database table columns name for farm UI and model
    //*************************************
    public static final String TABLE_FARM = "farm";
    public static final String COLUMN_FARM_ID = "id";
    public static final String COLUMN_FARM_NAME = "name";
    public static final String COLUMN_FARM_ADDRESS = "address";

    //*************************************
    //Database table columns name for product UI and model
    //*************************************
    public static final String TABLE_PRODUCT = "product";
    public static final String COLUMN_PRODUCT_ID = "id";
    public static final String COLUMN_PRODUCT_NAME = "name";

    //*************************************
    //Database table columns name for product detail UI and model
    //*************************************
    public static final String TABLE_PRODUCT_DETAIL = "product_detail";
    public static final String COLUMN_PRODUCT_DETAIL_ID = "id";
    public static final String COLUMN_PRODUCT_TYPE = "type";
    public static final String COLUMN_PRODUCT_CODE = "code";
    public static final String COLUMN_PRODUCT_PRICE_1 = "price_1";
    public static final String COLUMN_PRODUCT_PRICE_2 = "price_2";
    public static final String COLUMN_FOREIGN_KEY_PRODUCT_ID = "product_id";
}
