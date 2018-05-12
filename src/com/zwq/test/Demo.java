package com.zwq.test;

import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.zwq.imapper.IUser;
import com.zwq.model.User;

//测试类
public class Demo {

	private static SqlSessionFactory sqlSessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;

	/*
	 * SqlSessionFactory是MyBatis的关键对象,它是个单个数据库映射关系经过
	 * 编译后的内存镜像.SqlSessionFactory对象的实例可以通过SqlSessionFactoryBuilder对
	 * 象类获得,而SqlSessionFactoryBuilder则可以从XML配置文件或一个预先定制
	 * 的Configuration的实例构建出SqlSessionFactory的实例.每一个MyBatis的应
	 * 用程序都以一个SqlSessionFactory对象的实例为核心.同时SqlSessionFactory也是线程
	 * 安全的,SqlSessionFactory一旦被创建,应该在应用执行期间都存在.在应用运行期间不要重复创
	 * 建多次,建议使用单例模式.SqlSessionFactory是创建SqlSession的工厂.
	 * 
	 */
	
	/*
	 * SqlSession是MyBatis的关键对象,是执行持久化操作的独享,类似于JDBC中
	 * 的Connection.它是应用程序与持久层之间执行交互操作的一个单线程对象,也是MyBatis执
	 * 行持久化操作的关键对象.SqlSession对象完全包含以数据库为背景的所有执行SQL操作的方法,它的
	 * 底层封装了JDBC连接,可以用SqlSession实例来直接执行被映射的SQL语句.每个线程都应该有它自
	 * 己的SqlSession实例.SqlSession的实例不能被共享,同时SqlSession也是线程不安全的
	 * ,绝对不能讲SqlSeesion实例的引用放在一个类的静态字段甚至是实例字段中.也绝不能
	 * 将SqlSession实例的引用放在任何类型的管理范围中,比如Servlet当中的HttpSession对
	 * 象中.使用完SqlSeesion之后关闭Session很重要,应该确保使用finally块来关闭它.
	 * 
	 */
	
	
	// 根据mybatis的配置文件创建sqlSessionFactory
	public static void init() {
		try {
			reader = Resources.getResourceAsReader("com/zwq/config/Configuration.xml");
			// 下面的这行代码功能是通过配置文件com.zwq.config-Configuration.xml,创建SqlSessionFactory对象
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			reader.close(); //关闭流
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	//通过用户id查询用户信息
	public static void testgetOneUserById(int id) {
		
		// 获取SqlSession；注意openSession 每一次获得的是一个全新的session对象，而getCurrentSession获得的是与当前线程绑定的session对象，3、openSession需要手动关闭，而getCurrentSession系统自动关闭
		sqlSession =  sqlSessionFactory.openSession();

		// 获取IUser接口对象，User是个类名User.class会得到一个Class（字节码对象）类型的对象，这个对象包含这个类的所有属性
		IUser iUser = sqlSession.getMapper(IUser.class);

		//调用查询方法
		User user = new User();
		user = iUser.getOneUserByID(id);
		System.out.println("用户ID：" + user.getId());
		System.out.println("用户名：" + user.getUsername());
		System.out.println("用户电话：" + user.getTel());
		System.out.println("用户地址：" + user.getAddress());
	}

	//通过用户名查询用户信息
	public static void testgetOneUserByName(String name) {
		sqlSession = sqlSessionFactory.openSession();
		User user = new User();
		IUser iUser=sqlSession.getMapper(IUser.class);
		user=iUser.getOneUserByName(name);
		System.out.println("用户ID：" + user.getId());
		System.out.println("用户名：" + user.getUsername());
		System.out.println("用户电话：" + user.getTel());
		System.out.println("用户地址：" + user.getAddress());
		
	}
		
	//查询用户所有信息
	public static void testgetAllUser(){
		sqlSession = sqlSessionFactory.openSession();
	    IUser iUser = sqlSession.getMapper(IUser.class);
	    List<User>list = new ArrayList<User>();
	    
	    //集合遍历， for (User user:list) 中第一个参数是集合中元素类型，第二个参数是定义一个集合中该元素类型的变量，冒号右边的是要遍历的集合对象
	    list = iUser.getAllUser();
	    for (User user:list) {
	    	System.out.println("用户ID：" + user.getId());
			System.out.println("用户名：" + user.getUsername());
			System.out.println("用户电话：" + user.getTel());
			System.out.println("用户地址：" + user.getAddress());
			
		}
	    
	}
	
	
	   public static void testaddUser() {
	        sqlSession = sqlSessionFactory.openSession();
			// 获取用户接口对象
			IUser iUser = sqlSession.getMapper(IUser.class);
			// 声明新用户
			User user =new User();
			user.setId(2);
			user.setUsername("zwqzeq");
        
			user.setTel("13264860490");
			user.setAddress("不知道是哪的");
			iUser.addUser(user);
			System.out.println("插入后的ID"+user.getId());
			//提交事务
			sqlSession.commit();
	}

	
	
	
	
	
	public static void main(String[] args) {
		init();
		//testgetOneUserById(1);
		//testgetOneUserByName("zwq");
		//testaddUser();
		testgetAllUser();
		sqlSession.close();
	}

}
