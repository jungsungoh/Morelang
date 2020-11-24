
package com.morelang.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Amount {

	private Integer total, tax_free, vat, point, discount;

}
