package com.chen.myo2o.service;

import com.chen.myo2o.dto.LocalAuthExecution;
import com.chen.myo2o.entity.LocalAuth;

public interface LocalAuthService {
	/**
	 * 
	 * @param userName
	 * @return
	 */
	LocalAuth getLocalAuthByUserNameAndPwd(String userName, String password);

	/**
	 * 
	 * @param userId
	 * @return
	 */
	LocalAuth getLocalAuthByUserId(long userId);

	/**
	 * 
	 * @param localAuth
	 * @param profileImg
	 * @return
	 * @throws RuntimeException
	 */
	//LocalAuthExecution register(LocalAuth localAuth,CommonsMultipartFile profileImg) throws RuntimeException;


	/**
	 * 
	 * @param localAuth
	 * @return
	 * @throws RuntimeException
	 */
	LocalAuthExecution bindLocalAuth(LocalAuth localAuth)throws RuntimeException;


	/**
	 * 
	 * @param username
	 * @param password
	 * @param newPassword
	 * @return
	 */
	LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword);

}
