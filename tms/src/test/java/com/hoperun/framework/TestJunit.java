package com.hoperun.framework;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

public class TestJunit {
	
	//@Test
	public void testSaveMethod() {
		Food food = new Food("红烧肉", new BigDecimal(45.5).setScale(2,
				BigDecimal.ROUND_HALF_UP), "hsr.jpg");
		saveFood(food);
		Assert.assertNotNull(food.getId());
	}

	public void saveFood(Food food) {
		if (food != null) {
			food.setId(UUID.randomUUID().toString());
			//System.out.println("Food Save is OK!");
			//System.out.println("当前ID：" + food.getId() + "   名称："
			//		+ food.getName() + "   单价：" + food.getPrice());
		} else {
			System.out.println("Food Save is False!");
		}

	}
}

class Food implements Serializable {
	private String id;
	private String name;
	private BigDecimal price;
	private String icon;

	public Food(String name, BigDecimal price, String icon) {
		this.name = name;
		this.price = price;
		this.icon = icon;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getIcon() {
		return icon;
	}
}