/**
 * 
 */
package com.demo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Mehul
**/

@Data
public class Employee {

	@Getter
	@Setter
	private String empId;
	
	@Getter
	@Setter
    private String empName;
	
	@Setter
	@Getter
	private String emailId;
    
}
