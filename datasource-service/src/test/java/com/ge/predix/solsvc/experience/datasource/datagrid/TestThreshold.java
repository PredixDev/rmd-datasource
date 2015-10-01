package com.ge.predix.solsvc.experience.datasource.datagrid;
/**
 * 
 * 
 * @author 212421693
 */
public class TestThreshold {

	/**
	 * @param args args
	 */
	public static void main(String[] args) {
		Double currentValue = 18.7d;
		Double thresholdMin = -2d;
		Double thresholdMax = 15d;
		
		// current>=min
		if(currentValue.compareTo(thresholdMin) >=0  && currentValue.compareTo(thresholdMax)<=0){
		
			Double midRange = (thresholdMax - thresholdMin)/2;
			if(currentValue.compareTo(midRange) >= 0){
				Double value=  ((thresholdMax - currentValue )/(thresholdMax - thresholdMin) ) * 100;
				System.out.print("In range ^"+value); //$NON-NLS-1$
			}else {
				Double value=  ((currentValue - thresholdMin )/(thresholdMax - thresholdMin) ) * 100;
				System.out.print("In range v"+value); //$NON-NLS-1$
			}
			
		}else if (currentValue.compareTo(thresholdMin) < 0 ){
			System.out.print("v RED range v"+((currentValue - thresholdMin )/thresholdMin )*100 ); //$NON-NLS-1$
			
		}else if (currentValue.compareTo(thresholdMax) > 0 ){
			System.out.print("^ RED range v"+((thresholdMax - currentValue )/thresholdMax )*100 ); //$NON-NLS-1$
		}

	}

}
