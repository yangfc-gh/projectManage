package cn.com.project.mybatisGenerate;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 多数据源切换
 * @author Administrator
 *
 */
public class ConnecterHolder extends AbstractRoutingDataSource {
	
	public static final String DS_MYSQL = "ds_mysql";
	public static final String DS_ORACLE = "ds_oracle";
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
	
	/**
	 * 设置数据源
	 * @param customerType
	 */
	public static void setCustomerType(String customerType) {
        contextHolder.set(customerType);
    }

    /**
     * 清除数据源设置，（如同一方法内需访问不同数据源时调用此方法清除上一次设置的数据源）
     */
    public static void clearCustomerType() {
        contextHolder.remove();
    }

    /**
     * 获取数据源
     * @return
     */
    public static String getCustomerType() {
        return contextHolder.get();
    }
	
	/** 
	 * 拦截系统数据源
	 * (non-Javadoc)
	 * @see AbstractRoutingDataSource#determineCurrentLookupKey()
	 */
	@Override
	protected Object determineCurrentLookupKey() {
		return ConnecterHolder.getCustomerType();
	}

}
