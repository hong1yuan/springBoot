package com.spring.springboot.util.hbase;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.BufferedMutator;
import org.apache.hadoop.hbase.client.BufferedMutatorParams;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.spring.springboot.util.ResourcesUtil;

public class HbaseUtil {
	private static Integer flushSize = 10;

	private static Logger log = LoggerFactory.getLogger(HbaseUtil.class);

	private static Connection connection;

	private static Configuration conf;

	private static final String ROWKEY_FLAG = "key";

	static {
		try {
			conf = HBaseConfiguration.create();
			conf.set("hbase.zookeeper.quorum", ResourcesUtil.getValue("hbase.zookeeper.quorum"));
			conf.set("hbase.zookeeper.property.clientPort",
					ResourcesUtil.getValue("hbase.zookeeper.property.clientPort"));
			connection = ConnectionFactory.createConnection(conf);
			log.info("连接hbase成功");
		} catch (Exception e) {
			log.error("连接hbase异常", e);
		}
	}

	private static Connection getConnection() {
		return connection;
	}

	public static void main(String[] args) {
		try {
			boolean r = HbaseUtil.exists("push_task_info", "123456");
			System.out.println(r);
			/*HbaseUtil.addRecord("push_msg_info", "key123456", "info", new String[] { "push_type", "title", "name" },
					new String[] { "1", "tt", "n1" });
			HbaseUtil.addRecord("push_msg_info", "key123457", "info", new String[] { "push_type", "title", "name" },
					new String[] { "1", "tt", "n1" });
			Map<String, String> map = HbaseUtil.getRecord("push_msg_info", "key123456", "info",
					new String[] { "name", "push_type" });
			for (Map.Entry<String, String> e : map.entrySet()) {
				System.out.println(e.getKey() + ":" + e.getValue());
			}*/
			/*
			 * HbaseUtil.addRecord("push_msg_info", "key123458", "info", new
			 * String[]{"push_type", "title"}, new String[]{"1", "tt"});
			 * System.out.println("111111111"); List<Map<String, String>> list =
			 * HbaseUtil.scanRecords("push_msg_info", "key123456", "key123458",
			 * "info", new String[]{"push_type", "msgids"}); for(Map<String,
			 * String> map : list){ System.out.println("----------------");
			 * for(Map.Entry<String, String> e : map.entrySet()){
			 * System.out.println(e.getKey() + ":" + e.getValue()); } }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean exists(String tableName, String key) throws Exception {
		Table table = null;
		try {
			table = HbaseUtil.getConnection().getTable(TableName.valueOf(tableName));
			Get get = new Get(Bytes.toBytes(key));
			return table.exists(get);
		} finally {
			if (table != null) {
				table.close();
			}
		}
	}

	public static Map<String, String> getRecord(String tableName, String key, String family) throws Exception {
		Table table = null;
		Map<String, String> map = null;
		try {
			table = HbaseUtil.getConnection().getTable(TableName.valueOf(tableName));
			Get get = new Get(Bytes.toBytes(key));
			get.addFamily(Bytes.toBytes(family));
			Result result = table.get(get);
			if (result != null) {
				List<Cell> cells = result.listCells();
				if (cells != null && cells.size() > 0) {
					map = new HashMap<String, String>();
					for (Cell c : result.listCells()) {
						map.put(new String(CellUtil.cloneQualifier(c)), new String(CellUtil.cloneValue(c)));
					}
				}
			}
			return map;
		} finally {
			if (table != null) {
				table.close();
			}
		}
	}

	public static Map<String, String> getRecord(String tableName, String key, String family, String[] qualifiers)
			throws Exception {
		Table table = null;
		Map<String, String> map = null;
		try {
			table = HbaseUtil.getConnection().getTable(TableName.valueOf(tableName));
			Get get = new Get(Bytes.toBytes(key));
			byte[] bf = Bytes.toBytes(family);
			get.addFamily(bf);
			for (String qualifier : qualifiers) {
				get.addColumn(bf, Bytes.toBytes(qualifier));
			}
			Result result = table.get(get);
			if (result != null) {
				List<Cell> cells = result.listCells();
				if (cells != null && cells.size() > 0) {
					map = new HashMap<String, String>();
					for (Cell c : result.listCells()) {
						map.put(new String(CellUtil.cloneQualifier(c)), new String(CellUtil.cloneValue(c)));
					}
				}
			}
			return map;
		} finally {
			if (table != null) {
				table.close();
			}
		}
	}

	public static String getRecord(String tableName, String key, String family, String qualifier) throws Exception {
		Table table = null;
		try {
			table = HbaseUtil.getConnection().getTable(TableName.valueOf(tableName));
			Get get = new Get(Bytes.toBytes(key));
			get.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
			Result result = table.get(get);
			if (result != null) {
				List<Cell> cells = result.listCells();
				if (cells != null && cells.size() > 0) {
					return new String(CellUtil.cloneValue(result.listCells().get(0)));
				}
			}
			return null;
		} finally {
			if (table != null) {
				table.close();
			}
		}
	}

	public static List<Map<String, String>> scanRecords(String tableName, String family, String[] qualifiers) throws Exception {
		return scanRecords(tableName, null, null, family, qualifiers);
	}
	
	public static List<Map<String, String>> scanRecords(String tableName, String startKey, String endKey, String family,
			String[] qualifiers) throws Exception {
		Table table = null;
		List<Map<String, String>> list = null;
		try {
			table = HbaseUtil.getConnection().getTable(TableName.valueOf(tableName));
			Scan scan = new Scan();
			if(StringUtils.isNotBlank(startKey)){
				scan.setStartRow(Bytes.toBytes(startKey));
			}
			if(StringUtils.isNotBlank(endKey)){
				scan.setStopRow(Bytes.toBytes(endKey));
			}
			byte[] f = family.getBytes();
			scan.addFamily(f);
			if (qualifiers == null) {
				return null;
			}
			for (String qualifier : qualifiers) {
				scan.addColumn(family.getBytes(), qualifier.getBytes());
			}
			ResultScanner rs = table.getScanner(scan);
			list = new ArrayList<Map<String, String>>();
			for (Result r : rs) {
				Map<String, String> map = new HashMap<String, String>();
				map.put(ROWKEY_FLAG, new String(new String(r.getRow())));
				for (String qualifier : qualifiers) {
					byte[] q = qualifier.getBytes();
					Cell c = r.getColumnLatestCell(f, q);
					if (c != null) {
						map.put(qualifier, new String(CellUtil.cloneValue(c)));
					} else {
						map.put(qualifier, null);
					}
				}
				list.add(map);
			}
			return list;
		} finally {
			if (table != null) {
				table.close();
			}
		}
	}

	public static void addRecord(String tableName, String key, String family, String qualifier, String value)
			throws Exception {
		Table table = null;
		try {
			table = HbaseUtil.getConnection().getTable(TableName.valueOf(tableName));
			Put put = new Put(Bytes.toBytes(key));
			put.addColumn(family.getBytes(), qualifier.getBytes(), value.getBytes());
			table.put(put);
		} finally {
			if (table != null) {
				table.close();
			}
		}
	}

	public static void addRecord(String tableName, String key, String family, String[] qualifier, String[] value)
			throws Exception {
		Table table = null;
		try {
			table = HbaseUtil.getConnection().getTable(TableName.valueOf(tableName));
			Put put = new Put(Bytes.toBytes(key));
			String tempQualifier = "";
			String tempValue = "";
			for (int i = 0; i < qualifier.length; i++) {
				if (qualifier[i] != null) {
					tempQualifier = qualifier[i];
					if (value[i] == null) {
						tempValue = "";
					} else {
						tempValue = value[i];
					}
					put.addColumn(family.getBytes(), tempQualifier.getBytes(), tempValue.getBytes());
				}
			}

			table.put(put);
		} catch (Exception e) {
			log.error("addRecord [{}] recode error", tableName, e);
			throw e;
		} finally {
			if (table != null) {
				table.close();
			}
		}
	}

	public static void addRecord(String tableName, String key, String family, Map<String, String> qualifierAndValue)
			throws Exception {
		Table table = null;
		try {
			table = HbaseUtil.getConnection().getTable(TableName.valueOf(tableName));
			Put put = new Put(Bytes.toBytes(key));
			for (Map.Entry<String, String> e : qualifierAndValue.entrySet()) {
				put.addColumn(family.getBytes(), e.getKey().getBytes(), e.getValue().getBytes());
			}

			table.put(put);
		} catch (Exception e) {
			log.error("addRecord [{}] recode error", tableName, e);
			throw e;
		} finally {
			if (table != null) {
				table.close();
			}
		}
	}

	public static void delRecord(String tableName, String key) throws Exception {
		Table table = null;
		try {
			table = HbaseUtil.getConnection().getTable(TableName.valueOf(tableName));
			Delete delete = new Delete(key.getBytes());
			table.delete(delete);
		} finally {
			if (table != null) {
				table.close();
			}
		}
	}

	public static void delRecords(String tableName, List<String> keys) throws Exception {
		Table table = null;
		try {
			table = HbaseUtil.getConnection().getTable(TableName.valueOf(tableName));
			List<Delete> list = new ArrayList<Delete>();
			for (String key : keys) {
				list.add(new Delete(key.getBytes()));
			}
			table.delete(list);
		} finally {
			if (table != null) {
				table.close();
			}
		}
	}

	public static boolean checkAndPut(String tableName, String key, String family, String qualifier, String oldValue,
			String newValue) throws Exception {
		Table table = null;
		try {
			table = HbaseUtil.getConnection().getTable(TableName.valueOf(tableName));
			Put put = new Put(Bytes.toBytes(key));
			put.addColumn(family.getBytes(), qualifier.getBytes(), newValue.getBytes());
			return table.checkAndPut(key.getBytes(), family.getBytes(), qualifier.getBytes(), oldValue.getBytes(), put);
		} finally {
			if (table != null) {
				table.close();
			}
		}
	}

	public static boolean checkAndDelete(String tableName, String key, String family, String qualifier, String value)
			throws Exception {
		Table table = null;
		try {
			table = HbaseUtil.getConnection().getTable(TableName.valueOf(tableName));
			Delete delete = new Delete(key.getBytes());
			return table.checkAndDelete(key.getBytes(), family.getBytes(), qualifier.getBytes(), value.getBytes(),
					delete);
		} finally {
			if (table != null) {
				table.close();
			}
		}
	}

	/**
	 * 创建表
	 *
	 * @param tableName
	 *            表名
	 * @param family
	 *            family
	 */
	public static void createTable(String tableName, String family) throws Exception {
		Admin admin = HbaseUtil.getConnection().getAdmin();
		HTableDescriptor desc = new HTableDescriptor(TableName.valueOf(tableName));
		desc.addFamily(new HColumnDescriptor(family));
		if (admin.tableExists(TableName.valueOf(tableName))) {
			log.info("table Exists!");
		} else {
			admin.createTable(desc);
			log.info("create table Success!");
		}
	}

	/**
	 * 判断table是否存在
	 *
	 * @param tableName
	 *            表名
	 */
	public static boolean tableExists(String tableName) throws Exception {
		Admin admin = HbaseUtil.getConnection().getAdmin();
		if (admin.tableExists(TableName.valueOf(tableName))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 批量插入
	 * 
	 * @param family
	 * @param tableName
	 * @param column
	 * @param kvAndCv
	 *            rowkey值和column列值
	 * @throws IOException
	 */
	public static void insertDatas(String tableName, String family, String column, Map<String, String> kvAndCv)
			throws Exception {
		// Table table =
		// HbasePool.connection.getTable(TableName.valueOf(tableName));
		TableName tName = TableName.valueOf(tableName);
		BufferedMutatorParams params = new BufferedMutatorParams(tName);
		params.writeBufferSize(flushSize * 1024 * 1024);
		BufferedMutator table = HbaseUtil.getConnection().getBufferedMutator(params);

		try {
			// table.setWriteBufferSize(flushSize*1024*1024);
			byte[] familyByte = Bytes.toBytes(family);
			byte[] colByte = Bytes.toBytes(column);

			List<Mutation> mutations = new ArrayList<Mutation>();
			for (Map.Entry<String, String> entry : kvAndCv.entrySet()) {
				// if (i % 1000 == 0) {
				// //避免hbase压力过大
				// Thread.sleep(100);
				// }
				Put put = new Put(Bytes.toBytes(entry.getKey()));// 设置rowkey
				put.addColumn(familyByte, colByte, Bytes.toBytes(entry.getValue()));

				mutations.add(put);
			}
			table.mutate(mutations);
		} catch (IOException e) {
			log.error("insert table [{}] recodes error", tableName, e);
		} finally {
			try {
				if (table != null) {
					// table.close();
					table.flush();
				}
			} catch (IOException e1) {
				log.error("Table closed fail！ " + e1);
			}
		}

	}

	/**
	 * 批量插入
	 * 
	 * @param family
	 * @param tableName
	 * @param columnNames
	 *            插入的列名列表
	 * @param kvAndCv
	 *            插入的rowkey及每列的值列表
	 * @throws Exception
	 */
	public static void insertDatas(String tableName, String family, List<String> columnNames,
			Map<String, List<String>> kvAndCv) throws Exception {
		// Table table =
		// HbasePool.connection.getTable(TableName.valueOf(tableName));
		TableName tName = TableName.valueOf(tableName);
		BufferedMutatorParams params = new BufferedMutatorParams(tName);
		params.writeBufferSize(flushSize * 1024 * 1024);
		BufferedMutator table = HbaseUtil.getConnection().getBufferedMutator(params);

		try {
//			long start = System.currentTimeMillis();
			byte[] familyByte = Bytes.toBytes(family);
			List<byte[]> listColNamesByte = new ArrayList<byte[]>();
			for (String columnName : columnNames) {
				listColNamesByte.add(Bytes.toBytes(columnName));
			}

			List<Mutation> mutations = new ArrayList<Mutation>();
			String tempColValue = "";
			for (Map.Entry<String, List<String>> entry : kvAndCv.entrySet()) {
				// if (i % 1000 == 0) {
				// //避免hbase压力过大
				// Thread.sleep(100);
				// }
				Put put = new Put(Bytes.toBytes(entry.getKey()));// 设置rowkey
				List<String> listColValue = entry.getValue();
				for (int i = 0; i < listColNamesByte.size(); i++) {
					if (listColValue.get(i) == null) {
						tempColValue = "";
					} else {
						tempColValue = listColValue.get(i);
					}
					put.addColumn(familyByte, listColNamesByte.get(i), Bytes.toBytes(tempColValue));
				}

				mutations.add(put);
			}
			table.mutate(mutations);

//			log.info("---hbase insertDatas use time:" + (System.currentTimeMillis() - start));
		} catch (IOException e) {
			log.error("insert table [{}] recodes error", tableName, e);
		} finally {
			try {
				if (table != null) {
					// table.close();
					table.flush();
				}
			} catch (IOException e1) {
				log.error("Table closed fail！ " + e1);
			}
		}

	}
	
	 /**
	  * 获取某列所有数据
	  * @param tableName
	  * @param family
	  * @return
	  */
  public static Map<String, String> getColAll(String tableName, String family, String colName) {
		Map<String, String> map = new HashMap<String, String>();
		Table table = null;
		ResultScanner rs = null;
		
		try {
			table = HbaseUtil.getConnection().getTable(TableName.valueOf(tableName));
			Scan scan = new Scan();
			/*scan.setBatch(3);*/
			//scan.setCaching(1000);
			scan.addColumn(Bytes.toBytes(family), Bytes.toBytes(colName));
			rs = table.getScanner(scan);
			int i = 0; 
			for (Result r : rs) {
				map.put(Bytes.toString(r.getRow()), Bytes.toString(r.getValue(Bytes.toBytes(family), Bytes.toBytes(colName))));
			}
			return map;
		} catch (IOException e) {

			log.error("Table " + tableName + " does not exist.", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (table != null)
					table.close();
			} catch (IOException e1) {
				log.error("Table closed fail！ ", e1);
			}
		}
		return null;
	}

	/**
	 * 获取所有数据
	 * 
	 * @param tableName
	 * @param family
	 * @param cols
	 * @return
	 */
	public static Map<String, Map<String, String>> getAll(String tableName, String family, String[] cols) {

		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		Table table = null;
		ResultScanner rs = null;

		try {
			table = HbaseUtil.getConnection().getTable(TableName.valueOf(tableName));
			Scan scan = new Scan();
			/*
			 * scan.setBatch(3); scan.setCaching(10);
			 */
			rs = table.getScanner(scan);
			for (Result r : rs) {
				Map<String, String> mapCol = new HashMap<String, String>();
				for (String col : cols) {
					mapCol.put(col, Bytes.toString(r.getValue(Bytes.toBytes(family), Bytes.toBytes(col))));
				}
				map.put(Bytes.toString(r.getRow()), mapCol);
			}
			return map;
		} catch (IOException e) {

			log.error("Table " + tableName + " does not exist.", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (table != null)
					table.close();
			} catch (IOException e1) {
				log.error("Table closed fail！ ", e1);
			}
		}
		return null;
	}

	/**
	 * 获取记录
	 */
	public static Result getRecode(String tableName, String family, String rowkey) {
		Table table = null;
		try {
			Get get = new Get(Bytes.toBytes(rowkey));
			get.addFamily(Bytes.toBytes(family));
			table = HbaseUtil.getConnection().getTable(TableName.valueOf(tableName));

			Result result = table.get(get);
			return result;

		} catch (IOException e) {
			log.error(String.format("get table [{}] recodes error", tableName), e);
		} finally {
			try {
				if (table != null) {
					table.close();
				}
			} catch (IOException e1) {
				log.error("Table closed fail！ " + e1);
			}
		}
		return null;
	}

	/**
	 * 根据某列获取key
	 * 
	 * @param tableName
	 * @param family
	 * @param colName
	 * @param colValue
	 * @return
	 */
	public static List<String> getKeyByCol(String tableName, String family, String colName, String colValue) {
		Table table = null;
		List<String> listKey = new ArrayList<String>();
		try {
			table = HbaseUtil.getConnection().getTable(TableName.valueOf(tableName));
			Scan scan = new Scan();
			scan.addFamily(family.getBytes());
			SingleColumnValueFilter f = new SingleColumnValueFilter(family.getBytes(), colName.getBytes(),
					CompareOp.EQUAL, colValue.getBytes());
			f.setLatestVersionOnly(true);
			f.setFilterIfMissing(true);
			scan.setFilter(f);
			ResultScanner res = table.getScanner(scan);
			Result next = null;

			while ((next = res.next()) != null) {
				listKey.add(new String(next.getRow()));
			}
			return listKey;
		} catch (IOException e) {
			log.error(String.format("getKeyByCol [{}] error", tableName), e);
		} finally {
			try {
				if (table != null) {
					table.close();
				}
			} catch (IOException e1) {
				log.error("Table closed fail！ " + e1);
			}
		}
		return null;

	}

	/**
	 * 得到所有key
	 *
	 * @param @param
	 *            tableName
	 * @param @param
	 *            family
	 * @return List<String>
	 */
	public static List<String> getKeys(String tableName, String family) {
		Table table = null;
		ResultScanner rs = null;
		List<String> list = new ArrayList<String>();
		try {
			table = HbaseUtil.getConnection().getTable(TableName.valueOf(tableName));

			Scan s = new Scan();
			rs = table.getScanner(s);
			for (Result r : rs) {
				list.add(Bytes.toString(r.getRow()));
			}
			return list;
		} catch (IOException e) {
			log.error("Table " + tableName + " does not exist.", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (table != null) {
					table.close();
				}
			} catch (IOException e1) {
				log.error("Table closed fail！ " + e1);
			}
		}
		return null;
	}

	 /**
	  * 批量获取Result
	  * @param tableName
	  * @param family
	  * @param listKey
	  * @return
	  */
		public static List<Result> getListResultByKeys(String tableName, String family, List<String> listKey) {
			 Table table = null;
		        try {	        	
		        	table = HbaseUtil.getConnection().getTable(TableName.valueOf(tableName));
		        	List<Get> listGet = new ArrayList<Get>();  
		        	for (String rowkey : listKey){
		                 Get get = new Get(Bytes.toBytes(rowkey));  
		                 get.addFamily(Bytes.toBytes(family));
		                 listGet.add(get);  
		             }  
		            

		            Result[] result = table.get(listGet);
		            ArrayList<Result> listResult = new ArrayList<Result>(Arrays.asList(result));
		            return listResult;

		        } catch (IOException e) {
		        	log.error(String.format("get table [{}] recodes error", tableName), e);
		        } finally {
		            try {
		                if (table != null) {
		                    table.close();
		                }
		            } catch (IOException e1) {
		            	log.error("Table closed fail！ " + e1);
		            }
		        }
		        return null;
		}

    public static long rowCount(String tableName) throws Exception {
    	long rowCount = 0;  
    	Table table = null;
		ResultScanner rs = null;
		try {
			table = HbaseUtil.getConnection().getTable(TableName.valueOf(tableName));

			Scan s = new Scan();
			s.setFilter(new FirstKeyOnlyFilter());  
			rs = table.getScanner(s);
			for (Result result : rs) {  
                rowCount += result.size();  
            }  
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (table != null) {
				table.close();
			}
		}
        return rowCount;  
    }  
    
    
    public static List<Map<String, String>> scanRecordsWithFilterEquals(String tableName, String regexStr, String family,String[] qualifiers) throws Exception {
		Table table = null;
		List<Map<String, String>> list = null;
		try {
			table = HbaseUtil.getConnection().getTable(TableName.valueOf(tableName));
			Scan scan = new Scan();
			
			scan.setCaching(50);
			
			Filter filter = new RowFilter(CompareOp.EQUAL, new RegexStringComparator(regexStr));
			scan.setFilter(filter);
			
			byte[] f = family.getBytes();
			scan.addFamily(f);
			if (qualifiers == null) {
				return null;
			}
			
			for (String qualifier : qualifiers) {
				scan.addColumn(family.getBytes(), qualifier.getBytes());
			}
			
			ResultScanner rs = table.getScanner(scan);
			list = new ArrayList<Map<String, String>>();
			for (Result r : rs) {
				Map<String, String> map = new HashMap<String, String>();
				map.put(ROWKEY_FLAG, new String(new String(r.getRow())));
				for (String qualifier : qualifiers) {
					byte[] q = qualifier.getBytes();
					Cell c = r.getColumnLatestCell(f, q);
					if (c != null) {
						map.put(qualifier, new String(CellUtil.cloneValue(c)));
					} else {
						map.put(qualifier, null);
					}
				}
				list.add(map);
			}
			return list;
		} finally {
			if (table != null) {
				table.close();
			}
		}
	}
    
    public static List<Map<String, String>> scanRecordsWithFilterEquals(String tableName, String regexStr, String family) throws Exception {
		Table table = null;
		List<Map<String, String>> list = null;
		try {
			table = HbaseUtil.getConnection().getTable(TableName.valueOf(tableName));
			Scan scan = new Scan();
			
			Filter filter = new RowFilter(CompareOp.EQUAL, new RegexStringComparator(regexStr));
			scan.setFilter(filter);
			
			ResultScanner rs = table.getScanner(scan);
			list = new ArrayList<Map<String, String>>();
			
			for (Result r : rs) {
				Map<String, String> map = new HashMap<String, String>();
				map.put(ROWKEY_FLAG, new String(new String(r.getRow())));
				List<Cell> cells = r.listCells();
				if (cells != null && cells.size() > 0) {
					for (Cell c : cells) {
						map.put(new String(CellUtil.cloneQualifier(c)), new String(CellUtil.cloneValue(c)));
					}
				}
				list.add(map);
			}
			return list;
		} finally {
			if (table != null) {
				table.close();
			}
		}
	}
    
}
