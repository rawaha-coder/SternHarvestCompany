package harvest.database;

public class ConstantDAO {

    //*************************************
    //Database table columns name for individual hours UI and model
    //*************************************
    public static final String TABLE_HOURS = "hours";
    public static final String COLUMN_HOURS_ID = "id";
    public static final String COLUMN_HOURS_DATE = "date";
    public static final String COLUMN_HOURS_SM = "start_morning";
    public static final String COLUMN_HOURS_EM = "end_morning";
    public static final String COLUMN_HOURS_SN = "start_noon";
    public static final String COLUMN_HOURS_EN = "end_noon";
    public static final String COLUMN_HOURS_EMPLOYEE_TYPE = "employee_type";
    public static final String COLUMN_HOURS_EMPLOYEE_ID = "employee_id";
    public static final String COLUMN_HOURS_TRANSPORT_ID = "transport_id";
    public static final String COLUMN_HOURS_CREDIT_ID = "credit_id";
    public static final String COLUMN_HOURS_PRICE = "hour_price";
    public static final String COLUMN_HOURS_REMARQUE = "remarque";
    public static final String COLUMN_HOURS_PRODUCTION_ID = "production_id";

    //*************************************
    //Database table columns name for individual hours UI and model
    //*************************************
    public static final String TABLE_QUANTITY = "quantity";
    public static final String COLUMN_QUANTITY_ID = "id";
    public static final String COLUMN_QUANTITY_PRODUCTION_ID = "production_id";
    public static final String COLUMN_QUANTITY_DATE = "date";
    public static final String COLUMN_QUANTITY_ALL = "all_quantity";
    public static final String COLUMN_QUANTITY_BAD = "bad_quantity";
    public static final String COLUMN_QUANTITY_PG = "penalty_general";
    public static final String COLUMN_QUANTITY_DG = "damage_general";
    public static final String COLUMN_QUANTITY_GOOD = "good_quantity";
    public static final String COLUMN_QUANTITY_HARVEST_TYPE = "harvest_type";
    public static final String COLUMN_QUANTITY_EMPLOYEE_ID = "employee_id";
    public static final String COLUMN_QUANTITY_TRANSPORT_ID = "transport_id";
    public static final String COLUMN_QUANTITY_CREDIT_ID = "credit_id";
    public static final String COLUMN_QUANTITY_PRICE = "price";
    public static final String COLUMN_QUANTITY_REMARQUE = "remarque";


    //*************************************
    //Database table columns name for group work UI and model
    //*************************************
    public static final String TABLE_HARVEST = "harvest";
    public static final String COLUMN_HARVEST_ID = "id";
    public static final String COLUMN_HARVEST_DATE = "date";
    public static final String COLUMN_HARVEST_AQ = "all_quantity";
    public static final String COLUMN_HARVEST_DQ = "defective_quantity";
    public static final String COLUMN_HARVEST_PG = "penalty_general";
    public static final String COLUMN_HARVEST_DG = "defective_general";
    public static final String COLUMN_HARVEST_GQ = "good_quantity";
    public static final String COLUMN_HARVEST_PRICE = "price";
    public static final String COLUMN_HARVEST_EMPLOYEE_ID = "employee_id";
    public static final String COLUMN_HARVEST_EMPLOYEE_NAME = "employee_name";
    public static final String COLUMN_HARVEST_TRANSPORT_ID = "transport_id";
    public static final String COLUMN_HARVEST_TRANSPORT_AMOUNT = "transport_amount";
    public static final String COLUMN_HARVEST_CREDIT_ID = "credit_id";
    public static final String COLUMN_HARVEST_CREDIT_AMOUNT = "credit_amount";
    public static final String COLUMN_HARVEST_FARM_ID = "farm_id";
    public static final String COLUMN_HARVEST_FARM_NAME = "farm_name";
    public static final String COLUMN_HARVEST_NET_AMOUNT = "net_amount";
    public static final String COLUMN_HARVEST_REMARQUE = "remarque";
    public static final String COLUMN_HARVEST_TYPE = "harvest_type";
    public static final String COLUMN_HARVEST_PRODUCTION_ID = "production_id";

