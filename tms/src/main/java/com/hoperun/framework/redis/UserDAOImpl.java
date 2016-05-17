package com.hoperun.framework.redis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;


public class UserDAOImpl implements IUserDao{

	    public RedisTemplate<Serializable,Serializable> redisTemplate;
	   
	    public StringRedisTemplate stringRedisTemplate;
	    
	    public void setRedisTemplate(RedisTemplate<Serializable,Serializable> redisTemplate) {  
	        this.redisTemplate = redisTemplate;  
	    }  
	    

	    public void saveUser(final User user) {
	        redisTemplate.execute(new RedisCallback<Object>() {

	            @Override
	            public Object doInRedis(RedisConnection connection) throws DataAccessException {
	                connection.set(redisTemplate.getStringSerializer().serialize("user.uid." + user.getId()),
	                               redisTemplate.getStringSerializer().serialize(user.getName()));
	                return null;
	            }
	        });
	    }
	    
	    public void savePassword(final User user) {
	    	stringRedisTemplate.execute(new RedisCallback<Object>() {

	            @Override
	            public Object doInRedis(RedisConnection connection) throws DataAccessException {
	                connection.set(stringRedisTemplate.getStringSerializer().serialize("user.uid." + user.getId()),
	                				stringRedisTemplate.getStringSerializer().serialize(user.getName()));
	                return null;
	            }
	        });
	    }
	  
	    public void saveUserObject(final User user) {
	        redisTemplate.execute(new RedisCallback<Object>() {

	            @Override
	            public Object doInRedis(RedisConnection connection) throws DataAccessException {
	            	BoundHashOperations<Serializable, String, String> boundHashOperations = redisTemplate.boundHashOps("user.uid." + user.getId());  
	            	Map<String, String> data = new HashMap<String, String>();  
	                data.put("name", user.getName());  
	                data.put("password", user.getPassword());  
	                boundHashOperations.putAll(data);  
	                return null;
	            }
	        });
	    }
	    
	    public User getUserObject(final String id) {
	        return redisTemplate.execute(new RedisCallback<User>() {
	            @Override
	            public User doInRedis(RedisConnection connection) throws DataAccessException {
	            	BoundHashOperations<Serializable, String, String> boundHashOperations = redisTemplate.boundHashOps("user.uid." + id);  
	                Map<String, String> data = boundHashOperations.entries();  
	                if (data != null) {  
	                    User user = new User();  
	                    user.setId(id);  
	                    user.setName(data.get("name"));  
	                    user.setPassword(data.get("password"));  
	                    return user;  
	                } else {  
	                    return null;  
	                }  
	            }
	        });
	    }
	    
	    @Override
	    public User getUser(final String id) {
	        return redisTemplate.execute(new RedisCallback<User>() {
	            @Override
	            public User doInRedis(RedisConnection connection) throws DataAccessException {
	                byte[] key = redisTemplate.getStringSerializer().serialize("user.uid." + id);
	                if (connection.exists(key)) {
	                    byte[] value = connection.get(key);
	                    String name = redisTemplate.getStringSerializer().deserialize(value);
	                    User user = new User();
	                    user.setName(name); 
	                    user.setId(id);
	                    return user;
	                }
	                return null;
	            }
	        });
	    }


		@Override
		public boolean add(final User user) {
			boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
	            public Boolean doInRedis(RedisConnection connection)  
	                    throws DataAccessException {  
	                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();  
	                byte[] key  = serializer.serialize(user.getId());  
	                byte[] name = serializer.serialize(user.getName());  
	                return connection.setNX(key, name);  
	            }  
	        });  
	        return result;
		}


		@Override
		public boolean add(final List<User> list) {
			Assert.notEmpty(list);  
	        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
	            public Boolean doInRedis(RedisConnection connection)  
	                    throws DataAccessException {  
	                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();   
	                for (User user : list) {  
	                    byte[] key  = serializer.serialize(user.getId());  
	                    byte[] name = serializer.serialize(user.getName());  
	                    connection.setNX(key, name);  
	                }  
	                return true;  
	            }  
	        }, false, true);  
	        return result;  
		}


		@Override
		public void delete(final String key) {
			List<String> list = new ArrayList<String>();  
	        list.add(key);  
	        delete(list);  
		}


		@Override
		public void delete(final List<String> keys) {
			redisTemplate.delete((Serializable) keys);;
		}


		@Override
		public boolean update(final User user) {
			String key = user.getId();  
	        if (get(key) == null) {  
	            throw new NullPointerException("数据行不存在, key = " + key);  
	        }  
	        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
	            public Boolean doInRedis(RedisConnection connection)  
	                    throws DataAccessException {  
	            	RedisSerializer<String> serializer = redisTemplate.getStringSerializer();   
	                byte[] key  = serializer.serialize(user.getId());  
	                byte[] name = serializer.serialize(user.getName());  
	                connection.set(key, name);  
	                return true;  
	            }  
	        });  
	        return result;  
		}


		@Override
		public User get(final String keyId) {
			User result = redisTemplate.execute(new RedisCallback<User>() {  
	            public User doInRedis(RedisConnection connection)  
	                    throws DataAccessException {  
	            	RedisSerializer<String> serializer = redisTemplate.getStringSerializer(); 
	                byte[] key = serializer.serialize(keyId);  
	                byte[] value = connection.get(key);  
	                if (value == null) {  
	                    return null;  
	                }  
	                String name = serializer.deserialize(value);  
	                return new User(keyId, name, null);  
	            }  
	        });  
	        return result;  
		}
	    

	}