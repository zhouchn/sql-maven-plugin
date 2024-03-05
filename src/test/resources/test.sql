
-- MySQL dump 10.13  Distrib 8.0.28, for Linux (x86_64)
--
-- Host: localhost    Database: fate_flow
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `fate_flow`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `fate_flow` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `fate_flow`;

--
-- Table structure for table `componentsummary`
--

DROP TABLE IF EXISTS `componentsummary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `componentsummary` (
  `f_id` bigint NOT NULL AUTO_INCREMENT,
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_job_id` varchar(64) NOT NULL,
  `f_role` varchar(25) NOT NULL,
  `f_party_id` varchar(20) NOT NULL,
  `f_component_name` varchar(50) NOT NULL,
  `f_task_id` varchar(50) DEFAULT NULL,
  `f_task_version` varchar(50) DEFAULT NULL,
  `f_summary` longtext NOT NULL,
  PRIMARY KEY (`f_id`),
  KEY `componentsummary_f_job_id` (`f_job_id`),
  KEY `componentsummary_f_role` (`f_role`),
  KEY `componentsummary_f_party_id` (`f_party_id`),
  KEY `componentsummary_f_task_id` (`f_task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `node_resource`
--

DROP TABLE IF EXISTS `node_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `node_resource` (
  `resource_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `server_node_id` bigint NOT NULL,
  `resource_type` varchar(255) DEFAULT NULL,
  `total` bigint NOT NULL DEFAULT '0',
  `used` bigint NOT NULL DEFAULT '0',
  `pre_allocated` bigint NOT NULL DEFAULT '0',
  `allocated` bigint NOT NULL DEFAULT '0',
  `extention` varchar(512) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`resource_id`),
  UNIQUE KEY `resource_id` (`resource_id`),
  UNIQUE KEY `idx_u_node_resource` (`server_node_id`,`resource_type`),
  KEY `idx_node_id_node_resource` (`server_node_id`),
  KEY `idx_node_status_node_resource` (`server_node_id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `processor_resource`
--

DROP TABLE IF EXISTS `processor_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `processor_resource` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `processor_id` bigint NOT NULL,
  `session_id` varchar(767) DEFAULT NULL,
  `server_node_id` int NOT NULL,
  `resource_type` varchar(255) DEFAULT NULL,
  `allocated` bigint NOT NULL DEFAULT '0',
  `extention` varchar(512) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `pid` int NOT NULL DEFAULT '-1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `idx_processor_id_processor_resource` (`processor_id`),
  KEY `idx_node_id_processor_resource` (`server_node_id`),
  KEY `idx_session_id_processor_resource` (`session_id`),
  KEY `idx_node_status_processor_resource` (`server_node_id`,`resource_type`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `server_node`
--

DROP TABLE IF EXISTS `server_node`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `server_node` (
  `server_node_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(2000) NOT NULL DEFAULT '',
  `server_cluster_id` bigint unsigned NOT NULL DEFAULT '0',
  `host` varchar(1000) NOT NULL,
  `port` int NOT NULL,
  `node_type` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  `last_heartbeat_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`server_node_id`),
  UNIQUE KEY `server_node_id` (`server_node_id`),
  KEY `idx_server_node_h_p_nt` (`host`(600),`port`,`node_type`(100)),
  KEY `idx_server_node_h` (`host`(767)),
  KEY `idx_server_node_sci` (`server_cluster_id`),
  KEY `idx_server_node_nt` (`node_type`),
  KEY `idx_server_node_s` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `session_main`
--

DROP TABLE IF EXISTS `session_main`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `session_main` (
  `session_id` varchar(767) NOT NULL,
  `name` varchar(2000) NOT NULL DEFAULT '',
  `status` varchar(255) NOT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `total_proc_count` int DEFAULT NULL,
  `active_proc_count` int DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`session_id`),
  KEY `idx_session_main_s` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `session_option`
--

DROP TABLE IF EXISTS `session_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `session_option` (
  `session_option_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `session_id` varchar(2000) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `data` varchar(2000) NOT NULL DEFAULT '',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`session_option_id`),
  UNIQUE KEY `session_option_id` (`session_option_id`),
  KEY `idx_session_option_si` (`session_id`(767))
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `session_processor`
--

DROP TABLE IF EXISTS `session_processor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `session_processor` (
  `processor_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `session_id` varchar(767) DEFAULT NULL,
  `server_node_id` int NOT NULL,
  `processor_type` varchar(255) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `command_endpoint` varchar(255) DEFAULT NULL,
  `transfer_endpoint` varchar(255) DEFAULT NULL,
  `processor_option` varchar(512) DEFAULT NULL,
  `pid` int NOT NULL DEFAULT '-1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`processor_id`),
  UNIQUE KEY `processor_id` (`processor_id`),
  KEY `idx_session_processor_si` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `session_ranks`
--

DROP TABLE IF EXISTS `session_ranks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `session_ranks` (
  `container_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `session_id` varchar(767) DEFAULT NULL,
  `server_node_id` int NOT NULL,
  `global_rank` int unsigned NOT NULL,
  `local_rank` int unsigned NOT NULL,
  PRIMARY KEY (`container_id`),
  UNIQUE KEY `container_id` (`container_id`),
  KEY `idx_session_id_session_ranks` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `store_locator`
--

DROP TABLE IF EXISTS `store_locator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store_locator` (
  `store_locator_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `store_type` varchar(255) NOT NULL,
  `namespace` varchar(2000) NOT NULL DEFAULT 'DEFAULT',
  `name` varchar(2000) NOT NULL,
  `path` varchar(2000) NOT NULL DEFAULT '',
  `total_partitions` int unsigned NOT NULL,
  `partitioner` varchar(2000) NOT NULL DEFAULT 'BYTESTRING_HASH',
  `serdes` varchar(2000) NOT NULL DEFAULT '',
  `version` int unsigned NOT NULL DEFAULT '0',
  `status` varchar(255) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`store_locator_id`),
  UNIQUE KEY `store_locator_id` (`store_locator_id`),
  UNIQUE KEY `idx_u_store_locator_ns_n` (`namespace`(120),`name`(640)),
  KEY `idx_store_locator_st` (`store_type`),
  KEY `idx_store_locator_ns` (`namespace`(767)),
  KEY `idx_store_locator_n` (`name`(767)),
  KEY `idx_store_locator_s` (`status`),
  KEY `idx_store_locator_v` (`version`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `store_option`
--

DROP TABLE IF EXISTS `store_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store_option` (
  `store_option_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `store_locator_id` bigint unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `data` varchar(2000) NOT NULL DEFAULT '',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`store_option_id`),
  UNIQUE KEY `store_option_id` (`store_option_id`),
  KEY `idx_store_option_si` (`store_locator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `store_partition`
--

DROP TABLE IF EXISTS `store_partition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store_partition` (
  `store_partition_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `store_locator_id` bigint unsigned NOT NULL,
  `node_id` bigint unsigned NOT NULL,
  `partition_id` int unsigned NOT NULL,
  `status` varchar(255) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`store_partition_id`),
  UNIQUE KEY `store_partition_id` (`store_partition_id`),
  UNIQUE KEY `idx_u_store_partition_si_spi_ni` (`store_locator_id`,`store_partition_id`,`node_id`),
  KEY `idx_store_partition_sli` (`store_locator_id`),
  KEY `idx_store_partition_ni` (`node_id`),
  KEY `idx_store_partition_s` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_cache_record`
--

DROP TABLE IF EXISTS `t_cache_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_cache_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_cache_key` varchar(500) NOT NULL,
  `f_cache` longtext NOT NULL,
  `f_job_id` varchar(64) DEFAULT NULL,
  `f_role` varchar(50) DEFAULT NULL,
  `f_party_id` varchar(20) DEFAULT NULL,
  `f_component_name` text,
  `f_task_id` varchar(100) DEFAULT NULL,
  `f_task_version` bigint DEFAULT NULL,
  `f_cache_name` varchar(50) DEFAULT NULL,
  `t_ttl` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `cacherecord_f_job_id` (`f_job_id`),
  KEY `cacherecord_f_role` (`f_role`),
  KEY `cacherecord_f_party_id` (`f_party_id`),
  KEY `cacherecord_f_task_version` (`f_task_version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_component_info`
--

DROP TABLE IF EXISTS `t_component_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_component_info` (
  `f_component_name` varchar(30) NOT NULL,
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_component_alias` longtext NOT NULL,
  `f_default_provider` varchar(20) NOT NULL,
  `f_support_provider` longtext,
  PRIMARY KEY (`f_component_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_component_provider_info`
--

DROP TABLE IF EXISTS `t_component_provider_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_component_provider_info` (
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_provider_name` varchar(20) NOT NULL,
  `f_version` varchar(10) NOT NULL,
  `f_class_path` longtext NOT NULL,
  `f_path` varchar(128) NOT NULL,
  `f_python` varchar(128) NOT NULL,
  PRIMARY KEY (`f_provider_name`,`f_version`),
  KEY `componentproviderinfo_f_provider_name` (`f_provider_name`),
  KEY `componentproviderinfo_f_version` (`f_version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_component_registry`
--

DROP TABLE IF EXISTS `t_component_registry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_component_registry` (
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_provider_name` varchar(20) NOT NULL,
  `f_version` varchar(10) NOT NULL,
  `f_component_name` varchar(30) NOT NULL,
  `f_module` varchar(128) NOT NULL,
  PRIMARY KEY (`f_provider_name`,`f_version`,`f_component_name`),
  KEY `componentregistryinfo_f_provider_name` (`f_provider_name`),
  KEY `componentregistryinfo_f_version` (`f_version`),
  KEY `componentregistryinfo_f_component_name` (`f_component_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_data_table_tracking`
--

DROP TABLE IF EXISTS `t_data_table_tracking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_data_table_tracking` (
  `f_table_id` bigint NOT NULL AUTO_INCREMENT,
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_table_name` varchar(300) DEFAULT NULL,
  `f_table_namespace` varchar(300) DEFAULT NULL,
  `f_job_id` varchar(64) DEFAULT NULL,
  `f_have_parent` tinyint(1) NOT NULL,
  `f_parent_number` int NOT NULL,
  `f_parent_table_name` varchar(500) DEFAULT NULL,
  `f_parent_table_namespace` varchar(500) DEFAULT NULL,
  `f_source_table_name` varchar(500) DEFAULT NULL,
  `f_source_table_namespace` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`f_table_id`),
  KEY `datatabletracking_f_job_id` (`f_job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_dependencies_storage_meta`
--

DROP TABLE IF EXISTS `t_dependencies_storage_meta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_dependencies_storage_meta` (
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_storage_engine` varchar(30) NOT NULL,
  `f_type` varchar(20) NOT NULL,
  `f_version` varchar(10) NOT NULL,
  `f_storage_path` varchar(256) DEFAULT NULL,
  `f_snapshot_time` bigint DEFAULT NULL,
  `f_fate_flow_snapshot_time` bigint DEFAULT NULL,
  `f_dependencies_conf` longtext,
  `f_upload_status` tinyint(1) NOT NULL,
  `f_pid` int DEFAULT NULL,
  PRIMARY KEY (`f_storage_engine`,`f_type`,`f_version`),
  KEY `dependenciesstoragemeta_f_version` (`f_version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_engine_registry`
--

DROP TABLE IF EXISTS `t_engine_registry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_engine_registry` (
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_engine_type` varchar(10) NOT NULL,
  `f_engine_name` varchar(50) NOT NULL,
  `f_engine_entrance` varchar(50) NOT NULL,
  `f_engine_config` longtext NOT NULL,
  `f_cores` int NOT NULL,
  `f_memory` int NOT NULL,
  `f_remaining_cores` int NOT NULL,
  `f_remaining_memory` int NOT NULL,
  `f_nodes` int NOT NULL,
  PRIMARY KEY (`f_engine_name`,`f_engine_type`),
  KEY `engineregistry_f_engine_type` (`f_engine_type`),
  KEY `engineregistry_f_engine_name` (`f_engine_name`),
  KEY `engineregistry_f_engine_entrance` (`f_engine_entrance`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_job`
--

DROP TABLE IF EXISTS `t_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_job` (
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_user_id` varchar(25) DEFAULT NULL,
  `f_job_id` varchar(64) NOT NULL,
  `f_name` varchar(500) DEFAULT NULL,
  `f_description` text,
  `f_tag` varchar(50) DEFAULT NULL,
  `f_dsl` longtext NOT NULL,
  `f_runtime_conf` longtext NOT NULL,
  `f_runtime_conf_on_party` longtext NOT NULL,
  `f_train_runtime_conf` longtext,
  `f_roles` longtext NOT NULL,
  `f_initiator_role` varchar(50) NOT NULL,
  `f_initiator_party_id` varchar(50) NOT NULL,
  `f_status` varchar(50) NOT NULL,
  `f_status_code` int DEFAULT NULL,
  `f_user` longtext NOT NULL,
  `f_role` varchar(50) NOT NULL,
  `f_party_id` varchar(20) NOT NULL,
  `f_is_initiator` tinyint(1) DEFAULT NULL,
  `f_progress` int DEFAULT NULL,
  `f_ready_signal` tinyint(1) NOT NULL,
  `f_ready_time` bigint DEFAULT NULL,
  `f_cancel_signal` tinyint(1) NOT NULL,
  `f_cancel_time` bigint DEFAULT NULL,
  `f_rerun_signal` tinyint(1) NOT NULL,
  `f_end_scheduling_updates` int DEFAULT NULL,
  `f_engine_name` varchar(50) DEFAULT NULL,
  `f_engine_type` varchar(10) DEFAULT NULL,
  `f_cores` int NOT NULL,
  `f_memory` int NOT NULL,
  `f_remaining_cores` int NOT NULL,
  `f_remaining_memory` int NOT NULL,
  `f_resource_in_use` tinyint(1) NOT NULL,
  `f_apply_resource_time` bigint DEFAULT NULL,
  `f_return_resource_time` bigint DEFAULT NULL,
  `f_inheritance_info` longtext,
  `f_inheritance_status` varchar(50) DEFAULT NULL,
  `f_start_time` bigint DEFAULT NULL,
  `f_start_date` datetime DEFAULT NULL,
  `f_end_time` bigint DEFAULT NULL,
  `f_end_date` datetime DEFAULT NULL,
  `f_elapsed` bigint DEFAULT NULL,
  PRIMARY KEY (`f_job_id`,`f_role`,`f_party_id`),
  KEY `job_f_job_id` (`f_job_id`),
  KEY `job_f_role` (`f_role`),
  KEY `job_f_party_id` (`f_party_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_machine_learning_model_info`
--

DROP TABLE IF EXISTS `t_machine_learning_model_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_machine_learning_model_info` (
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_role` varchar(50) NOT NULL,
  `f_party_id` varchar(20) NOT NULL,
  `f_roles` longtext NOT NULL,
  `f_job_id` varchar(64) NOT NULL,
  `f_model_id` varchar(100) NOT NULL,
  `f_model_version` varchar(100) NOT NULL,
  `f_size` bigint NOT NULL,
  `f_initiator_role` varchar(50) NOT NULL,
  `f_initiator_party_id` varchar(50) NOT NULL,
  `f_runtime_conf` longtext NOT NULL,
  `f_train_dsl` longtext NOT NULL,
  `f_train_runtime_conf` longtext NOT NULL,
  `f_runtime_conf_on_party` longtext NOT NULL,
  `f_inference_dsl` longtext NOT NULL,
  `f_fate_version` varchar(10) DEFAULT NULL,
  `f_parent` tinyint(1) DEFAULT NULL,
  `f_parent_info` longtext NOT NULL,
  `f_loaded_times` int NOT NULL,
  `f_imported` int NOT NULL,
  `f_archive_sha256` varchar(100) DEFAULT NULL,
  `f_archive_from_ip` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`f_role`,`f_party_id`,`f_model_id`,`f_model_version`),
  KEY `machinelearningmodelinfo_f_job_id` (`f_job_id`),
  KEY `machinelearningmodelinfo_f_model_id` (`f_model_id`),
  KEY `machinelearningmodelinfo_f_model_version` (`f_model_version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_model_tag`
--

DROP TABLE IF EXISTS `t_model_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_model_tag` (
  `f_id` bigint NOT NULL AUTO_INCREMENT,
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_m_id` varchar(25) NOT NULL,
  `f_t_id` bigint NOT NULL,
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_pipeline_component_meta`
--

DROP TABLE IF EXISTS `t_pipeline_component_meta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_pipeline_component_meta` (
  `id` int NOT NULL AUTO_INCREMENT,
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_model_id` varchar(100) NOT NULL,
  `f_model_version` varchar(100) NOT NULL,
  `f_role` varchar(50) NOT NULL,
  `f_party_id` varchar(20) NOT NULL,
  `f_component_name` varchar(100) NOT NULL,
  `f_component_module_name` varchar(100) NOT NULL,
  `f_model_alias` varchar(100) NOT NULL,
  `f_model_proto_index` longtext,
  `f_run_parameters` longtext,
  `f_archive_sha256` varchar(100) DEFAULT NULL,
  `f_archive_from_ip` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pipelinecomponentmeta_f_model_id_f_model_version_f_role__3156a89` (`f_model_id`,`f_model_version`,`f_role`,`f_party_id`,`f_component_name`),
  KEY `pipelinecomponentmeta_f_model_id` (`f_model_id`),
  KEY `pipelinecomponentmeta_f_model_version` (`f_model_version`),
  KEY `pipelinecomponentmeta_f_role` (`f_role`),
  KEY `pipelinecomponentmeta_f_party_id` (`f_party_id`),
  KEY `pipelinecomponentmeta_f_component_name` (`f_component_name`),
  KEY `pipelinecomponentmeta_f_model_alias` (`f_model_alias`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_server_registry_info`
--

DROP TABLE IF EXISTS `t_server_registry_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_server_registry_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_server_name` varchar(30) NOT NULL,
  `f_host` varchar(30) NOT NULL,
  `f_port` int NOT NULL,
  `f_protocol` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `serverregistryinfo_f_server_name` (`f_server_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_service_registry_info`
--

DROP TABLE IF EXISTS `t_service_registry_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_service_registry_info` (
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_server_name` varchar(30) NOT NULL,
  `f_service_name` varchar(30) NOT NULL,
  `f_url` varchar(100) NOT NULL,
  `f_method` varchar(10) NOT NULL,
  `f_params` longtext,
  `f_data` longtext,
  `f_headers` longtext,
  PRIMARY KEY (`f_server_name`,`f_service_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_session_record`
--

DROP TABLE IF EXISTS `t_session_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_session_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_engine_session_id` varchar(150) NOT NULL,
  `f_manager_session_id` varchar(150) NOT NULL,
  `f_engine_type` varchar(10) NOT NULL,
  `f_engine_name` varchar(50) NOT NULL,
  `f_engine_address` longtext NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sessionrecord_f_engine_type` (`f_engine_type`),
  KEY `sessionrecord_f_engine_name` (`f_engine_name`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_site_key_info`
--

DROP TABLE IF EXISTS `t_site_key_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_site_key_info` (
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_party_id` varchar(20) NOT NULL,
  `f_key_name` varchar(10) NOT NULL,
  `f_key` longtext NOT NULL,
  PRIMARY KEY (`f_party_id`,`f_key_name`),
  KEY `sitekeyinfo_f_party_id` (`f_party_id`),
  KEY `sitekeyinfo_f_key_name` (`f_key_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_storage_connector`
--

DROP TABLE IF EXISTS `t_storage_connector`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_storage_connector` (
  `f_name` varchar(100) NOT NULL,
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_engine` varchar(100) NOT NULL,
  `f_connector_info` longtext NOT NULL,
  PRIMARY KEY (`f_name`),
  KEY `storageconnectormodel_f_engine` (`f_engine`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_storage_table_meta`
--

DROP TABLE IF EXISTS `t_storage_table_meta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_storage_table_meta` (
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_name` varchar(100) NOT NULL,
  `f_namespace` varchar(100) NOT NULL,
  `f_address` longtext NOT NULL,
  `f_engine` varchar(100) NOT NULL,
  `f_store_type` varchar(50) DEFAULT NULL,
  `f_options` longtext NOT NULL,
  `f_partitions` int DEFAULT NULL,
  `f_id_delimiter` varchar(255) DEFAULT NULL,
  `f_in_serialized` tinyint(1) NOT NULL,
  `f_have_head` tinyint(1) NOT NULL,
  `f_extend_sid` tinyint(1) NOT NULL,
  `f_auto_increasing_sid` tinyint(1) NOT NULL,
  `f_schema` longtext NOT NULL,
  `f_count` bigint DEFAULT NULL,
  `f_part_of_data` longtext NOT NULL,
  `f_origin` varchar(50) NOT NULL,
  `f_disable` tinyint(1) NOT NULL,
  `f_description` text NOT NULL,
  `f_read_access_time` bigint DEFAULT NULL,
  `f_read_access_date` datetime DEFAULT NULL,
  `f_write_access_time` bigint DEFAULT NULL,
  `f_write_access_date` datetime DEFAULT NULL,
  PRIMARY KEY (`f_name`,`f_namespace`),
  KEY `storagetablemetamodel_f_name` (`f_name`),
  KEY `storagetablemetamodel_f_namespace` (`f_namespace`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_tags`
--

DROP TABLE IF EXISTS `t_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_tags` (
  `f_id` bigint NOT NULL AUTO_INCREMENT,
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_name` varchar(100) NOT NULL,
  `f_desc` text,
  PRIMARY KEY (`f_id`),
  UNIQUE KEY `tag_f_name` (`f_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_task`
--

DROP TABLE IF EXISTS `t_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_task` (
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_job_id` varchar(64) NOT NULL,
  `f_component_name` text NOT NULL,
  `f_component_module` varchar(200) NOT NULL,
  `f_task_id` varchar(100) NOT NULL,
  `f_task_version` bigint NOT NULL,
  `f_initiator_role` varchar(50) NOT NULL,
  `f_initiator_party_id` varchar(50) NOT NULL,
  `f_federated_mode` varchar(10) NOT NULL,
  `f_federated_status_collect_type` varchar(10) NOT NULL,
  `f_status` varchar(50) NOT NULL,
  `f_status_code` int DEFAULT NULL,
  `f_auto_retries` int NOT NULL,
  `f_auto_retry_delay` int NOT NULL,
  `f_role` varchar(50) NOT NULL,
  `f_party_id` varchar(20) NOT NULL,
  `f_run_on_this_party` tinyint(1) DEFAULT NULL,
  `f_worker_id` varchar(100) DEFAULT NULL,
  `f_cmd` longtext,
  `f_run_ip` varchar(100) DEFAULT NULL,
  `f_run_port` int DEFAULT NULL,
  `f_run_pid` int DEFAULT NULL,
  `f_party_status` varchar(50) NOT NULL,
  `f_provider_info` longtext NOT NULL,
  `f_component_parameters` longtext NOT NULL,
  `f_engine_conf` longtext,
  `f_kill_status` tinyint(1) NOT NULL,
  `f_error_report` text NOT NULL,
  `f_start_time` bigint DEFAULT NULL,
  `f_start_date` datetime DEFAULT NULL,
  `f_end_time` bigint DEFAULT NULL,
  `f_end_date` datetime DEFAULT NULL,
  `f_elapsed` bigint DEFAULT NULL,
  PRIMARY KEY (`f_job_id`,`f_task_id`,`f_task_version`,`f_role`,`f_party_id`),
  KEY `task_f_job_id` (`f_job_id`),
  KEY `task_f_status` (`f_status`),
  KEY `task_f_role` (`f_role`),
  KEY `task_f_party_id` (`f_party_id`),
  KEY `task_f_run_on_this_party` (`f_run_on_this_party`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_tracking_metric_20231120`
--

DROP TABLE IF EXISTS `t_tracking_metric_20231120`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_tracking_metric_20231120` (
  `f_id` bigint NOT NULL AUTO_INCREMENT,
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_job_id` varchar(64) NOT NULL,
  `f_component_name` varchar(30) NOT NULL,
  `f_task_id` varchar(100) DEFAULT NULL,
  `f_task_version` bigint DEFAULT NULL,
  `f_role` varchar(10) NOT NULL,
  `f_party_id` varchar(20) NOT NULL,
  `f_metric_namespace` varchar(80) NOT NULL,
  `f_metric_name` varchar(80) NOT NULL,
  `f_key` varchar(200) NOT NULL,
  `f_value` longtext NOT NULL,
  `f_type` int NOT NULL,
  PRIMARY KEY (`f_id`),
  KEY `trackingmetric_20231120_f_job_id` (`f_job_id`),
  KEY `trackingmetric_20231120_f_component_name` (`f_component_name`),
  KEY `trackingmetric_20231120_f_role` (`f_role`),
  KEY `trackingmetric_20231120_f_metric_namespace` (`f_metric_namespace`),
  KEY `trackingmetric_20231120_f_metric_name` (`f_metric_name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_tracking_metric_20231121`
--

DROP TABLE IF EXISTS `t_tracking_metric_20231121`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_tracking_metric_20231121` (
  `f_id` bigint NOT NULL AUTO_INCREMENT,
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_job_id` varchar(64) NOT NULL,
  `f_component_name` varchar(30) NOT NULL,
  `f_task_id` varchar(100) DEFAULT NULL,
  `f_task_version` bigint DEFAULT NULL,
  `f_role` varchar(10) NOT NULL,
  `f_party_id` varchar(20) NOT NULL,
  `f_metric_namespace` varchar(80) NOT NULL,
  `f_metric_name` varchar(80) NOT NULL,
  `f_key` varchar(200) NOT NULL,
  `f_value` longtext NOT NULL,
  `f_type` int NOT NULL,
  PRIMARY KEY (`f_id`),
  KEY `trackingmetric_20231121_f_job_id` (`f_job_id`),
  KEY `trackingmetric_20231121_f_component_name` (`f_component_name`),
  KEY `trackingmetric_20231121_f_role` (`f_role`),
  KEY `trackingmetric_20231121_f_metric_namespace` (`f_metric_namespace`),
  KEY `trackingmetric_20231121_f_metric_name` (`f_metric_name`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_worker`
--

DROP TABLE IF EXISTS `t_worker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_worker` (
  `f_worker_id` varchar(100) NOT NULL,
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_worker_name` varchar(50) NOT NULL,
  `f_job_id` varchar(64) NOT NULL,
  `f_task_id` varchar(100) NOT NULL,
  `f_task_version` bigint NOT NULL,
  `f_role` varchar(50) NOT NULL,
  `f_party_id` varchar(20) NOT NULL,
  `f_run_ip` varchar(100) DEFAULT NULL,
  `f_run_pid` int DEFAULT NULL,
  `f_http_port` int DEFAULT NULL,
  `f_grpc_port` int DEFAULT NULL,
  `f_config` longtext,
  `f_cmd` longtext,
  `f_start_time` bigint DEFAULT NULL,
  `f_start_date` datetime DEFAULT NULL,
  `f_end_time` bigint DEFAULT NULL,
  `f_end_date` datetime DEFAULT NULL,
  PRIMARY KEY (`f_worker_id`),
  KEY `workerinfo_f_worker_name` (`f_worker_name`),
  KEY `workerinfo_f_job_id` (`f_job_id`),
  KEY `workerinfo_f_task_version` (`f_task_version`),
  KEY `workerinfo_f_party_id` (`f_party_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trackingmetric`
--

DROP TABLE IF EXISTS `trackingmetric`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trackingmetric` (
  `f_id` bigint NOT NULL AUTO_INCREMENT,
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_job_id` varchar(64) NOT NULL,
  `f_component_name` varchar(30) NOT NULL,
  `f_task_id` varchar(100) DEFAULT NULL,
  `f_task_version` bigint DEFAULT NULL,
  `f_role` varchar(10) NOT NULL,
  `f_party_id` varchar(20) NOT NULL,
  `f_metric_namespace` varchar(80) NOT NULL,
  `f_metric_name` varchar(80) NOT NULL,
  `f_key` varchar(200) NOT NULL,
  `f_value` longtext NOT NULL,
  `f_type` int NOT NULL,
  PRIMARY KEY (`f_id`),
  KEY `trackingmetric_f_job_id` (`f_job_id`),
  KEY `trackingmetric_f_component_name` (`f_component_name`),
  KEY `trackingmetric_f_role` (`f_role`),
  KEY `trackingmetric_f_metric_namespace` (`f_metric_namespace`),
  KEY `trackingmetric_f_metric_name` (`f_metric_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trackingoutputdatainfo`
--

DROP TABLE IF EXISTS `trackingoutputdatainfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trackingoutputdatainfo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `f_create_time` bigint DEFAULT NULL,
  `f_create_date` datetime DEFAULT NULL,
  `f_update_time` bigint DEFAULT NULL,
  `f_update_date` datetime DEFAULT NULL,
  `f_job_id` varchar(64) NOT NULL,
  `f_component_name` text NOT NULL,
  `f_task_id` varchar(100) DEFAULT NULL,
  `f_task_version` bigint DEFAULT NULL,
  `f_data_name` varchar(30) NOT NULL,
  `f_role` varchar(50) NOT NULL,
  `f_party_id` varchar(20) NOT NULL,
  `f_table_name` varchar(500) DEFAULT NULL,
  `f_table_namespace` varchar(500) DEFAULT NULL,
  `f_description` text,
  PRIMARY KEY (`id`),
  KEY `trackingoutputdatainfo_f_job_id` (`f_job_id`),
  KEY `trackingoutputdatainfo_f_task_id` (`f_task_id`),
  KEY `trackingoutputdatainfo_f_role` (`f_role`),
  KEY `trackingoutputdatainfo_f_party_id` (`f_party_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-19 19:20:24
