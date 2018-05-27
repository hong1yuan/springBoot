package com.spring.springboot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@RestController
public class JdbcCRUD {

    //http://127.0.0.1:21000/insert?j=10&l=10             10*10=100条
    @RequestMapping(value = "/insert")
    public String insert(@RequestParam int j,int l){
        Long begin = System.currentTimeMillis();
        String result = "";
        try{
            java.sql.Connection conn = null;
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/springBoot";
            String user = "root";
            String password = "root";
            conn =  DriverManager.getConnection(url, user, password);
            result = conn.isClosed()?"没连上":"连接成功";
            System.out.println(result);

            String sql = "insert into t_test(name,age) values (?,?)";
            conn.setAutoCommit(false);
            PreparedStatement pst = conn.prepareStatement(sql);
            for (int i = 1; i <= j; i++) {
                for (int k = 1; k <= l; k++) {
                    //pst.setLong(1, k * i);
                    //pst.setLong(2, k * i);
                    pst.setString(1,UUID.randomUUID().toString().substring(0,10));
                    pst.setString(2,UUID.randomUUID().toString().substring(0,10));
                    pst.addBatch();
                }
                pst.executeBatch();
                conn.commit();
            }
            pst.close();
            conn.close();

        }catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return result;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return result;
        }
        Long end = System.currentTimeMillis();
        System.out.println("cast : " + (end - begin) / 1000 + " s");
        return result;

    }
}
