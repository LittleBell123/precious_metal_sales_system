package com.coding.sales.trans;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coding.constant.ProductItemConstant;
import com.coding.sales.input.OrderCommand;
import com.coding.sales.input.OrderItemCommand;
import com.coding.sales.input.PaymentCommand;
import com.coding.sales.members.MemberInfo;
import com.coding.sales.members.Members;
import com.coding.sales.output.DiscountItemRepresentation;
import com.coding.sales.output.OrderItemRepresentation;
import com.coding.sales.output.OrderRepresentation;
import com.coding.sales.output.PaymentRepresentation;
import com.coding.sales.product.Product;
import com.coding.util.StringUtils;
/**
 *@author yangshen
 * @date  2019年7月2日
 */
public class TransAmtCalcService {
	

	public OrderRepresentation dealOrder(OrderCommand orderCommand) {
		
		return null;
		
	} 
	
	public String calculateTotalPrice(OrderItemCommand orderItemCommand, Product product) {
		
		Double totalPrice = orderItemCommand.getAmount().doubleValue() * product.getPrice().doubleValue();
		
		return String.format("%.2f", totalPrice);
	}
	
	 public Map<String, Object>  updateMemberBonusPoints(MemberInfo memberInfo, Product product, PaymentCommand paymentCommand, OrderItemCommand orderItemCommand, String discount){
		    Map<String, Object> resultMap = new HashMap<String, Object>();
			Double totalPrice = orderItemCommand.getAmount().doubleValue() * product.getPrice();
			Double discountDouble = 0.0;
			String level = memberInfo.getLevel();
			if ( isCanDiscount(product, discount)) {
				if (ProductItemConstant.ProdcutDiscount.DISCOUNT_90.equals(discount)) {
					discountDouble = 0.9;
					totalPrice = totalPrice * 0.9;
				}else if (ProductItemConstant.ProdcutDiscount.DISCOUNT_95.equals(discount)) {
					discountDouble = 0.95;
					totalPrice = totalPrice * 0.95;
				}
			}
			Double points = 0.0;
			if ( null == Members.memberCardLevel.get(level)) {
				throw new IllegalArgumentException("卡级别错误");
			}
			points = totalPrice * Members.memberCardLevel.get(level);
			
			memberInfo.addBonusProints(points);
			OrderItemRepresentation orderItemRepresentation = new OrderItemRepresentation(product.getProductId(), product.getPrdName(), new BigDecimal(totalPrice), orderItemCommand.getAmount(), new BigDecimal(totalPrice));
			PaymentRepresentation paymentRepresentation = new PaymentRepresentation(paymentCommand.getType(), paymentCommand.getAmount()); 
			DiscountItemRepresentation discountItemRepresentation = new DiscountItemRepresentation(product.getProductId(), product.getPrdName(), new BigDecimal(discountDouble));
			
			resultMap.put("orderItemRepresentation", orderItemRepresentation);
			resultMap.put("paymentRepresentation", paymentRepresentation);
			resultMap.put("discountItemRepresentation", discountItemRepresentation);
			
			resultMap.put("totalPrice", totalPrice);
			return resultMap;
			
	 }
	 
	 private boolean isCanDiscount(Product product,String discount) {
		return  StringUtils.isNotEmpty(product.getDiscount()) &&  StringUtils.isNotEmpty(discount) && product.getDiscount().equals(discount);
	}
}
