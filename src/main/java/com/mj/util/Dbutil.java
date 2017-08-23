package com.mj.util;

import java.sql.*;
import java.util.*;

public class Dbutil {
    private String driver="com.mysql.jdbc.Driver";
    private String url="jdbc:mysql://192.168.0.183:3306/drunkard?useUnicode=true&characterEncoding=utf-8";
    private String user="drunk";
    private String password="drunk";
    private Connection conn=null;
    //定义当前页
    private int currpage=1;
    //页显
    private int pagesize=5;
    //定义总条数
    private int recordcount=0;
    //定义总页数
    private int pagecount=0;
    //定义数据库名
    private String dbname="drunkard";
    //定义端口号
    private int port=3306;
    //定义ip
    private String host="localhost";
    //因为需要经常换数据库，所以封装一个方法连接数据库的
    public void connect(String host,String user,String password,int port,String dbname){
        this.host=host;
        this.url=url;
        this.user=user;
        this.password=password;
        this.dbname=dbname;
        this.port=port;
        this.url=String.format("jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=utf-8", this.host,this.port,this.dbname);
        try {
            Class.forName(driver);
            this.conn= DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public int getCurrpage() {
        return currpage;
    }

    public void setCurrpage(int currpage) {
        this.currpage = currpage;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getRecordcount() {
        return recordcount;
    }

    public void setRecordcount(int recordcount) {
        this.recordcount = recordcount;
    }

    public int getPagecount() {
        return pagecount;
    }

    public void setPagecount(int pagecount) {
        this.pagecount = pagecount;
    }

    //连接数据库部分
    /**
     * 默认的情况下用这个，指定的账号地址用下面一个，要改端口的话就用connect方法
     */
    public Dbutil() {
        try {
            Properties pro = new Properties();
            pro.load(Dbutil.class.getClassLoader().getResourceAsStream("db.properties"));
            this.driver = pro.getProperty("db.driver");
            Class.forName(this.driver);
            this.url = pro.getProperty("db.url");
            this.user = pro.getProperty("db.user");
            this.password = pro.getProperty("db.password");
            this.conn=DriverManager.getConnection(this.url, this.user, this.password);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

    }


    //因为如果换数据库，密码很有可能不同，所以重写一下Dbutil()构造方法
    public Dbutil(String host,String user,String password,String dbname) {
        this.host=host;
        this.user=user;
        this.dbname=dbname;
        this.url=String.format("jdbc:mysql://%s:3306/%s?useUnicode=true&characterEncoding=utf-8", this.host,this.dbname);
        try {
            Class.forName(driver);
            this.conn=DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public Connection getConn(){

        return this.conn;
    }

    //获取主键的方法
    public String getPk(String tablename){
        String pk=null;
        try {
            DatabaseMetaData dbmd = this.conn.getMetaData();
            ResultSet rs=dbmd.getPrimaryKeys(this.dbname , null, tablename);
            if(rs.next()){
                //获取主键列
                pk=rs.getString(4);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pk;
    }




    //删除的方法(根据主键删)
    public int deleteById(String tablename,Object id){
        //这个是调用delete(String tablename,String where)，
        //然后把参数中条件调用查询主键方法,相当于结果是字段sid=传进来的id值，从而达到通过主键删除的目的
        int num=delete(tablename,this.getPk(tablename)+"="+id);
        return num;
    }
    //删除的方法(根据条件删)
    public int delete(String tablename,String where){
        int num=0;
        String sql=String.format("delete from %s where %s", tablename,where);
        try {
            PreparedStatement pst=this.conn.prepareStatement(sql);
            num=pst.executeUpdate();
            //关闭资源
            pst.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return num;
    }
    //删除的方法(根据表名删，直接删完)
    public int delete(String tablename){
        //这个是调用上面这个方法，把条件给成绝对相等，这样就可以直接删除整个表
        int num=delete(tablename,"2=2");
        return num;
    }



    //利用数组的方法插入数据
    public int add(String sql, Object[] values) {
        int num = 0;
        PreparedStatement pst;
        try {
            pst = this.conn.prepareStatement(sql);
            int i = 0;
            for (Object o : values) {
                pst.setObject(++i, o);
            }
            num = pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num;
    }
    //利用map集合的方法插入数据
    public int insert(String tablename, Map<String, Object> m) {
        int num = 0;
        StringBuilder n = new StringBuilder();
        StringBuilder v = new StringBuilder();
        for (String k : m.keySet()) {
            v.append("?,");
            n.append(k + ",");
        }
        String sql = String.format("insert into %s(%s) values(%s)", tablename, n.toString().subSequence(0, n.length() - 1), v.toString().subSequence(0, v.length() - 1));
        PreparedStatement pst;
        try {
            pst = this.conn.prepareStatement(sql);
            int i = 0;
            for (Object o : m.values()) {
                pst.setObject(++i, o);
            }
            num = pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num;
    }




    //类似于以前贵美写的修改查询 方法,只是以前用的是list集合，现在用的是map集合
    public Map<String, Object> queryById(String tablename,Object id){
        //也是一样，建个集合，把查询到的存到集合中
        Map<String, Object>m=new HashMap<String,Object>();
        String sql=String.format("select * from %s where %s",tablename,this.getPk(tablename)+"="+id);
        try {
            PreparedStatement pst=this.conn.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                //获取结果的信息
                ResultSetMetaData rsmd=rs.getMetaData();
                //获取列的数量
                int cc=rsmd.getColumnCount();
                for (int i = 1; i <=cc; i++) {
                    //获取的是指定下标的别名rsmd.getColumnLabel(i);
                    String name=rsmd.getColumnLabel(i);
                    m.put(name, rs.getObject(name));
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return m;
    }

    //修改方法
    public int update(String tablename,Map<String,Object> m){
        int num = 0;
        String pk = this.getPk(tablename);
        //判断是否包含主键列
        if(m.containsKey(pk)){
            num = update(tablename,m,pk+"="+m.get(pk));
        }else{
            num = update(tablename,m,"1=1");
        }
        return num;
    }

    public int update(String tablename,Map<String,Object> m,String where) {
        int num = 0;
        StringBuilder s = new StringBuilder();
        for(String k : m.keySet()){
            s.append(k+"=?,");
        }
        String sql = String.format("update %s set %s where %s",tablename,s.toString().subSequence(0,s.length()-1),where);
        PreparedStatement pst;
        try {
            pst = this.conn.prepareStatement(sql);
            int i = 0;
            for(Object o : m.values()){
                pst.setObject(++i,o);
            }
            num = pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num;
    }

    public void close(){
        if(this.conn!=null){
            try {
                this.conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    //查询个数的方法，可以查询指定条件下的个数，可用于jQuery中ajax异步显示进行查询，判断数据库有没有这个数据
    public int count(String tablename, String where) {
        int num = 0;
        String sql = String.format("select count(*) from %s where %s", tablename, where);
        try {
            PreparedStatement pst = this.conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                num = rs.getInt(1);
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num;
    }


    //以下三个是查询方法，可以根据指定条件，指定字段进行查询，可以应用于把表中的数据显示在html或jsp中
    //html中也没法用el表达式，所以显示在html静态网页，最好是采取json结合标签显示
    public List<Map<String, Object>> query(String tablename, String field) {
        return query(tablename, field, "1=1", "");
    }

    public List<Map<String, Object>> query(String tablename) {
        return query(tablename, "*", "1=1", "");
    }

    public List<Map<String, Object>> query(String tablename, String field, String where, String order) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String sql = String.format("select %s from %s where %s %s", field, tablename, where, order);
        PreparedStatement pst;
        try {
            pst = this.conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                Map<String, Object> m = new HashMap<String, Object>();
                int cc = rsmd.getColumnCount();
                for (int i = 1; i <= cc; i++) {
                    String name = rsmd.getColumnLabel(i);
                    m.put(name, rs.getObject(name));
                }
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }



    //以下三个方法是page(int currpage, String tablename, String fields, String where, String order)
    public List<Map<String, Object>> page(int currpage, String tablename,String where,String order) {
        return page(currpage,tablename,"*",where,order);
    }

    public List<Map<String, Object>> page(int currpage, String tablename,String order) {
        return page(currpage,tablename,"*","where 1=1",order);
    }
    public List<Map<String, Object>> page(int currpage, String tablename) {
        return page(currpage,tablename,"*","where 1=1","");
    }
    //tablename表名    fields字段
    public List<Map<String,Object>> page(int currpage,String tablename,String fileds,String where,String order){
        this.currpage=currpage;
        List<Map<String, Object>>list=new ArrayList<Map<String,Object>>();
        //用string.format实现sql语句    第一个%s对应的是后面的fileds字段，第二个%s对应的tablename表名，第三个%s对应的where条件，第四个%s对应的order排序
        String sql=String.format("select %s from %s %s %s limit ?,?", fileds,tablename,where,order);
        String qq=String.format("select count(*) c from %s %s", tablename,where);
        try {
            //分页信息
            PreparedStatement qpst=this.conn.prepareStatement(qq);
            ResultSet qrs=qpst.executeQuery();
            while(qrs.next()){
                this.recordcount=qrs.getInt("c");
                //总页数
                this.pagecount=this.recordcount%this.pagesize==0?this.recordcount/this.pagesize:this.recordcount/this.pagesize+1;
            }
            if(this.currpage<1) this.currpage=1;
            if(this.currpage>this.pagecount) this.currpage=this.pagecount;
            //分页结果信息
            PreparedStatement pst=this.conn.prepareStatement(sql);
            //从哪开始
            pst.setInt(1, this.currpage*this.pagesize-this.pagesize);
            //每页显示几条
            pst.setInt(2, this.pagesize);
            ResultSet rs=pst.executeQuery();
            //获取结果的信息，有时候要列名的
            ResultSetMetaData rsmd=rs.getMetaData();
            while(rs.next()){
                Map<String, Object>m=new HashMap<String,Object>();
                //查询结果列的数目
                int cc=rsmd.getColumnCount();
                //获取的是指定下标的别名rsmd.getColumnLabel(i);
                for (int i =1; i <=cc; i++) {
                    String name=rsmd.getColumnLabel(i);
//rs.getObject(name)这个就类似于我们以前用的rs.getInt(1),rs.getString(name);
                    m.put(name, rs.getObject(name));
                }
                list.add(m);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }


    //用了bootstrap之前的分页样式代码
    public String pageinfo(){
        StringBuilder s = new StringBuilder();
        //StringBuilder是一个字符串，append方法就是添加进这个字符串
        s.append("<div class=\"page\">");
        int start = 1;
        int end = 10;

        if(this.currpage>=7){
            start = this.currpage-5;
            end = this.currpage+4;
        }
        if(this.currpage!=1){
            s.append(String.format("<a class=\"prev\" href=\"?p=%d\">上一页</a>",this.currpage-1));
        }
        for(int i=start;i<=end;i++){
            if(i>this.pagecount) break;
            if(this.currpage == i){
                s.append(String.format("<span>%d</span>",i));
                continue;
            }
            s.append(String.format("<a href=\"?p=%d\">%d</a>",i,i));
        }
        if(this.currpage<this.pagecount){
            s.append(String.format("<a class=\"next\" href=\"?p=%d\">下一页</a>",this.currpage+1));
        }
        s.append("</div>");
        return s.toString();
    }
    public String pageinfo(String pat){
        StringBuilder s = new StringBuilder();
        //StringBuilder是一个字符串，append方法就是添加进这个字符串
        s.append("<div class=\"pagepp\">");
        int start = 1;
        int end = 10;

        if(this.currpage>=7){
            start = this.currpage-5;
            end = this.currpage+4;
        }
        if(this.currpage!=1){
            s.append(String.format("<a class=\"prevpp\" href=\"%s?p=%s\">上一页</a>",pat,this.currpage-1));
        }
        for(int i=start;i<=end;i++){
            if(i>this.pagecount) break;
            if(this.currpage == i){
                s.append(String.format("<span>%d</span>",i));
                continue;
            }
            s.append(String.format("<a href=\"%s?p=%s\">%d</a>",pat,i,i));
        }
        if(this.currpage<this.pagecount){
            s.append(String.format("<a class=\"nextnn\" href=\"%s?p=%s\">下一页</a>",pat,this.currpage+1));
        }
        s.append("</div>");
        return s.toString();
    }
    //用了bootstrap之后的分页样式代码
    public String pagebootstrap() {
        StringBuilder s = new StringBuilder();
        s.append("<ul class=\"pagination\">");
        int start = 1;
        int end = 10;

        if (this.currpage >= 7) {
            start = this.currpage - 5;
            end = this.currpage + 4;
        }
        if (this.currpage != 1) {
            s.append(String.format("<li><a class=\"prevpp\" href=\"?p=%d\">上一页</a></li>", this.currpage - 1));
        }
        for (int i = start; i <= end; i++) {
            if (i > this.pagecount)
                break;
            if (this.currpage == i) {
                s.append(String.format("<li class=\"active\"><a href=\"javascript:void(0)\">%d</a></li>", i));
                continue;
            }
            s.append(String.format("<li><a href=\"?p=%d\">%d</a></li>", i, i));
        }
        if (this.currpage < this.pagecount) {
            s.append(String.format("<li><a class=\"nextnn\" href=\"?p=%d\">下一页</a></li>", this.currpage + 1));
        }
        s.append("</ul>");
        return s.toString();
    }
}
