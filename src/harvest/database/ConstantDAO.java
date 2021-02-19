package harvest.database;

public class ConstantDAO {

    //*************************************
    //Database table columns name for individual hours UI and model
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
    //Database table columns name for individual work UI and model
    //*************************************
    /*public static final String TABLE_HARVEST_WORK = "harvest_individual";
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
    public static final String COLUMN_HARVEST_WORK_REMARQUE = "remarque";*/

    //*************************************
    //Database table columns name for group work UI and model
    //*************************************
    public static final String TABLE_HARVEST = "harvest";
    public static final String COLUMN_HARVEST_ID = "id";
    public static final String COLUMN_HARVEST_DATE = "date";
    public static final String COLUMN_HARVEST_AQ = "all_quantity";
    public static final String COLUMN_HARVEST_BQ = "bad_quantity";
    public static final String COLUMN_HARVEST_PQ = "penalty_qty";
    public static final String COLUMN_HARVEST_GPQ = "general_plt_qty";
    public static final String COLUMN_HARVEST_GQ = "good_quantity";
    public static final String COLUMN_HARVEST_PRICE = "price";
    public static final String COLUMN_HARVEST_EMPLOYEE_ID = "employee_id";
    public static final String COLUMN_HARVEST_EMPLOYEE_NAME = "employee_name";
    public static final String COLUMN_HARVEST_TRANSPORT_ID = "transport_id";
    public static final String COLUMN_HARVEST_TRANSPORT_AMOUNT = "transport_amount";
    public static final String COLUMN_HARVEST_CREDIT_ID = "credit_id";
    public static final String COLUMN_HARVEST_CREDIT_AMOUNT = "credit_amount";
    public static final String COLUMN_HARVEST_FARM_ID = "credit_id";
    public static final String COLUMN_HARVEST_FARM_NAME = "credit_amount";
    public static final String COLUMN_HARVEST_NET_AMOUNT = "net_amount";
    public static final String COLUMN_HARVEST_HARVEST_REMARQUE = "remarque";
    public static final String COLUMN_HARVEST_TYPE = "harvest_type";
    public static final String COLUMN_HARVEST_PRODUCTION_ID = "production_id";

    //*************************************
    //Database table columns name for production UI and model
    //*************************************
    public static final String TABLE_PRODUCTION = "production";
    public static final String COLUMN_PRODUCTION_ID = "id";
    public static final String COLUMN_PRODUCTION_DATE = "date";
    public static final String COLUMN_PRODUCTION_SUPPLIER_ID = "supplier_id";
    public static final String COLUMN_PRODUCTION_SUPPLIER_NAME = "supplier_name";
    public static final String COLUMN_PRODUCTION_FARM_ID = "farm_id";
    public static final String COLUMN_PRODUCTION_FARM_NAME = "farm_name";
    public static final String COLUMN_PRODUCTION_PRODUCT_ID = "product_id";
    public static final String COLUMN_PRODUCTION_PRODUCT_NAME = "product_name";
    public static final String COLUMN_PRODUCTION_PRODUCT_CODE = "product_code";
    public static final String COLUMN_PRODUCTION_TOTAL_EMPLOYEES = "total_employees";
    public static final String COLUMN_PRODUCTION_GOOD_QUANTITY = "good_quantity";
    public static final String COLUMN_PRODUCTION_PRICE = "production_price";
    public static final String COLUMN_PRODUCTION_COST = "production_cost";

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
    public static final String COLUMN_TRANSPORT_EMPLOYEE_NAME = "employee_name";
    public static final String COLUMN_TRANSPORT_FARM_ID = "farm_id";
    public static final String COLUMN_TRANSPORT_FARM_NAME = "farm_name";

    //*************************************
    //Database table columns name for credit UI and model
    //*************************************
    public static final String TABLE_CREDIT = "credit";
    public static final String COLUMN_CREDIT_ID = "id";
    public static final String COLUMN_CREDIT_DATE = "date";
    public static final String COLUMN_CREDIT_AMOUNT = "amount";
    public static final String COLUMN_CREDIT_EMPLOYEE_ID = "employee_id";
    public static final String COLUMN_CREDIT_EMPLOYEE_NAME = "employee_name";

    //*************************************
    //Database table columns name for supplier UI and model
    //*************************************
    public static final String TABLE_SUPPLIER = "supplier";
    public static final String COLUMN_SUPPLIER_ID = "id";
    public static final String COLUMN_SUPPLIER_NAME = "company_name";
    public static final String COLUMN_SUPPLIER_FIRSTNAME = "firstname";
    public static final String COLUMN_SUPPLIER_LASTNAME = "lastname";

    //*************************************
    //Database table columns name for supplier UI and model
    //*************************************
    public static final String TABLE_SUPPLY = "supply";
    public static final String COLUMN_SUPPLY_ID = "id";
    public static final String COLUMN_SUPPLY_FRGN_KEY_SUPPLIER_ID = "supplier_id";
    public static final String COLUMN_SUPPLY_FRGN_KEY_FARM_ID = "farm_id";
    public static final String COLUMN_SUPPLY_FRGN_KEY_PRODUCT_ID = "product_id";

    //*************************************
    //Database table columns name for farm UI and model
    //*************************************
    public static final String TABLE_FARM = "farm";
    public static final String COLUMN_FARM_ID = "id";
    public static final String COLUMN_FARM_NAME = "name";
    public static final String COLUMN_FARM_ADDRESS = "address";

    //*************************************
    //Database table columns name for farm UI and model
    //*************************************
    public static final String TABLE_SEASON = "season";
    public static final String COLUMN_SEASON_ID = "id";
    public static final String COLUMN_SEASON_DATE_PLANTING = "planting";
    public static final String COLUMN_SEASON_DATE_HARVEST = "harvest";
    public static final String COLUMN_SEASON_FARM_ID = "farm_id";

    //*************************************
    //Product table Database  columns names
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
    public static final String COLUMN_PRODUCT_PRICE_EMPLOYEE = "price_employee";
    public static final String COLUMN_PRODUCT_PRICE_COMPANY = "price_company";
    public static final String COLUMN_FOREIGN_KEY_PRODUCT_ID = "product_id";

    //*************************************
    //Database table columns name for product detail UI and model
    //*************************************
    public static final String TABLE_PREFERENCE = "preferences";
    public static final String COLUMN_PREFERENCE_PENALTY = "penalty_quantity";
    public static final String COLUMN_PREFERENCE_GENERAL_PENALTY = "general_penalty_quantity";
    public static final String COLUMN_PREFERENCE_HOUR_PRICE = "hour_price";
    public static final String COLUMN_PREFERENCE_TRANSPORT_PRICE = "transport_price";

}