    //*************************************
    //Database table columns name for production UI and model
    //*************************************
    public static final String TABLE_PRODUCTION = "production";
    public static final String COLUMN_PRODUCTION_ID = "id";
    public static final String COLUMN_PRODUCTION_TYPE = "production_type";
    public static final String COLUMN_PRODUCTION_DATE = "date";
    public static final String COLUMN_PRODUCTION_SUPPLIER_ID = "supplier_id";
    public static final String COLUMN_PRODUCTION_FARM_ID = "farm_id";
    public static final String COLUMN_PRODUCTION_PRODUCT_ID = "product_id";
    public static final String COLUMN_PRODUCTION_PRODUCT_DETAIL_ID = "product_detail_id";
    public static final String COLUMN_PRODUCTION_TOTAL_EMPLOYEES = "total_employees";
    public static final String COLUMN_PRODUCTION_TOTAL_QUANTITY = "total_quantity";
    public static final String COLUMN_PRODUCTION_TOTAL_MINUTES = "total_hours";
    public static final String COLUMN_PRODUCTION_PRICE = "price";

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
    public static final String COLUMN_EMPLOYEE_IS_EXIST = "is_exist";

    //*************************************
    //Database table columns name for transport UI and model
    //*************************************
    public static final String TABLE_TRANSPORT = "transport";
    public static final String COLUMN_TRANSPORT_ID = "id";
    public static final String COLUMN_TRANSPORT_DATE = "date";
    public static final String COLUMN_TRANSPORT_AMOUNT = "amount";
    public static final String COLUMN_TRANSPORT_EMPLOYEE_ID = "employee_id";
    public static final String COLUMN_TRANSPORT_FARM_ID = "farm_id";
    public static final String COLUMN_TRANSPORT_IS_EXIST = "is_exist";

    //*************************************
    //Database table columns name for credit UI and model
    //*************************************
    public static final String TABLE_CREDIT = "credit";
    public static final String COLUMN_CREDIT_ID = "id";
    public static final String COLUMN_CREDIT_DATE = "date";
    public static final String COLUMN_CREDIT_AMOUNT = "amount";
    public static final String COLUMN_CREDIT_EMPLOYEE_ID = "employee_id";
    public static final String COLUMN_CREDIT_IS_EXIST = "is_exist";

    //*************************************
    //Database table columns name for supplier UI and model
    //*************************************
    public static final String TABLE_SUPPLIER = "supplier";
    public static final String COLUMN_SUPPLIER_ID = "id";
    public static final String COLUMN_SUPPLIER_NAME = "company_name";
    public static final String COLUMN_SUPPLIER_FIRSTNAME = "firstname";
    public static final String COLUMN_SUPPLIER_LASTNAME = "lastname";
    public static final String COLUMN_SUPPLIER_IS_EXIST = "is_exist";

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
    public static final String COLUMN_FARM_IS_EXIST = "is_exist";

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
    public static final String COLUMN_PRODUCT_IS_EXIST = "is_exist";

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
    public static final String COLUMN_PRODUCT_DETAIL_IS_EXIST = "is_exist";

    //*************************************
    //Database table columns name for product detail UI and model
    //*************************************
    public static final String TABLE_PREFERENCE = "preferences";
    public static final String COLUMN_PREFERENCE_PENALTY_GENERAL = "penalty_general";
    public static final String COLUMN_PREFERENCE_DAMAGE_GENERAL = "damage_general";
    public static final String COLUMN_PREFERENCE_HOUR_PRICE = "hour_price";
    public static final String COLUMN_PREFERENCE_TRANSPORT_PRICE = "transport_price";

}
