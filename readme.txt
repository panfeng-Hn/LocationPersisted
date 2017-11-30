1、配置：
	1.1、时间配置（最多五个）
	1.2、地点配置（最多五个）
	1.3、频率配置（与时间配置互斥）
	
	配置表	config （每次只能启用一种配置，启用多种配置，自动将其他配置置为停用状态）
	表字段  config_id automatic integer 配置ID 
					record_time timestamp 记录时间
					location_id integer 位置ID
					record_frequence integer 记录频率
					is_enable boolean 是否启用
					last_update timestamp 最后更新配置时间
					
2、记录	：（只记录一个月的地点记录）
	位置记录表  location_record
	表字段  location_id automatic integer 位置ID
					location_X text 位置X
					location_Y text 位置Y
					is_late boolean 是否迟到（是否早退）
					record_time timestamp 记录时间
					
3、				
					
	
					
					