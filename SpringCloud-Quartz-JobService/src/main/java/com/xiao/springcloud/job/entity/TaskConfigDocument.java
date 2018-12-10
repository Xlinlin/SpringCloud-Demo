package com.xiao.springcloud.job.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TaskConfigDocument
{

	private String id;


	/** 任务说明 **/
	private String description;

	/** 是否锁定(0:否;1:是) **/
	private Integer isLock = 0;


	/** 任务名称 **/
	private String name;

	/** 任务执行URL **/
	private String url;

	/** 模块 ModelEnum定义 **/
	private String module;

	/**
	 * 运行状态: 0 停止 1运行 2删除
	 */
	private Integer status;

	/** 创建人 **/
	private String creator;

	/** 创建时间 **/
	private String createTime;

	/** 更新时间 **/
	private String updateTime;

	/** 最后更新人 **/
	private String updater;

	/** 运行频次 **/
	private String cronExp;

	/** 是否自动启动：0否 1是 **/

	private Integer autoStart;

	/** 最新启动时间：启动时候设置**/

	private String latestStartTime;

	/** 最后运行时间：停止时候设置**/
	private String lastRunTime;
 

}