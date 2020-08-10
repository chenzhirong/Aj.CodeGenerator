package ${basePackage}.${moduleName};
import com.aj.frame.beans.AJFrameNamedBeanAbstract;
<#if (table.hasDateColumn)>
import java.util.Date;
</#if>
import java.lang.Long;

 /**
 * @Title:  ${table.remarks}
 * @Description:${table.remarks}
 * @author: chenzhirong
 * @date:${.now}
 * @version V1.0
 */
public class ${table.className?substring(1)} extends AJFrameNamedBeanAbstract {

<#list table.primaryKeys as key>
    /**
     *   ${key.remarks}
     */
    private ${key.javaType} ${key.javaProperty};
</#list>

<#list table.baseColumns as column>
    /**
     *  ${column.remarks}
     */
<#if ( column.javaProperty == "state" )>
	private Byte ${column.javaProperty};
<#elseif (column.isIntegerNumber() && column.size == 1)>
	private Boolean ${column.javaProperty};
<#else>
	private ${column.javaType} ${column.javaProperty};
</#if>
</#list>

<#list table.primaryKeys as key>

    public ${key.javaType} ${key.getterMethodName}(){
        return this.${key.javaProperty};
    }
    public void ${key.setterMethodName}(${key.javaType} ${key.javaProperty}){
        this.${key.javaProperty} = ${key.javaProperty};
    }
</#list>

<#list table.baseColumns as column>
<#if ( column.javaProperty == "state")>
	public Byte ${column.getterMethodName}(){
        return this.${column.javaProperty};
    }
    public void ${column.setterMethodName}(Byte ${column.javaProperty}){
        this.${column.javaProperty} = ${column.javaProperty};
    }
<#elseif (column.isIntegerNumber() && column.size == 1)>
    public Boolean ${column.getterMethodName}(){
        return this.${column.javaProperty};
    }
    public void ${column.setterMethodName}(Boolean ${column.javaProperty}){
        this.${column.javaProperty} = ${column.javaProperty};
    }
<#else>
	public ${column.javaType} ${column.getterMethodName}(){
        return this.${column.javaProperty};
    }
    public void ${column.setterMethodName}(${column.javaType} ${column.javaProperty}){
        this.${column.javaProperty} = ${column.javaProperty};
    }
</#if>
</#list>

    @Override
    public String[] importantFieldValues() {
    return new String[] {this.getClass().getSimpleName(), "" + this.getObjId()};
    }
}
