package com.codi.laolaiqiao.common.jdbc;

import org.hibernate.cfg.ImprovedNamingStrategy;

public class CustomNamingStrategy extends ImprovedNamingStrategy {
    private static final long serialVersionUID = 1L;
    
	@Override
    public String classToTableName(String className){
		return "t_" + super.classToTableName(className).toLowerCase();
	}
	
	@Override
    public String joinKeyColumnName(String joinedColumn, String joinedTable) {
		System.err.println("!!! GEN foreign key for :" + joinedTable + ", column:" + joinedColumn);
		return super.joinKeyColumnName(joinedColumn, joinedTable);
	}
	
	@Override
    public String foreignKeyColumnName(
			String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName
	) {
		System.err.println("!!! GEN foreign key for :" + propertyTableName + ", column:" + propertyName);
		return super.foreignKeyColumnName(propertyName, propertyEntityName, propertyTableName, referencedColumnName);
	}

}
