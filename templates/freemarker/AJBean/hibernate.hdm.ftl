<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">

<hibernate-mapping>
    <class name="${basePackage}.${moduleName}.${table.className?substring(1)}" table="${table.tableName}">
        <comment>${table.tableAlias}</comment>
        <id name="objId" type="java.lang.Long">
            <column name="OBJ_ID" precision="16" scale="0" />
            <generator class="com.aj.frame.db.hibernate.util.MySqlSequence">
                <param name="sequence">S_COMMON_PKID_USM</param>
                <param name="table">T_MYSQL_SEQUENCES</param>
            </generator>
        </id>
<#list table.baseColumns as column>
      <#if (column.isDate())>
         <property name="${column.javaProperty}" type="java.util.Date">
            <column name="${column.columnName}" length="${column.size}">
                <comment>${column.remarks}</comment>
            </column>
        </property>
     </#if>
     <#if (column.isString())>
         <property name="${column.javaProperty}" type="java.lang.String">
            <column name="${column.columnName}" length="${column.size}">
                <comment>${column.remarks}</comment>
            </column>
        </property>
     </#if>
     <#if (column.isIntegerNumber())>
	     <#if (column.javaProperty == "state" )>
		 <property name="${column.javaProperty}" type="java.lang.Byte">
            <column name="${column.columnName}" length="${column.size}">
                <comment>${column.remarks}</comment>
            </column>
        </property>
		<#elseif (column.isIntegerNumber() && column.size == 1)>
		 <property name="${column.javaProperty}" type="java.lang.Boolean">
            <column name="${column.columnName}" length="${column.size}">
                <comment>${column.remarks}</comment>
            </column>
        </property>
		<#else>
		<property name="${column.javaProperty}" type="java.lang.Long">
            <column name="${column.columnName}" length="${column.size}">
                <comment>${column.remarks}</comment>
            </column>
        </property>
		</#if>
	 </#if>
</#list>
    </class>
</hibernate-mapping>
