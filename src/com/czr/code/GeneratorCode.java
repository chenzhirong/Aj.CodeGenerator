package com.czr.code;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.JDBC4Connection;
import org.apache.log4j.Logger;
import org.joy.config.Configuration;
import org.joy.config.TypeMapping;
import org.joy.config.model.DatabaseElement;
import org.joy.config.model.TemplateElement;
import org.joy.db.Database;
import org.joy.db.DatabaseFactory;
import org.joy.db.model.Table;
import org.joy.exception.ApplicationException;
import org.joy.generator.Generator;
import org.joy.generator.engine.FreeMarkerImpl;
import org.joy.generator.engine.TemplateEngineException;
import org.joy.util.ClassloaderUtility;
import org.joy.util.ObjectFactory;
import org.joy.util.StringUtil;
/**
 * 一键生成代码 
 * @author chenzhirong
 *
 */
public class GeneratorCode {
	
	private static final Logger LOGGER = Logger.getLogger(Generator.class);

    public static final int IDX_COLUMN_JAVATYPE = 2;
    public static final int IDX_COLUMN_NULLABLE = 8;
    public static final int IDX_COLUMN_DISPLAY = 10;
    public static final int IDX_COLUMN_SEARCHABLE = 11;
    public static final int IDX_COLUMN_DICT = 12;
    public static final int IDX_COLUMN_REMARK = 13;

    private Configuration configuration;
    private TypeMapping typeMapping;
    private transient Connection connection;
    private Table tableModel;
    private String classPath;
    
    //连接数据库信息
    private String name;
    
	
	public static void main(String[] args) {
		GeneratorCode generator = new GeneratorCode();
    	generator.initSettings();
    	generator.getTonnection();
    	//提交远程测试
		generator.generatorCode();
	}
	
	private List<String> tableNames(String schema) {
		 List<String> tableNameList = new ArrayList<String>();
	        String schemaPattern = null;
	        try {
	            if (StringUtil.isNotEmpty(schema)) {
	                schemaPattern = schema;
	            }
	            ResultSet rs = connection.getMetaData().getTables(null, schemaPattern, "%", null);
	            while (rs.next()) {
	                String tableSchema = rs.getString(getSchemaIndex());
	                String tableName = rs.getString(3);
	                if ("TABLE".equalsIgnoreCase(rs.getString(4))) {
	                	if (StringUtil.isNotEmpty(tableSchema)) {
	                		tableNameList.add(tableName);
	                		LOGGER.info(String.format("tableName=%s", tableName));
//	                		if(tableSchema.startsWith(prefix)) {
//	                			
//	                		}
	                	}
	                }
	            }
	            rs.close();
	        } catch (SQLException e) {
	            LOGGER.info(e.getMessage(), e);
	        }
	        return tableNameList;
	    }
    
    /**
     * 获取DB连接
     */
    public void getTonnection() {
    	
    	List<DatabaseElement> databaseElements = configuration.getConnectionHistory();
    	for (DatabaseElement databaseElement : databaseElements) {
    		try {
    			connection = databaseElement.connect();
    			name = databaseElement.getName();
    			generatorCode();
			} catch (ApplicationException e) {
				e.printStackTrace();
			}
		}
    	
      
    }
    
    public void generatorCode() {
    	  try {
          	Database db = DatabaseFactory.createDatabase(connection, typeMapping);
          	List<String> tableNames = tableNames(name);
          	for (String tableName : tableNames) {
          		if(!tableName.toUpperCase().startsWith("T_")) {
          			continue;
          		}
          		LOGGER.info(String.format("生成tableName文件=%s", tableName));
          		tableModel = db.getTable(getCatalog(),name, tableName);
          		List<TemplateElement> templateElements = configuration.getTemplates();
          		FreeMarkerImpl fmi = new FreeMarkerImpl("");
          		for (TemplateElement templateElement : templateElements) {
          			Map<String, Object> model = new HashMap<String, Object>();
          			model.put("targetProject", configuration.getTargetProject());
          			LOGGER.debug(String.format("targetProject=%s",  configuration.getTargetProject()));
          			model.put("basePackage", configuration.getBasePackage());
          			LOGGER.debug(String.format("basePackage=%s", configuration.getBasePackage()));
          			model.put("moduleName", configuration.getModuleName());
          			LOGGER.debug(String.format("moduleName=%s", configuration.getModuleName()));
          			model.put("table", tableModel);
          			fmi.processToFile(model, templateElement);
  				}
  			}
          	
  		} catch (SQLException e) {
  			e.printStackTrace();
  		} catch (TemplateEngineException e) {
  			e.printStackTrace();
  		}
    }
    
    /**
     * 初始化设置
     */
    private void initSettings() {
        File f = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
        LOGGER.info(getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
        classPath = f.getParentFile().getPath() + File.separator;
        classPath = classPath.replaceAll("%20", " ");

        configuration = new Configuration("./");
        try {
            configuration.loadConfiguration();
            if (!configuration.getClassPathEntries().isEmpty()) {
                ClassLoader classLoader = ClassloaderUtility.getCustomClassloader("./",
                        configuration.getClassPathEntries());
                ObjectFactory.addExternalClassLoader(classLoader);
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage(), e);
        }

        typeMapping = new TypeMapping("./");
        try {
            typeMapping.loadMappgin();
        } catch (Exception e) {
            LOGGER.info(e.getMessage(), e);
        }
    }

    private int getSchemaIndex() {
		try {
			String productName = connection.getMetaData().getDatabaseProductName().toLowerCase();
			if (productName.contains("mysql")) {
				return 1;
			}
			else if (productName.contains("oracle")) {
				return 2;
			}
			else {
				return 2;
			}
		} catch (SQLException throwables) {
			return 2;
		}
	}


	private String getCatalog() {
		try {
			String productName = connection.getMetaData().getDatabaseProductName().toLowerCase();
			if (productName.contains("mysql")) {
				return "";
			}
			else if (productName.contains("oracle")) {
				return "catalog";
			}
			else {
				return "catalog";
			}
		} catch (SQLException throwables) {
			return "catalog";
		}
	}

}
