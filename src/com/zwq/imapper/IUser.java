package com.zwq.imapper;

import java.util.List;

import com.zwq.model.User;

public interface IUser {

	//查询用户信息
	public User getOneUserByID(int id);//根据用户id查询
	public User getOneUserByName(String name);//根据用户名查询
	public List<User>getAllUser();//查询所有用户信息
	
	//添加用户信息
	public void addUser(User user);
	
	//修改用户信息
	//public Boolean updateUser(User user);
	
	//删除用户信息
	//public Boolean deleteUser(int id);
	 
		
}
