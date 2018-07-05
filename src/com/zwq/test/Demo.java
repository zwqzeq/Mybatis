package com.zwq.test;

import java.io.Reader;
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


	

	
	/**
	 * Mybatis开发流程：
	 * 0. 配置环境：
	 * 1. 新建工程，导入相关jar包----jdbc驱动包，log日志包，Mybatis驱动包
	 *    先组织好包的框架：         a:   config包存放 属性文件（如日志，连数据库的属性） 和xml文件
	 *                   b:   model包里面放 JavaBean类，从数据库中取出的数据以及要存到数据库中的数据都要先存到JavaBean类的对象中
	 *                   c:   imapper包放  接口
	 *                   d:   demo包放测试类
     *
	 * 2. 在源码中配置一个日志文件，用于打印日志log4j.properties
	 * 3. 配置核心配置文件，配置文件中主要是配置连接数据库和事务管理的内容，文件名可以自定义，默认SqlMapConfig.xml
	 *    或者将连接数据库中的属性封装到数据库配置文件db.properties中
	 * 
	 * 4. 配置sql映射文件,文件名自定义，这里默认为User.xml
	 * 5. 在核心配置文件中引入映射文件<mappers><mapper resource="User.xml"/></mappers>
	 * 6. 环境配置完毕
	 * 
	 * 7. 1）定义接口
	 *    2）定义实体类
	 *    4）定义测试类

	 */
	
	
	
	
	
	/*
	 * 传统的JDBC方式操作数据库缺点：频繁创建释放资源降低性能;代码耦合性强，不易维护;传参与所获结果集编码不够灵活(存在硬编码)
	 * 
	 * mybatis操作数据库的优点： 1、在SqlMapConfig.xml中配置数据链接池，使用连接池管理数据库链接,大大减少了不断创建释放资源。
	 * 2、将Sql语句配置在XXXXmapper.xml文件中与java代码分离。
	 * 3、Mybatis自动将java对象映射至sql语句，通过statement中的parameterType定义输入参数的类型。
	 * 解决了条件查询中笨重问题
	 *
	 */

	/*
	 * 二、Mybatis与Hibernate区别 ：
	 * 两个都是持久层框架，操作数据库，但是两者还是有区别的
	 * hibernate:它是一个标准的orm框架,比较重量级,学习成本高. 
	 * 优点:高度封装,使用起来不用写sql,开发的时候,会减低开发周期.
	 * 缺点:sql语句无法优化 
	 * 应用场景:oa(办公自动化系统), erp(企业的流程系统)等,还有一些政府项目,
	 * 总的来说,在用于量不大,并发量小的时候使用.
	 * 
	 * mybatis:它不是一个orm框架, 它是对jdbc的轻量级封装, 学习成本低,比较简单
	 * 优点:学习成本低, sql语句可以优化, 执行效率高,速度快 
	 * 缺点:编码量较大,会拖慢开发周期 
	 * 应用场景: 互联网项目,比如电商,P2p等
	 * 总的来说是用户量较大,并发高的项目。
	 * 
	 * 
	 * 
	 */

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
			// 下面的这行代码功能是通过配置文件com.zwq.config-Configuration.xml,创建SqlSessionFactory对象 ，即创建工厂
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			reader.close(); // 关闭流
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	// 通过用户id查询用户信息
	public static void testgetOneUserById(int id) {

		// 获取SqlSession；
		// 注意： openSession每一次获得的是一个全新的session对象，而getCurrentSession获得的是与当前线程绑定的session对象，3、openSession需要手动关闭，而getCurrentSession系统自动关闭
		sqlSession = sqlSessionFactory.openSession();//通过工厂创建会话

		
		//查询一条用户信息：     方法一：
		// 获取IUser接口对象，User是个类名User.class会得到一个Class（字节码对象）类型的对象，这个对象包含这个类的所有属性
		IUser iUser = sqlSession.getMapper(IUser.class);
		// 调用查询方法
		User user = new User();
		user = iUser.getOneUserByID(id);
		System.out.println("用户ID：" + user.getId());
		System.out.println("用户名：" + user.getUserName());
		System.out.println("用户电话：" + user.getTelphone());
		System.out.println("用户地址：" + user.getAddress());
		
		
		/*
		 * 
		 * 查询一条用户信息：       方法二：
		 * 这里调用的是selectOne方法，旨在查询一条指定的数据，如果用它查询多条数据，会报异常(查询多条要用selectList)
		 * 
		 *  //第一个参数：所调用的sql语句：namespace+‘.’+SqlID
         *  //第二个参数：传入的参数
		 * */
		/*User user =new User();
	    user=sqlSession.selectOne("com.zwq.imapper.IUser.getOneUserByID",1);
	    System.out.println(user);
	    System.out.println("用户ID：" + user.getId());
		System.out.println("用户名：" + user.getUsername());
		System.out.println("用户电话：" + user.getTel());
		System.out.println("用户地址：" + user.getAddress());*/	
	}

	// 通过用户名查询用户信息
	public static void testgetOneUserByName(String name) {
		sqlSession = sqlSessionFactory.openSession();
		User user = new User();
		IUser iUser = sqlSession.getMapper(IUser.class);
		user = iUser.getOneUserByName(name);
		System.out.println("用户ID：" + user.getId());
		System.out.println("用户名：" + user.getUserName());
		System.out.println("用户电话：" + user.getTelphone());
		System.out.println("用户地址：" + user.getAddress());

	}

	// 查询用户所有信息
	public static void testgetAllUser() {
		sqlSession = sqlSessionFactory.openSession();
		IUser iUser = sqlSession.getMapper(IUser.class);
		List<User> list = new ArrayList<User>();

		// 集合遍历， for (User user:list)
		// 中第一个参数是集合中元素类型，第二个参数是定义一个集合中该元素类型的变量，冒号右边的是要遍历的集合对象
		list = iUser.getAllUser();
		for (User user : list) {
			System.out.println("用户ID：" + user.getId());
			System.out.println("用户名：" + user.getUserName());
			System.out.println("用户电话：" + user.getTelphone());
			System.out.println("用户地址：" + user.getAddress());

		}

	}

	// 添加一个用户信息
	public static void testaddUser() {
		sqlSession = sqlSessionFactory.openSession();

		// 获取用户接口对象
		IUser iUser = sqlSession.getMapper(IUser.class);

		// 声明新用户
		User user = new User();

		// 此处没有设置用户ID是因为数据库中id列设置的是主键（不可重复）且自增（也就是每次向数据库中插入一个用户数据，id值自动加一，不用手动设置id）
		user.setUserName("abcdefghj");
		user.setTelphone("63");
		user.setAddress("上海");
		iUser.addUser(user);
		// 提交事务，如果不提交，则数据不能保存到数据库
		sqlSession.commit();
		user = iUser.getOneUserByName(user.getUserName());
		System.out.println("添加ID为 "+user.getId()+",姓名为："+user.getUserName()+" 的用户信息成功");	
	}

	//根据用户ID删除一个用户信息
	public static void testdeleteUserByID(int id){		
		sqlSession = sqlSessionFactory.openSession();
		IUser iUser = sqlSession.getMapper(IUser.class);
		iUser.deleteUserByID(id);
		sqlSession.commit();
		System.out.println("删除ID为"+id+"的用户信息成功");	
	}
	
	//根据用户ID修改一个用户信息
	public static void testupdateUserByID(int id){
		
		sqlSession=sqlSessionFactory.openSession();
		IUser iUser = sqlSession.getMapper(IUser.class);
		User user=new User();
		user.setId(id);
		user.setUserName("王修");
		iUser.updateUserByID(user);
        sqlSession.commit();
        System.out.println("修改ID为"+id+"的用户成功！");
	}
	
	//程序入口
	public static void main(String[] args) {
		init();
		//testgetOneUserById(1);
		//testgetOneUserByName("abcdef");		
		//testgetAllUser();		
		//testaddUser();
		testupdateUserByID(37);
		//testdeleteUserByID(38);
		sqlSession.close();
	}

}
